package com.bit.module.system.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bit.base.dto.*;
import com.bit.base.dto.OaOrganization;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.BaseConst;
import com.bit.common.SysConst;
import com.bit.common.ResultCode;
import com.bit.common.consts.ApplicationTypeEnum;
import com.bit.common.consts.RedisKey;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.core.utils.CacheUtil;
import com.bit.module.system.bean.*;
import com.bit.module.system.dao.*;
import com.bit.module.system.feign.*;
import com.bit.module.system.service.IdentityService;
import com.bit.module.system.service.UserRelVolStationService;
import com.bit.module.system.service.UserService;
import com.bit.module.system.vo.*;
import com.bit.soft.sms.bean.SmsCodeRequest;
import com.bit.soft.sms.client.SmsFeignClient;
import com.bit.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserDao userDao;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private UserRelIdentityDao userRelIdentityDao;
    @Autowired
    private IdentityDao identityDao;
    @Autowired
    private UserRelPbOrgDao userRelPbOrgDao;
    @Autowired
    private UserRelAppDao userRelAppDao;
    @Autowired
    private OaDepartmentDao oaDepartmentDao;
    @Autowired
    private UserRelOaDepDao userRelOaDepDao;
    @Autowired
    private PartyMemberServiceFeign partyMemberServiceFeign;
    @Autowired
    private VolServiceFeign volServiceFeign;
    @Autowired
    private StationServiceFeign stationServiceFeign;
    @Autowired
    private UserRelVolStationDao userRelVolStationDao;
    @Autowired
    private UserRelVolStationService userRelVolStationService;
    @Autowired
    private PbServiceFeign pbServiceFeign;
    @Autowired
	private CboServiceFeign cboServiceFeign;
    @Autowired
	private UserRelCboDepDao userRelCboDepDao;
    @Autowired
	private DictDao dictDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private SmsFeignClient smsFeignClient;


    /**
     * 用户身份操作
     */
    @Autowired
    private IdentityService identityService;

    @Value("${atToken.expire}")
    private String atTokenExpire;
    @Value("${rtToken.expire}")
    private String rtTokenExpire;
    /**
     * 登陆
     * @param userLoginVO
     * @return
     */
    @Override
    public BaseVo login(UserLoginVO userLoginVO, HttpServletRequest request)  {
        BaseVo baseVo = new BaseVo();
        //要支持手机号，账户名，身份证号登陆，所以用正则判断是用户名、手机号还是身份证号
        //1. 获取用户名
        String username = userLoginVO.getUsername();
        String userNameRegex = "^[a-zA-Z]([.-_a-zA-Z0-9]{5,19})$";
        String idcardRegex="(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        String phoneRegex = "^1[0-9]{10}$";
        userLoginVO.setUsername(null);
        if (username.matches(idcardRegex)){
            // 把用户名放到身份证号里
            userLoginVO.setIdcard(username);
        }else if (username.matches(phoneRegex)){
            // 把用户名放到手机号里
            userLoginVO.setMobile(username);
        }else if (username.matches(userNameRegex)){
            userLoginVO.setUsername(username);
        }else {
            throw new BusinessException("请检查用户名，手机号或身份证号格式是否正确");
        }
        //2. 根据用户名或身份证号或手机号查询user对象
        User user = userDao.findByUserLoginVO(userLoginVO);
        if (user != null){
            if (user.getStatus() == 1){
                String pw = MD5Util.compute(userLoginVO.getPassword() + user.getSalt());
                if (!pw.equals(user.getPassword())) {
                    throw new BusinessException("密码错误");
                }
                if (userLoginVO.getTerminalId() == null) {
                    throw new BusinessException("接入端id不能为空");
                }
                UserInfo userInfo = new UserInfo();
                userLoginVO.setId(user.getId());
                userInfo.setIdcard(user.getIdcard());
                userInfo.setId(user.getId());

                //手机号
                if(StringUtils.isNotEmpty(user.getMobile())){
                    userInfo.setMobile(user.getMobile());
                }

                List<UserRelPbOrg> pbOrgs=userRelPbOrgDao.findByUserId(user.getId());
                if (pbOrgs.size()>0){
                    userInfo.setPbOrgId(pbOrgs.get(0).getPborgId().toString());
                }
                userInfo.setUsername(user.getUsername());
                userInfo.setTid(userLoginVO.getTerminalId());
                userInfo.setRealName(user.getRealName());


                String token = UUIDUtil.getUUID();
                userInfo.setToken(token);
                //rt token 失效时间为7天
                String rtToken = UUIDUtil.getUUID();
                RefreshTokenVO refreshTokenVO = new RefreshTokenVO();
                refreshTokenVO.setUserInfo(userInfo);
                refreshTokenVO.setAtKey(SysConst.TOKEN_PREFIX+userLoginVO.getTerminalId()+":"+token);
                Map map = new HashMap<>();
                //用户应用权限
                List<UserRelApp> apps = userRelAppDao.findByUserId(user.getId());
                List<Integer> appIdList = new ArrayList<>();
                for (UserRelApp app : apps) {
                    Integer appId = app.getAppId();
                    appIdList.add(appId);
                }
                map.put("appIds",appIdList);
                map.put("token", token);
                map.put("refreshToken", rtToken);
                List<Identity> identitys = identityDao.findByUserId(user.getId());
                userInfo.setIdentitys(identitys);
                //查询用户所有的角色
                List<Long> roleIds = roleDao.findRoleIdByUserId(user.getId());
                userInfo.setRoleIds(roleIds);

                map.put("identitys",identitys);
                map.put("id",user.getId());
                map.put("username",user.getUsername());
                map.put("realName",user.getRealName());
                map.put("createType",user.getCreateType());
                userInfo.setAppIds(appIdList);

                //设置社区信息
				CboInfo cboInfo = getCboInfo(user.getId());
				userInfo.setCboInfo(cboInfo);
				map.put("cboInfo",cboInfo);

				//设置安检
				List<Dict> anjian = dictDao.findByModule(SysConst.ANJIAN_MODULE_NAME);
				Security security = new Security();
				security.setId(Long.valueOf(anjian.get(0).getDictCode()));
				security.setName(anjian.get(0).getDictName());
				userInfo.setSecurity(security);
				map.put("security",security);
				//设置环保
				List<Dict> huanbao = dictDao.findByModule(SysConst.HUANBAO_MODULE_NAME);
				Environment environment = new Environment();
				environment.setId(huanbao.get(0).getId());
				environment.setName(huanbao.get(0).getDictName());
				userInfo.setEnvironment(environment);
				map.put("environment",environment);

                //at token 失效时间为1天
                String userJson = JSON.toJSONString(userInfo);
                logger.info("denglushujian ----------------------------------"+Long.valueOf(atTokenExpire) );
                cacheUtil.set(SysConst.TOKEN_PREFIX+userLoginVO.getTerminalId()+":"+token, userJson,Long.valueOf(atTokenExpire));
                String rtJson = JSON.toJSONString(refreshTokenVO);
                cacheUtil.set(SysConst.REFRESHTOKEN_TOKEN_PREFIX + userLoginVO.getTerminalId() + ":" + rtToken, rtJson, Long.valueOf(rtTokenExpire));

                //验证推送绑定客户端(只针对APP用户)
                //现阶段无推送 先行注调
                /*if(!userLoginVO.getTerminalId().equals(1)){
                    checkUserPushBinding(userLoginVO);
                }*/
                baseVo.setData(map);
            }else if (user.getStatus() == 2){
                throw new BusinessException("该账户已经被停用，禁止登录");
            }
        }else {
            throw new BusinessException("用户名或者密码错误");
        }

        return baseVo;
    }

	/**
	 * 处理社区信息
	 * @param userId
	 * @return
	 */
    private CboInfo getCboInfo(Long userId){
    	CboInfo cboInfo = new CboInfo();
    	//社区id集合
    	List<Long> orgIds = new ArrayList<>();
    	//存放与用户有关的社区
    	List<OaOrganization> oaOrganizationList = new ArrayList<>();
        OaDepartment byUserId = userRelCboDepDao.findByUserId(userId);
        if (byUserId==null){
        	return null;
		}

		OaOrganization oaOrganization = new OaOrganization();
		BeanUtils.copyProperties(byUserId,oaOrganization);
		cboInfo.setCurrentCboOrg(oaOrganization);
        //如果是社区办
        if (byUserId.getId().equals(Long.valueOf(SysConst.SHE_QU_BAN_ID))){

			List<OaDepartment> oaOrganizations = oaDepartmentDao.findByIdLike(SysConst.SHE_QU_BAN_ID, null);
			if (CollectionUtils.isNotEmpty(oaOrganizationList)){
				for (OaDepartment oaDepartment : oaOrganizations) {
					OaOrganization oa = new OaOrganization();
					BeanUtils.copyProperties(oaDepartment,oa);
					oaOrganizationList.add(oa);
				}
			}
			cboInfo.setCboOrgs(oaOrganizationList);
			cboInfo.setUserType(SysConst.USER_TYPE_ORG_SHE_QU_BAN);
		}else {
			List<OaOrganization> cboOrgs = new ArrayList<>();
			cboOrgs.add(oaOrganization);
			cboInfo.setCboOrgs(cboOrgs);
			cboInfo.setUserType(SysConst.USER_TYPE_ORG);
		}

		//调用feign 批量查询小区信息
		List<Community> communityList = cboServiceFeign.batchSelectByOrgIds(orgIds);
		if (CollectionUtils.isNotEmpty(communityList)){
			cboInfo.setCurrentCommunity(communityList.get(0));
		}
		cboInfo.setCommunities(communityList);
		return cboInfo;
	}

    /**
     * 党建app登录
     * @return
     */
    @Override
    @Transactional
    public BaseVo appPbLogin(UserLoginVO userLoginVO, HttpServletRequest request) {
        BaseVo baseVo = new BaseVo();
        if (userLoginVO.getLoginType()==null){
            baseVo.setCode(ResultCode.IDENTITY_NOT_CHOOSE.getCode());
            baseVo.setMsg(ResultCode.IDENTITY_NOT_CHOOSE.getInfo());
            return baseVo;
        }
        //1. 获取用户名
        String username = userLoginVO.getUsername();
        String phoneRegex = "^1[0-9]{10}$";
        String userNameRegex = "^[a-zA-Z]([.-_a-zA-Z0-9]{5,19})$";
        if (username.matches(userNameRegex)){
            userLoginVO.setMobile(null);
            userLoginVO.setUsername(username);
        } else if (username.matches(phoneRegex)){
            userLoginVO.setMobile(username);
            userLoginVO.setUsername(null);
        } else {
            throw new BusinessException("用户名或密码错误");
        }
        //2. 根据用户名或身份证号或手机号查询user对象
        User user = userDao.findByUserLoginVO(userLoginVO);
        if (user!=null){
            //如果用户停用 抛出异常
            if (user.getStatus().equals(SysConst.USER_STATUS_STOP)){
                baseVo.setCode(ResultCode.USER_STATUS_STOP.getCode());
                baseVo.setMsg(ResultCode.USER_STATUS_STOP.getInfo());
                return baseVo;
            }
            String pw = MD5Util.compute(userLoginVO.getPassword() + user.getSalt());
            if (!pw.equals(user.getPassword())) {
                baseVo.setCode(ResultCode.PASSWORD_WRONG.getCode());
                baseVo.setMsg(ResultCode.PASSWORD_WRONG.getInfo());
                return baseVo;
            }
            if (userLoginVO.getTerminalId() == null) {
                baseVo.setCode(ResultCode.TERMINALID_WRONG.getCode());
                baseVo.setMsg(ResultCode.TERMINALID_WRONG.getInfo());
                return baseVo;
            }
            UserInfo userInfo = new UserInfo();
            userLoginVO.setId(user.getId());
            userInfo.setIdcard(user.getIdcard());
            userInfo.setId(user.getId());
            userInfo.setRealName(user.getRealName());
            userInfo.setUsername(user.getUsername());
            userInfo.setTid(userLoginVO.getTerminalId());
            UserRelAppVO userRelAppVO1=new UserRelAppVO ();
            userRelAppVO1.setUserId(user.getId());
            userRelAppVO1.setAppId(userLoginVO.getAppId());
            //用户应用权限
            List<UserRelApp> apps = userRelAppDao.findByUserId(user.getId());
            List<Integer> appIdList = new ArrayList<>();
            ArrayList<Identity> returnIdentity = new ArrayList<>();
            List<Identity> identitys = identityDao.findByUserId(user.getId());//当前身份
            List<Identity> defaultIdentity=identityDao.findIdentListByAppId(Long.valueOf(userLoginVO.getAppId()),1);//当前的默认身份
            PartyMember partyMember =null;
            BaseVo partyMemberData= partyMemberServiceFeign.findByIdCard(user.getIdcard());
            if(partyMemberData.getData()!=null){
                String s1 = JSON.toJSONString(partyMemberData.getData());
                partyMember = JSON.parseObject(s1,PartyMember.class);
            }
            for (UserRelApp app : apps) {
                Integer appId = app.getAppId();
                if(app.getAppId().equals(userLoginVO.getAppId())){
                    appIdList.add(appId);
                }
            }
            if(appIdList.size()==0){
                //无此app权限的话分有党员信息和无党员信息的
                /**增加默认的用户判断逻辑 start***/
                if(partyMember!=null){
                    //添加验证党员停用状态
                    if (partyMember.getStatus().equals(SysConst.PARTY_MEMBER_STATUS_STOP)){
                        baseVo.setCode(ResultCode.PARTY_MEMBER_STATUS_STOP.getCode());
                        baseVo.setMsg(ResultCode.PARTY_MEMBER_STATUS_STOP.getInfo());
                        return baseVo;
                    }
                    if(!org.springframework.util.StringUtils.isEmpty(partyMember.getOrgId()) ){
                        //建身份和关联关系
                        if(userLoginVO.getLoginType().equals(SysConst.LOGIN_TYPE_DANG)){
                            UserRelIdentity userRelIdentity=new UserRelIdentity();
                            userRelIdentity.setIdentityId(defaultIdentity.get(0).getId());
                            userRelIdentity.setUserId(user.getId());
                            userRelIdentityDao.add(userRelIdentity);
                            UserRelApp userRelApp=new UserRelApp ();
                            userRelApp.setAppId(userLoginVO.getAppId());
                            userRelApp.setUserId(userLoginVO.getId());
                            userRelAppDao.add(userRelApp);
                            returnIdentity.add(defaultIdentity.get(0));
                            appIdList.add(userLoginVO.getAppId());//拼装
                        }
                    }
                }else{
                    if(userLoginVO.getLoginType().equals(SysConst.LOGIN_TYPE_DANG)){
                        baseVo.setCode(ResultCode.NOT_PARTY_MEMBER.getCode());
                        baseVo.setMsg(ResultCode.NOT_PARTY_MEMBER.getInfo());
                        return baseVo;
                    }
                }
            }else{
                for (Identity identity : identitys) {
                    if (identity.getAppId().equals(userLoginVO.getAppId())){
                        returnIdentity.add(identity);
                    }
                }
            }

            //管理员
            if (userLoginVO.getLoginType().equals(SysConst.LOGIN_TYPE_ADMIN)){
                //应用有权限
                if(appIdList.size()>0){
                    Integer j=null;
                    if(returnIdentity.size()>0){
                        //if (partyMember!=null){
                            //验证是否是党员,转出或无党员信息则去除党员身份
                            if (partyMember==null||partyMember.getOrgId()==null){
                                for(int t=0;t<returnIdentity.size();t++){
                                    if(returnIdentity.get(t).getAcquiesce().equals(1)){
                                        j=t;
                                        break;
                                    }
                                }
                                if(j!=null){
                                    logger.info("去除无用身份---------"+j+JSON.toJSONString(returnIdentity));
                                    userRelIdentityDao.delete(null,user.getId(),returnIdentity.get(j).getId());
                                    returnIdentity.remove(j.intValue());//去除后端平台添加的默认身份，因为业务表中的无党员信息或迁出
                                    logger.info("去除无用身份后---------"+JSON.toJSONString(returnIdentity));
                                }
                            }else{
                                //判断是否有默认身份，默认身份是党员
                                int lsnum=0;
                                for (Identity ls:returnIdentity){
                                    if(ls.getAcquiesce().equals(1)){
                                        lsnum++;
                                    }
                                }
                                if(lsnum==0){
                                    UserRelIdentity userRelIdentity=new UserRelIdentity();
                                    userRelIdentity.setIdentityId(defaultIdentity.get(0).getId());
                                    userRelIdentity.setUserId(user.getId());
                                    userRelIdentityDao.add(userRelIdentity);
                                    returnIdentity.add(defaultIdentity.get(0));
                                }
                            }
                       // }
                    }
                    List<UserRelPbOrg> pbOrgs=userRelPbOrgDao.findByUserId(user.getId());
                    if (pbOrgs.size()>0){
                        userInfo.setPbOrgId(pbOrgs.get(0).getPborgId().toString());
                    }
                }else{
                    baseVo.setCode(ResultCode.NOT_AUTHORIZE.getCode());
                    baseVo.setMsg(ResultCode.NOT_AUTHORIZE.getInfo());
                    return baseVo;
                }
            }
            //党员
            if (userLoginVO.getLoginType().equals(SysConst.LOGIN_TYPE_DANG)){
                if (partyMember!=null){
                    //验证党员是否停用
                    if (partyMember.getStatus().equals(SysConst.PARTY_MEMBER_STATUS_STOP)){
                        baseVo.setCode(ResultCode.PARTY_MEMBER_STATUS_STOP.getCode());
                        baseVo.setMsg(ResultCode.PARTY_MEMBER_STATUS_STOP.getInfo());
                        return baseVo;
                    }else{
                        if(partyMember.getOrgId()==null){
                            //党组织已转出
                            baseVo.setCode(ResultCode.NOT_PARTY_MEMBER.getCode());
                            baseVo.setMsg(ResultCode.NOT_PARTY_MEMBER.getInfo());
                            return baseVo;
                        }
                    }
                    userInfo.setPbOrgId(partyMember.getOrgId());
                }else{
                    baseVo.setCode(ResultCode.NOT_PARTY_MEMBER.getCode());
                    baseVo.setMsg(ResultCode.NOT_PARTY_MEMBER.getInfo());
                    return baseVo;
                }
            }
            String token = UUIDUtil.getUUID();
            userInfo.setToken(token);
            //rt token 失效时间为7天
            String rtToken = UUIDUtil.getUUID();
            RefreshTokenVO refreshTokenVO = new RefreshTokenVO();
            refreshTokenVO.setUserInfo(userInfo);
            refreshTokenVO.setAtKey(SysConst.TOKEN_PREFIX+userLoginVO.getTerminalId()+":"+token);
            Map map = new HashMap<String,Object>();
            //用户应用权限

            map.put("appIds",appIdList);
            map.put("token", token);
            map.put("refreshToken", rtToken);
			//全部身份 20190902 添加
			List<Identity> allIdentitys = new ArrayList<>();
            //logintype 0- 管理员 1-党员
            //          dang - 0  admin - 1
            for (Identity identity : returnIdentity) {
                if (userLoginVO.getLoginType().equals(SysConst.LOGIN_TYPE_ADMIN)){
                    if (identity.getAcquiesce().equals(SysConst.IDENTITY_ACQUIESCE_NOT_DEFAULT)){
                        userInfo.setCurrentIdentity(identity.getId());
                        break;
                    }
                }
                if (userLoginVO.getLoginType().equals(SysConst.LOGIN_TYPE_DANG)){
                    if (identity.getAcquiesce().equals(SysConst.IDENTITY_ACQUIESCE_DEFAULT)){
                        userInfo.setCurrentIdentity(identity.getId());
                        break;
                    }else {
                    	//20190902
                    	//先要查询下有没有党员身份
						//判断是不是党员
						if (partyMember!=null){
							//查询单元身份记录
							Identity defaultIdentityByAppIdSql = identityDao.getDefaultIdentityByAppIdSql(ApplicationTypeEnum.APPLICATION_PB.getApplicationId().longValue(), SysConst.IDENTITY_ACQUIESCE_DEFAULT);
							UserRelIdentityVO userRelIdentityVO = new UserRelIdentityVO();
							userRelIdentityVO.setIdentityId(defaultIdentityByAppIdSql.getId());
							userRelIdentityVO.setUserId(user.getId());
							List<UserRelIdentity> byConditionPage = userRelIdentityDao.findByConditionPage(userRelIdentityVO);
							//如果查询出有党员身份就略过，如果没有就新增党员身份
							if (CollectionUtils.isEmpty(byConditionPage)){
								UserRelIdentity relIdentity = new UserRelIdentity();
								//党员身份是3
								relIdentity.setIdentityId(defaultIdentityByAppIdSql.getId());
								relIdentity.setUserId(user.getId());
								userRelIdentityDao.add(relIdentity);
								userInfo.setCurrentIdentity(defaultIdentityByAppIdSql.getId());
								//添加所有身份
								allIdentitys.add(defaultIdentityByAppIdSql);
							}
						}
                    }
                }
            }
			//20190902 添加
			allIdentitys.addAll(returnIdentity);

            userInfo.setIdentitys(allIdentitys);
            map.put("id",user.getId());
            map.put("username",user.getUsername());
            map.put("realName",user.getRealName());
            map.put("createType",user.getCreateType());
            userInfo.setAppIds(appIdList);
            map.put("userInfo",userInfo);
            //at token 失效时间为1天
            String userJson = JSON.toJSONString(userInfo);
            cacheUtil.set(SysConst.TOKEN_PREFIX+userLoginVO.getTerminalId()+":"+token, userJson,Long.valueOf(atTokenExpire));
            String rtJson = JSON.toJSONString(refreshTokenVO);
            cacheUtil.set(SysConst.REFRESHTOKEN_TOKEN_PREFIX + userLoginVO.getTerminalId() + ":" + rtToken, rtJson, Long.valueOf(rtTokenExpire));
            baseVo.setData(map);

        }else {
            baseVo.setCode(ResultCode.USER_NOT_EXIST.getCode());
            baseVo.setMsg(ResultCode.USER_NOT_EXIST.getInfo());
            return baseVo;
        }

        return baseVo;
    }


    /**
     * 党建app注册
     * @return
     */
    @Override
    @Transactional
    public BaseVo appPbRegister(User user) {
        //随机密码盐
        String salt = StringRandom.getStringRandom(SysConst.RANDOM_PASSWORD_SALT);
        //密码和盐=新密码  md5加密新密码
        String password = MD5Util.compute(user.getPassword() + salt);
        user.setUsername("u"+user.getMobile());
        user.setPassword(password);
        user.setSalt(salt);
        user.setInsertTime(new Date());
        user.setStatus(SysConst.USER_STATUS);
        user.setCreateType(SysConst.USER_REGISTER);
        userDao.add(user);
        Long userId = user.getId();
        //存中间表t_sys_user_rel_app
        UserRelApp userRelApp = new UserRelApp();
        userRelApp.setAppId(  ApplicationTypeEnum.APPLICATION_PB.getApplicationId().intValue());
        userRelApp.setUserId(userId);
        userRelAppDao.add(userRelApp);
        //存中间表t_sys_user_rel_identity
        Identity identity = new Identity();
        identity.setAppId( ApplicationTypeEnum.APPLICATION_PB.getApplicationId().intValue());
        identity.setAcquiesce(1);
        Identity identity1 = identityDao.findByAppIdAndAcquiesce(identity);

        UserRelIdentity userRelIdentity = new UserRelIdentity();
        userRelIdentity.setIdentityId(identity1.getId());
        userRelIdentity.setUserId(userId);
        userRelIdentityDao.add(userRelIdentity);

        return new BaseVo();
    }

    /**
     * 党建app注册 验证身份证
     * @return
     */
    @Override
    public BaseVo appPbVerifyIdCard(User user) {
        BaseVo baseVo = new BaseVo();
        String idcard = user.getIdcard();
        String realname = user.getRealName();
        //检查身份证格式是否正确
        String idcardRegex="(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        if (!idcard.matches(idcardRegex)){
            baseVo.setCode(ResultCode.IDCARD_FORMAT_WRONG.getCode());
            baseVo.setMsg(ResultCode.IDCARD_FORMAT_WRONG.getInfo());
            return baseVo;
        }
        //检查是否为党员 身份证校验
        BaseVo b1 = partyMemberServiceFeign.findByIdCard(idcard);
        if (b1.getData()!=null) {
            String s = JSON.toJSONString(b1.getData());
            PartyMember partyMember = JSON.parseObject(s, PartyMember.class);
            //验证是否是党员
            if (partyMember == null) {
                baseVo.setCode(ResultCode.NOT_PARTY_MEMBER.getCode());
                baseVo.setMsg(ResultCode.NOT_PARTY_MEMBER.getInfo());
                return baseVo;
            }
            //验证党员是否停用
            if (partyMember.getStatus().equals(SysConst.PARTY_MEMBER_STATUS_STOP)){
                baseVo.setCode(ResultCode.PARTY_MEMBER_STATUS_STOP.getCode());
                baseVo.setMsg(ResultCode.PARTY_MEMBER_STATUS_STOP.getInfo());
                return baseVo;
            }
            //验证党员姓名和用户姓名是否一致
            if (!partyMember.getName().equals(user.getRealName())){
                throw new BusinessException("姓名或身份证输入有误，请重新输入");
            }
            //验证党员身份证和用户身份证是否一致
            if (!partyMember.getIdCard().equals(user.getIdcard())){
                throw new BusinessException("姓名或身份证输入有误，请重新输入");
            }
        }else {
            baseVo.setCode(ResultCode.NOT_PARTY_MEMBER.getCode());
            baseVo.setMsg(ResultCode.NOT_PARTY_MEMBER.getInfo());
            return baseVo;
        }
        //验证平台身份证
        UserVO check1 = new UserVO();
        check1.setIdcard(idcard);
        check1.setRealName(realname);
        List<User> byConditionPage = userDao.findByConditionPage(check1);
        if (byConditionPage!=null && byConditionPage.size() >0){
            baseVo.setCode(ResultCode.SYS_ID_CARD_EXIST.getCode());
            baseVo.setMsg(ResultCode.SYS_ID_CARD_EXIST.getInfo());
            return baseVo;
        }
        if (byConditionPage == null || byConditionPage.size()==0){
            return baseVo;
        }
        return baseVo;
    }

    /**
     * 党建app注册 验证手机号
     * @return
     */
    @Override
    public BaseVo appPbVerifyMobile(User user) {
        BaseVo baseVo = new BaseVo();
        //校验手机号格式
        String phoneRegex = "^1[0-9]{10}$";
        String mobile = user.getMobile();
        if (!mobile.matches(phoneRegex)){
            baseVo.setCode(ResultCode.MOBILE_FORMAT_WRONG.getCode());
            baseVo.setMsg(ResultCode.MOBILE_FORMAT_WRONG.getInfo());
            return baseVo;
        }
        //判断手机号是否存在
        UserVO userVO = new UserVO();
        userVO.setMobile(mobile);
        int i = userDao.checkMobile(userVO);
        if (i>0){
            baseVo.setCode(ResultCode.MOBILE_ALREADY_EXIST.getCode());
            baseVo.setMsg(ResultCode.MOBILE_ALREADY_EXIST.getInfo());
            return baseVo;
        }
        UserVO check2 = new UserVO();
        check2.setMobile(user.getMobile());
        check2.setIdcard(user.getIdcard());
        check2.setRealName(user.getRealName());
        List<User> byConditionPage1 = userDao.findByConditionPage(check2);
        if (byConditionPage1!=null && byConditionPage1.size() == 1){
            baseVo.setCode(ResultCode.MOBILE_ALREADY_EXIST.getCode());
            baseVo.setMsg(ResultCode.MOBILE_ALREADY_EXIST.getInfo());
            return baseVo;
        }else if (byConditionPage1.size() > 1){
            throw new BusinessException("手机号重复");
        }

        return baseVo;
    }



    /**
     * 党建app 验证短信验证码
     * @param user
     * @return
     */
    @Override
    public BaseVo appPbVerifySmsCode(User user) {
        BaseVo baseVo = new BaseVo();
        //校验验证码
        /*String regisVerCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_REGISTER_VERIFICATION_CODE,user.getMobile(),  String.valueOf(TerminalTypeEnum.TERMINALTOPBAPP.getTid()));
        String code = (String) cacheUtil.get(regisVerCodeKey);
        if (!user.getSmsCode().equals(code) || StringUtil.isEmpty(code)){
            baseVo.setMsg(ResultCode.PB_APP_REGISTER_SMS_CODE_INCORRECT.getInfo());
            baseVo.setCode(ResultCode.PB_APP_REGISTER_SMS_CODE_INCORRECT.getCode());
            return baseVo;
        }*/
        SmsCodeRequest smsCodeRequest=new SmsCodeRequest();
        smsCodeRequest.setSmsTemplateId(TerminalTypeEnum.TERMINALTOPBAPP.getTid());
        smsCodeRequest.setSmsCode(user.getSmsCode());
        smsCodeRequest.setMobileNumber(user.getMobile());
        BaseVo feignDto=smsFeignClient.checkCode(smsCodeRequest);
        if(feignDto.getCode()==ResultCode.SMS_CHECKED_FAIL.getCode()){
            throwBusinessException("验证码不正确");
        }else if((feignDto.getCode()==ResultCode.SMS_CHECKED_NO_EFFECT.getCode())){
            throwBusinessException("验证码失效");
        }
        return baseVo;
    }

    /**
     * 登出
     * @param
     * @return
     */
    @Override
    public BaseVo logout() {

        //获取token
        UserInfo userInfo = getCurrentUserInfo();
        String token = userInfo.getToken();
        String terminalId = userInfo.getTid().toString();

        cacheUtil.del("token:"+terminalId+":"+token);
        return new BaseVo();
    }

    /**
     * 验证登录用户推送绑定客户端
     * @param userLoginVO
     */
    private void checkUserPushBinding(UserLoginVO userLoginVO) {

        //检测用户推送绑定信息是否需要存在
        List<PushBinding> pushBindingList = userDao.checkUserBindingSql(userLoginVO);
        if(pushBindingList.size()>0){
            //获取设备ID看是否需要更新
            String registrationId = pushBindingList.get(0).getRegistrationId();

            //检测设备是否需要更新
            if(!registrationId.equals(userLoginVO.getRegistrationId())){
                userDao.updatePushBindingSql(userLoginVO);
            }
        }else {
            userDao.insertPushBindingSql(userLoginVO);
        }
    }


    @Override
    public BaseVo list(UserVO userVO) {
        userVO.setData(userDao.findByConditionPage(userVO));
        return userVO;
    }

    /**
     * 分页模糊查询
     * @param userVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(UserVO userVO) {
        PageHelper.startPage(userVO.getPageNum(), userVO.getPageSize());
        List<User> list = userDao.findByConditionPage(userVO);
        PageInfo<User> pageInfo = new PageInfo<User>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查看
     * @param id
     * @return
     */
    @Override
    public User findById(Long id) {
        return userDao.findById(id);
    }

    /**
     * 查看---业务
     * @param id
     * @return
     */
    @Override
    public User findRealById(Long id) {
        User user = userDao.findById(id);
        List<Identity> identities=identityDao.findByUserId(id);

        List<Long> prOrgIds = new ArrayList<>();
        //在查询党组织信息
        List<UserRelPbOrg> byUserId = userRelPbOrgDao.findByUserId(id);
        for (UserRelPbOrg userRelPbOrg : byUserId) {
            prOrgIds.add(userRelPbOrg.getPborgId());
        }
        List<PbOrganization> pbOrganizations = new ArrayList<>();
        //调用feign查询组织信息
        BaseVo b11 = pbServiceFeign.findOrganizationsByIds(prOrgIds);
        if (b11.getData()!=null){
            String ss = JSON.toJSONString(b11.getData());
            List<OrganizationPb> OrganizationPbs = JSONArray.parseArray(ss,OrganizationPb.class);
            for (OrganizationPb organizationPb : OrganizationPbs) {
                PbOrganization pbOrganization = new PbOrganization();
                BeanUtils.copyProperties(organizationPb,pbOrganization);
                pbOrganization.setId(organizationPb.getId());
                pbOrganization.setPcode(organizationPb.getPCode());
                pbOrganizations.add(pbOrganization);
            }
        }


        if (pbOrganizations.size()>0){
            for (PbOrganization pbOrganization : pbOrganizations) {
                pbOrganization.setStrId(RadixUtil.toFullBinaryString(Long.valueOf(pbOrganization.getId())));
            }
        }
        List<OaDepartment> oaDepartments=oaDepartmentDao.findByUserId(id);
        user.setOaDepartments(oaDepartments);

        List<UserRelApp> userRelApps=userRelAppDao.findByUserId(id);
        List<Integer> integers = new ArrayList<>();
        for (UserRelApp userRelApp : userRelApps) {
            integers.add(userRelApp.getAppId());
        }
        UserRelVolStationVO userRelVolStationVO = new UserRelVolStationVO();
        userRelVolStationVO.setUserId(id);
        List<UserRelVolStation> userRelVolStationList = userRelVolStationDao.findByConditionPage(userRelVolStationVO);
        for (UserRelVolStation userRelVolStation : userRelVolStationList) {
            BaseVo b1 = stationServiceFeign.reflectById(userRelVolStationList.get(0).getStationId());
            String s = JSON.toJSONString(b1.getData());
            Station station = JSON.parseObject(s,Station.class);
            if (station==null){
                throw new BusinessException("站点不存在");
            }
            userRelVolStation.setStationName(station.getStationName());
        }
        //获取社区关联关系
		OaDepartment cboRel = userRelCboDepDao.findByUserId(id);

		user.setIdentities(identities);
        user.setPbOrganizations(pbOrganizations);
        user.setApps(integers);
        user.setUserRelVolStationList(userRelVolStationList);

		List<OaDepartment> oaDepartmentList = new ArrayList<>();
		if (cboRel!=null){
            oaDepartmentList.add(cboRel);
        }
		user.setCboDepartments(oaDepartmentList);
        return user;
    }

    /**
     * 校验username唯一
     * @param userVO
     * @return
     */
    @Override
    public BaseVo checkUsername(UserVO userVO) {
        BaseVo baseVo = new BaseVo();
        Boolean flag;
        //更新校验
        if (userVO.getId() != null) {
            //原数据（旧数据）
            User byId = userDao.findById(userVO.getId());
            String username = byId.getUsername();
            if (username.equals(userVO.getUsername())) {
                flag = true;
            } else {
                int count = userDao.checkUsername(userVO);
                if (count > 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        } else {
            //保存校验
            int count = userDao.checkUsername(userVO);
            if (count > 0) {
                flag = false;
            } else {
                flag = true;
            }
        }
        baseVo.setData(flag);
        return baseVo;
    }

    /**
     * 注册
     * @param user
     * @return
     */
    /*@Override
    @Transactional
    public BaseVo registerUser(User user) {
        int count = userDao.findCountByUsername(user.getMobile());
        if (count > 0){
            throw new BusinessException("用户名重复");
        }else {
            user.setUsername(user.getMobile());
            //随机密码盐
            String salt = StringRandom.getStringRandom(SysConst.RANDOM_PASSWORD_SALT);
            //密码和盐=新密码  md5加密新密码
            String password = MD5Util.compute(user.getPassword() + salt);
            user.setPassword(password);
            user.setSalt(salt);
            user.setInsertTime(new Date());
            user.setStatus(SysConst.USER_STATUS);
            user.setCreateType(SysConst.USER_REGISTER);
            userDao.add(user);
            //自增主键id
            Long id = user.getId();

            //存中间表t_sys_user_rel_app
            UserRelApp userRelApp = new UserRelApp();
            userRelApp.setAppId(user.getDictId());
            userRelApp.setUserId(id);
            userRelAppDao.add(userRelApp);
            //存中间表t_sys_user_rel_identity
            Identity identity = new Identity();
            identity.setAppId(user.getDictId());
            identity.setAcquiesce(1);
            Identity identity1 = identityDao.findByAppIdAndAcquiesce(identity);

            UserRelIdentity userRelIdentity = new UserRelIdentity();
            userRelIdentity.setIdentityId(identity1.getId());
            userRelIdentity.setUserId(id);
            userRelIdentityDao.add(userRelIdentity);

            BaseVo baseVo = new BaseVo();
            return baseVo;
        }
    }*/

    /**
     * 新增
     * @param userVO
     */
    @Override
    @Transactional
    public void add(UserVO userVO)  {

        //随机密码盐
        String salt = StringRandom.getStringRandom(SysConst.RANDOM_PASSWORD_SALT);
        //密码和盐=新密码  md5加密新密码
        String password= MD5Util.compute(SysConst.RESET_PASSWORD + salt);
        userVO.setPassword(password);
        userVO.setSalt(salt);
        userVO.setCreateType(SysConst.USER_CREATE_TYPE);
        userVO.setStatus(SysConst.USER_STATUS);
        userVO.setInsertTime(new Date());
        userDao.addNew(userVO);
        Long userId = userVO.getId();

        //如果接入端code不为空，那么则必须选择组织和身份
        this.batchAddApp(userId,userVO.getAppIds());
        //1表示党建
        /*if (userVO.getAppIds().contains(1) && userVO.getAppIds().contains(2)){
            this.batchAddPbOrg(userId,userVO.getPbOrgIds());
            this.batchAddIdent(userId,userVO.getIdentityIds());
            this.batchAddOaOrg(userId,userVO.getOaOrgIds());
            //2表示政务
        }else if (userVO.getAppIds().contains(2)){
            this.batchAddOaOrg(userId,userVO.getOaOrgIds());
            this.batchAddIdent(userId,userVO.getIdentityIds());
        }else if (userVO.getAppIds().contains(1)){
            this.batchAddPbOrg(userId,userVO.getPbOrgIds());
            this.batchAddIdent(userId,userVO.getIdentityIds());
        }else if (userVO.getAppIds().contains(8)){
            this.addUserVolStation(userId,userVO.getStationId());
        }else {
            this.batchAddPbOrg(userId,userVO.getPbOrgIds());
            this.batchAddIdent(userId,userVO.getIdentityIds());
            this.batchAddOaOrg(userId,userVO.getOaOrgIds());
        }*/


        /*修改后*/
        //新增人与身份关联
        this.batchAddIdentitys(userId,userVO.getIdentityIds());
        this.batchAddPbOrg(userId,userVO.getPbOrgIds());
        this.batchAddOaOrg(userId,userVO.getOaOrgIds());
        this.addUserVolStation(userId,userVO.getStationId());
        //添加社区的关联
		this.batchAddCbo(userId,userVO.getCboDepIds());
    }

    /**
     * 保存政务
     * @param userId
     * @param oaDepartments
     */
    @Transactional
    void batchAddOaOrg(Long userId, List<Integer> oaDepartments) {
        if (oaDepartments!=null && oaDepartments.size() > 0){
            List<UserRelOaDep> userRelOaDeps = new ArrayList<>();
            for (Integer oaDepartmentId : oaDepartments) {
                UserRelOaDep userRelOaDep = new UserRelOaDep();
                userRelOaDep.setUserId(userId);
                userRelOaDep.setDepId(oaDepartmentId);
                userRelOaDeps.add(userRelOaDep);
            }
            userRelOaDepDao.batchAdd(userRelOaDeps);
        }
    }

    /**
     * 保存字典 app
     * @param userId
     * @param apps
     */
    void batchAddApp(Long userId, List<Integer> apps) {
        if (apps != null && apps.size() > 0){
            List<UserRelApp> userRelApps = new ArrayList<>();
            for (Integer app : apps) {
                UserRelApp userRelApp = new UserRelApp();
                userRelApp.setUserId(userId);
                userRelApp.setAppId(app);
                userRelApps.add(userRelApp);
            }
            userRelAppDao.batchAdd(userRelApps);
        }
    }

    /**
     * 保存用户和志愿者站点关系表
     * @param userId
     * @param stationId
     */
	private void addUserVolStation(Long userId,Long stationId){
        if (userId!=null && stationId!=null){
            UserRelVolStation userRelVolStation = new UserRelVolStation();
            userRelVolStation.setStationId(stationId);
            userRelVolStation.setUserId(userId);
            userRelVolStationService.add(userRelVolStation);
        }
    }
    /**
     * 保存用户和党建组织中间表
     * @param userId
     * @param strIds
     */
	private void batchAddPbOrg(Long userId, List<Long> strIds) {
        if (CollectionUtils.isNotEmpty(strIds) && userId!=null) {
            List<UserRelPbOrg> userRelPbOrgs = new ArrayList<>();
            for (Long pborgId : strIds) {
                UserRelPbOrg userRelPbOrg = new UserRelPbOrg();
                userRelPbOrg.setUserId(userId);
                userRelPbOrg.setPborgId(pborgId);
                userRelPbOrgs.add(userRelPbOrg);
            }
            userRelPbOrgDao.batchAdd(userRelPbOrgs);
        }
    }

    /**
     * 保存用户和身份中间表
     * @param userId
     * @param identities
     */
    private void batchAddIdentitys(Long userId,List<Long> identities) {
        if (CollectionUtils.isNotEmpty(identities) && userId!=null){
            List<UserRelIdentity> userRelIdentities = new ArrayList<>();
            for (Long identityId : identities) {
                UserRelIdentity userRelIdentity = new UserRelIdentity();
                userRelIdentity.setIdentityId(identityId);
                userRelIdentity.setUserId(userId);
                userRelIdentities.add(userRelIdentity);
            }
            userRelIdentityDao.batchAdd(userRelIdentities);
        }
    }

	/**
	 * 保存用户和社区中间表
	 * @param userId
	 * @param depIds
	 */
	private void batchAddCbo(Long userId,List<Long> depIds){
    	if (CollectionUtils.isNotEmpty(depIds) && userId!=null){
    		List<UserRelCboDep> userRelCboDepList = new ArrayList<>();
			for (Long depId : depIds) {
				UserRelCboDep userRelCboDep = new UserRelCboDep();
				userRelCboDep.setUserId(userId);
				userRelCboDep.setDepId(depId.longValue());
				userRelCboDepList.add(userRelCboDep);
			}
			userRelCboDepDao.batchAdd(userRelCboDepList);
		}
	}

	/**
	 * 保存用户和志愿者站点中间表
	 * @param userId
	 * @param stationIds
	 */
	@Transactional(rollbackFor=Exception.class)
	void batchAddVolStation(Long userId,List<Long> stationIds){
		if (CollectionUtils.isNotEmpty(stationIds) && userId!=null){
			List<UserRelVolStation> userRelVolStationList = new ArrayList<>();
			for (Long stationId : stationIds) {
				UserRelVolStation userRelVolStation = new UserRelVolStation();
				userRelVolStation.setUserId(userId);
				userRelVolStation.setStationId(stationId);
				userRelVolStationList.add(userRelVolStation);
			}
			userRelVolStationDao.batchAdd(userRelVolStationList);
		}
	}

    /**
     * 修改
     * @param userVO
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void update(UserVO userVO) {
        Long userId = userVO.getId();
        userRelIdentityDao.delByUserId(userId);
        userRelPbOrgDao.delByUserId(userId);
        userRelAppDao.delByUserId(userId);
        userRelOaDepDao.delByUserId(userId);
        userRelVolStationDao.delByUserId(userId);
        userRelCboDepDao.delByUserId(userId);
        //如果接入端code不为空，那么则必须选择组织和身份
        this.batchAddApp(userId,userVO.getAppIds());
        //志愿者
        if (userVO.getAppIds().contains(ApplicationTypeEnum.APPLICATION_VOL.getApplicationId().intValue())){
            this.addUserVolStation(userId,userVO.getStationId());
        }
		//5 表示社区
		if (userVO.getAppIds().contains(ApplicationTypeEnum.APPLICATION_CBO.getApplicationId().intValue())){
			//添加社区关系
			this.batchAddCbo(userId,userVO.getCboDepIds());
		}
        //1表示党建
        if (userVO.getAppIds().contains(ApplicationTypeEnum.APPLICATION_PB.getApplicationId().intValue()) && userVO.getAppIds().contains(ApplicationTypeEnum.APPLICATION_OA.getApplicationId().intValue())){
            this.batchAddPbOrg(userId,userVO.getPbOrgIds());
            this.batchAddIdentitys(userId,userVO.getIdentityIds());
            this.batchAddOaOrg(userId,userVO.getOaOrgIds());
            //2表示政务
        }else if (userVO.getAppIds().contains(ApplicationTypeEnum.APPLICATION_OA.getApplicationId().intValue())){
            this.batchAddOaOrg(userId,userVO.getOaOrgIds());
            this.batchAddIdentitys(userId,userVO.getIdentityIds());
        }else if (userVO.getAppIds().contains(ApplicationTypeEnum.APPLICATION_PB.getApplicationId().intValue())){
            this.batchAddPbOrg(userId,userVO.getPbOrgIds());
            this.batchAddIdentitys(userId,userVO.getIdentityIds());
        }else {
            this.batchAddPbOrg(userId,userVO.getPbOrgIds());
            this.batchAddIdentitys(userId,userVO.getIdentityIds());
            this.batchAddOaOrg(userId,userVO.getOaOrgIds());
        }
        userVO.setStatus(SysConst.USER_STATUS);
        userDao.update(userVO);
    }

    /**
     * 停用启用用户
     * @param user
     */
    @Override
    @Transactional
    public void switchUser(User user) {
        userDao.switchUser(user);
    }

    /**
     * 重置密码
     * @param id
     */
    @Override
    @Transactional
    public void resetPassword(Long id)  {
        User user = new User();
        //用户id
        user.setId(id);
        //密码盐
        String salt = StringRandom.getStringRandom(SysConst.RANDOM_PASSWORD_SALT);
        //密码+密码盐=新的密码 （存数据库）
        String password= SysConst.RESET_PASSWORD+salt;
        //md5加密新的密码
        user.setPassword( MD5Util.compute(password));
        user.setSalt(salt);
        userDao.resetPassword(user);
    }

    @Override
    public BaseVo verifyUser(PartyMemberVO partyMemberVO) {
        BaseVo result = new BaseVo();
        // 根据姓名和身份证号通过接口验证该人是否是党员
        try {
            BaseVo baseVo = partyMemberServiceFeign.existMember(partyMemberVO.getName(),partyMemberVO.getIdCard());
            Boolean data = (Boolean) baseVo.getData();
            if (data){
                result.setData(data);
            }else {
                result.setData(data);
            }
        }catch (Exception e){
            throw new BusinessException("验证失败");
        }
        return result;
    }

   /* @Override
    public BaseVo sendVaildCode(User user) {
        //发送短信验证码
        //todo 目前暂无短信验证码接口，等待短信验证码确定，并确定验证码有效时间
        String phone = String.valueOf(user.getMobile());
        String num = String.valueOf(new Random().nextInt(899999) + 100000);
        cacheUtil.set(SysConst.REDIS_KEY_SMSCAPTCHA + phone, num, 6000 * 10 * 5);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }*/

  /*  @Override
    public BaseVo verifyVaildCode(VaildCodeVO vaildCodeVO) {
        String phone = String.valueOf(vaildCodeVO.getMobile());
        //从redis中取验证码
//        String code = (String) cacheUtil.get(SysConst.REDIS_KEY_SMSCAPTCHA + phone);
        //暂时写死验证码
        String code = "123456";
        //验证验证码是否正确
        if (vaildCodeVO.getVaildCode().equals(code)){
            BaseVo baseVo = new BaseVo();
            return baseVo;
        }else {
            throw new BusinessException("验证码错误");
        }
    }*/

    @Override
    @Transactional
    public BaseVo refreshToken(RefreshTokenVO refreshTokenVO) {
        BaseVo baseVo = new BaseVo();
        Map<String,Object> map = new HashMap<>();
        //根据refreshToken和接入端id去redis中查询
        String refreshToken = (String) cacheUtil.get(SysConst.REFRESHTOKEN_TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + refreshTokenVO.getRefreshToken());
        //如果能取到refreshToken，那么刷新accessToken
        if (StringUtils.isNotEmpty(refreshToken)){
            RefreshTokenVO tokenObject = JSONObject.parseObject(refreshToken, RefreshTokenVO.class);
            if (tokenObject != null){
                String accessToken = (String) cacheUtil.get(tokenObject.getAtKey());
                //如果accessToken不为空，表示token未过期，则删除在新生成token处理
                if (StringUtils.isNotEmpty(accessToken)){
                    //删除
                    cacheUtil.del(tokenObject.getAtKey());
                    //创建新的atToken
                    String atToken = UUIDUtil.getUUID();
                    cacheUtil.set(SysConst.TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + atToken,JSONObject.toJSONString(tokenObject.getUserInfo()),Long.valueOf(atTokenExpire));
                    //refreshToken 更新atKey
                    //获取Key过期时间
                    long expire = cacheUtil.getExpire(SysConst.REFRESHTOKEN_TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + refreshTokenVO.getRefreshToken());
                    RefreshTokenVO refreshTokenVOParam = new RefreshTokenVO();
                    refreshTokenVOParam.setUserInfo(tokenObject.getUserInfo());
                    refreshTokenVOParam.setAtKey(SysConst.TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + atToken);
                    String rtJson = JSON.toJSONString(refreshTokenVOParam);
                    cacheUtil.set(SysConst.REFRESHTOKEN_TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + refreshTokenVO.getRefreshToken(), rtJson,expire);
                    map.put("token",atToken);
                    baseVo.setData(map);
                    return baseVo;
                }else {
                    //新增
                    String atToken = UUIDUtil.getUUID();
                    cacheUtil.set(SysConst.TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + atToken,JSONObject.toJSONString(tokenObject.getUserInfo()),Long.valueOf(atTokenExpire));
                    //refreshToken 更新atKey
                    //获取Key过期时间
                    long expire = cacheUtil.getExpire(SysConst.REFRESHTOKEN_TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + refreshTokenVO.getRefreshToken());
                    RefreshTokenVO refreshTokenVOParam = new RefreshTokenVO();
                    refreshTokenVOParam.setUserInfo(tokenObject.getUserInfo());
                    refreshTokenVOParam.setAtKey(SysConst.TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + atToken);
                    String rtJson = JSON.toJSONString(refreshTokenVOParam);
                    cacheUtil.set(SysConst.REFRESHTOKEN_TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + refreshTokenVO.getRefreshToken(), rtJson,expire);
                    map.put("token",atToken);
                    baseVo.setData(map);
                    return baseVo;
                }
            }
        }else {
            throw new BusinessException("当前refreshToken已失效");
        }
        return baseVo;
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll("");
    }

    @Override
    @Transactional
    public BaseVo updatePwd(UserVO userVO) {
        BaseVo baseVo = new BaseVo();
        UserInfo userInfo = getCurrentUserInfo();
        User user = userDao.findById(userInfo.getId());
        String checkPwd = MD5Util.compute(userVO.getOldPassword() + user.getSalt());
        //表示密码正确
        if (checkPwd.equals(user.getPassword())){
            //随机密码盐
            String salt = StringRandom.getStringRandom(SysConst.RANDOM_PASSWORD_SALT);
            //密码和盐=新密码  md5加密新密码
            String password = MD5Util.compute(userVO.getPassword() + salt);
            userVO.setPassword(password);
            userVO.setSalt(salt);
            userVO.setId(userInfo.getId());
            userDao.update(userVO);
        }else {
            throw new BusinessException("原密码不正确");
        }
        return baseVo;
    }

	/**
	 * 批量更新设置身份
	 * @param userMultiSetVO
	 * @return
	 */
    @Override
    @Transactional
    public void multiSet(UserMultiSetVO userMultiSetVO) {
        List<Long> uids = userMultiSetVO.getUids();
        if (CollectionUtils.isEmpty(uids)){
            throwBusinessException("请选择用户");
        }
        for (Long userId : uids) {

            userRelIdentityDao.delByUserId(userId);
            userRelPbOrgDao.delByUserId(userId);
            userRelAppDao.delByUserId(userId);
            userRelOaDepDao.delByUserId(userId);
            userRelCboDepDao.delByUserId(userId);
            userRelVolStationDao.delByUserId(userId);

            this.batchAddApp(userId, userMultiSetVO.getAppIds());
            this.batchAddIdentitys(userId, userMultiSetVO.getIdentityIds());
            this.batchAddPbOrg(userId, userMultiSetVO.getPbOrgIds());
            this.batchAddOaOrg(userId, userMultiSetVO.getOaOrgIds());
			this.batchAddCbo(userId,userMultiSetVO.getCboIds());
			this.batchAddVolStation(userId,userMultiSetVO.getStationIds());

        }
        //批量更新用户状态
		userDao.batchUpdateUserStatus(uids, SysConst.USER_STATUS);
    }

    @Override
    public BaseVo batchFindByAppIdAndIds(UserMultiListVO userVO) {
        List list = userDao.batchFindByAppIdAndIds(userVO.getUids(),userVO.getAppId());
        BaseVo baseVo = new BaseVo();
        baseVo.setData(list);
        return baseVo;
    }

    @Override
    public BaseVo findUserListByDepartmentIds(UserVO userVO) {
        Map<String,Object> map = new HashMap<>();
        map.put("ids",userVO.getDepIds());
        map.put("realName",userVO.getRealName());
        List<User> userList = userDao.findByDepartmentIds(map);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(userList);
        return baseVo;
    }

    @Override
    public BaseVo findByAppId() {
        //todo 修改数据结构
        List<OaDepartment> departments = oaDepartmentDao.findAll("");
        List<UserAndDepVO> users = userDao.findByAppId();
        List<UserDeptVO> list = new ArrayList<>();
        for (OaDepartment department : departments) {
            List<UserAndDepVO> userList = new ArrayList<>();
            for (UserAndDepVO user : users) {
                if (department.getId().equals(user.getDepId())){
                    userList.add(user);
                }
            }
            UserDeptVO userDeptVO = new UserDeptVO();
            userDeptVO.setDepName(department.getName());
            userDeptVO.setUserList(userList);
            list.add(userDeptVO);
        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(list);
        return baseVo;
    }

    @Override
    public BaseVo userInfo(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        //从token中获取当前用户
        UserInfo userInfo = getCurrentUserInfo();

        //包装志愿者信息
        wrapVolunteerInfo(userInfo);
        //包装党建信息
        wrapPbInfo(userInfo,result);
        //包装政务信息
        wrapOAInfo(userInfo,result);

        String userJson = JSON.toJSONString(userInfo);
        int tid = userInfo.getTid();
        String token = userInfo.getToken();
        cacheUtil.set(SysConst.TOKEN_PREFIX+tid+":"+token, userJson,Long.valueOf(atTokenExpire));

        result.put("userInfo",userInfo);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(result);

        return baseVo;
    }

    private void wrapOAInfo(UserInfo userInfo, Map<String, Object> result) {
        if (!userInfo.getAppIds().contains(ApplicationTypeEnum.APPLICATION_OA.getApplicationId().intValue())) {
			return;
		}
        //先查询部门信息
        List<OaDepartment> departments = oaDepartmentDao.findByUserId(userInfo.getId());
        result.put("departments",departments);
    }

    private void wrapPbInfo(UserInfo userInfo,Map result) {
        if (!userInfo.getAppIds().contains(ApplicationTypeEnum.APPLICATION_PB.getApplicationId().intValue())) {
			return;
		}
        List<Long> prOrgIds = new ArrayList<>();
        if (userInfo.getCurrentIdentity()!=null){
            Identity byId = identityDao.findById(userInfo.getCurrentIdentity());
            //如果身份不是默认的 就是管理员 直接拿token中的党组织id去查党组织表
            if (byId.getAcquiesce().equals(SysConst.IDENTITY_ACQUIESCE_NOT_DEFAULT)){
                Long userId = userInfo.getId();
                List<UserRelPbOrg> byUserId = userRelPbOrgDao.findByUserId(userId);
                if (byUserId!=null && byUserId.size() == 1){
                    Long pbOrgId = byUserId.get(0).getPborgId();
                    BaseVo b1 = pbServiceFeign.query(pbOrgId.toString());
                    if (b1.getData()!=null){
                        String ss = JSON.toJSONString(b1.getData());
                        OrganizationPb organizationPb = JSON.parseObject(ss,OrganizationPb.class);
                        userInfo.setPbOrgName(organizationPb.getName());
                    }
                }
            }
            //如果身份是默认的 就是普通党员 拿token中身份证去党员表里查询
            if (byId.getAcquiesce().equals(SysConst.IDENTITY_ACQUIESCE_DEFAULT)){
                BaseVo b1 = partyMemberServiceFeign.findByIdCard(userInfo.getIdcard());
                if (b1.getData()!=null) {
                    String s = JSON.toJSONString(b1.getData());
                    PartyMember partyMember = JSON.parseObject(s, PartyMember.class);
                    userInfo.setPbOrgName(partyMember.getOrgName());
                }
            }
//            BaseVo b1 = partyMemberServiceFeign.queryByIdCard(userInfo.getIdcard());
//            if (b1.getData()!=null) {
//                String s = JSON.toJSONString(b1.getData());
//                PartyMember partyMember = JSON.parseObject(s, PartyMember.class);
//                userInfo.setPbOrgName(partyMember.getOrgName());
//            }
        }else {
            //在查询党组织信息
            List<UserRelPbOrg> byUserId = userRelPbOrgDao.findByUserId(userInfo.getId());
            for (UserRelPbOrg userRelPbOrg : byUserId) {
                prOrgIds.add(userRelPbOrg.getPborgId());
            }
            List<PbOrganization> pbOrganizations = new ArrayList<>();
            //调用feign查询组织信息
            BaseVo b1 = pbServiceFeign.findOrganizationsByIds(prOrgIds);
            if (b1.getData()!=null){
                String ss = JSON.toJSONString(b1.getData());
                List<OrganizationPb> OrganizationPbs = JSONArray.parseArray(ss,OrganizationPb.class);
                for (OrganizationPb organizationPb : OrganizationPbs) {
                    PbOrganization pbOrganization = new PbOrganization();
                    BeanUtils.copyProperties(organizationPb,pbOrganization);
                    pbOrganization.setId(organizationPb.getId());
                    pbOrganization.setPcode(organizationPb.getPCode());
                    String idstr = RadixUtil.toFullBinaryString(Long.valueOf(organizationPb.getId()));
                    pbOrganization.setLevel(RadixUtil.getlevel(idstr));
                    pbOrganizations.add(pbOrganization);
                }
                if (CollectionUtils.isNotEmpty(pbOrganizations)){
                    userInfo.setPbOrgName(pbOrganizations.get(0).getName());
                }
            }
            result.put("pbOrganizations",pbOrganizations);
        }

    }

    private void wrapVolunteerInfo(UserInfo userInfo) {
        if (!userInfo.getAppIds().contains(ApplicationTypeEnum.APPLICATION_VOL.getApplicationId().intValue())) {
            return;
        }
        //根据tid判断接入端是web还是app 1-web 3-志愿者APP
        if (userInfo.getTid().equals(TerminalTypeEnum.TERMINALTOWEB.getTid())){
            //查出来用户所在志愿者服务站id
            //如果是管理员
            UserRelVolStation byUserId = userRelVolStationDao.findByUserId(userInfo.getId());
            if (byUserId!=null){
                VolunteerInfo info = new VolunteerInfo();
                info.setVolunteerId(byUserId.getUserId());
                info.setStationId(byUserId.getStationId());
                BaseVo b1 = stationServiceFeign.reflectById(byUserId.getStationId());
                String s = JSON.toJSONString(b1.getData());
                Station station = JSON.parseObject(s,Station.class);
                if (station==null){
                    throw new BusinessException("站点不存在");
                }
                info.setStationLevel(station.getStationLevel());
                info.setStationName(station.getStationName());
                userInfo.setVolunteerInfo(info);
            }
        }
        else if (userInfo.getTid().equals( TerminalTypeEnum.TERMINALTOVOLAPP.getTid())){

            //如果是普通用户
            VolunteerInfo info = volServiceFeign.getInfo(userInfo.getIdcard());
            if (info!=null){
                userInfo.setVolunteerInfo(info);
            }
        }
    }

    /**
     * 验证token
     * @param token
     * @param tid
     * @return
     */
    @Override
    public Boolean verifyToken(String token,Integer tid) {
        String ss = (String) cacheUtil.get("token:"+tid+":"+token);
        if (StringUtil.isNotEmpty(ss)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public BaseVo checkMobileUnique(UserVO userVO) {
        BaseVo baseVo = new BaseVo();
        Boolean flag;
        //更新校验
        if (userVO.getId() != null) {
            //原数据（旧数据）
            User byId = userDao.findById(userVO.getId());
            String mobile = byId.getMobile();
            if (mobile.equals(userVO.getMobile())) {
                flag = true;
            } else {
                int count = userDao.checkMobile(userVO);
                if (count > 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        } else {
            //保存校验
            int count = userDao.checkMobile(userVO);
            if (count > 0) {
                flag = false;
            } else {
                flag = true;
            }
        }
        baseVo.setData(flag);
        return baseVo;
    }

    @Override
    public BaseVo checkIdCardUnique(UserVO userVO) {
        BaseVo baseVo = new BaseVo();
        Boolean flag;
        //更新校验
        if (userVO.getId() != null) {
            //原数据（旧数据）
            User byId = userDao.findById(userVO.getId());
            String idcard = byId.getIdcard();
            if (idcard.equals(userVO.getIdcard())) {
                flag = true;
            } else {
                int count = userDao.checkIdCard(userVO);
                if (count > 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        } else {
            //保存校验
            int count = userDao.checkIdCard(userVO);
            if (count > 0) {
                flag = false;
            } else {
                flag = true;
            }
        }
        baseVo.setData(flag);
        return baseVo;
    }

    /**
     * 用户授权
     * @param appId
     * @return
     */
    @Override
    @Transactional
    public BaseVo setUserAuthorization(Long appId) {
        if(appId.equals(ApplicationTypeEnum.APPLICATION_VOL.getApplicationId().longValue())){
            //从token中获取数据
            UserInfo userInfo = getCurrentUserInfo();

            //获取默认身份
            Identity identity = identityService.getDefaultIdentityByAppId(appId);

            //插入身份用户关联信息
            UserRelIdentity userRelIdentity = new UserRelIdentity();
            userRelIdentity.setUserId(userInfo.getId());
            userRelIdentity.setIdentityId(identity.getId());

            //更新用户身份关联表
            userRelIdentityDao.add(userRelIdentity);

            //插入用户APP关联信息
            UserRelApp userRelApp = new UserRelApp();
            userRelApp.setUserId(userInfo.getId());
            userRelApp.setAppId(appId.intValue());

            //更新用户APP关联表
            userRelAppDao.add(userRelApp);

            //获取用户所有身份
            List<Identity> identitys = identityDao.findByUserId(userInfo.getId());

            //获取原来token
            String token = userInfo.getToken();
            String terminalId = userInfo.getTid().toString();

            //更新业务信息
            String user = (String) cacheUtil.get("token:"+terminalId+":"+token);
            UserInfo userInfoNew = JSON.parseObject(user, UserInfo.class);
            userInfoNew.setIdentitys(identitys);

            //判断原来appIds是否有新的APPID
            if(!userInfoNew.getAppIds().contains(appId)){
                userInfoNew.getAppIds().add(appId.intValue());
            }

            //更新新token
            String userJson = JSON.toJSONString(userInfoNew);
            cacheUtil.set("token:"+terminalId+":"+ token, userJson,Long.valueOf(atTokenExpire));

            BaseVo baseVo = new BaseVo();
            baseVo.setData(identitys);

            return baseVo;
        }else {
            throw new BusinessException("用户App错误");
        }

    }

    /**
     * 根据验证码修改用户密码
     * @param userVO
     * @return
     */
    @Override
    public BaseVo updatePwdByVCode(UserVO userVO) {

        //获取修改密码短信总数的key
        String backPwdVerCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_BACKPWD_VERIFICATION_CODE,userVO.getMobile(),userVO.getTerminalId().toString());

        //获取redis中验证码的正确性
        String code = (String)cacheUtil.get(backPwdVerCodeKey);

        //如果不为空说明验证码存在
        if(StringUtils.isNotEmpty(code)){
            //校验验证码是否正确
            if(code.equals(userVO.getVerificationCode())){
                //根据手机号修改密码
                //随机密码盐
                String salt = StringRandom.getStringRandom(SysConst.RANDOM_PASSWORD_SALT);
                //密码和盐=新密码  md5加密新密码
                String password = MD5Util.compute(userVO.getPassword() + salt);
                userVO.setPassword(password);
                userVO.setSalt(salt);
                userDao.updateUserForVolSql(userVO);

                BaseVo baseVo = new BaseVo();
                baseVo.setMsg("修改成功");
                return baseVo;
            }else {
                throw new BusinessException("验证码错误", SysConst.SMS_VERCODE_ERROR);
            }


        }else {
            //如果不存在直接返回
            throw new BusinessException("验证码已失效，请重新获取", SysConst.SMS_VERCODE_LOSE);
        }

    }

    /**
     * 志愿者用户注册
     * @param userVO 用户详情
     * @return
     */
    @Override
    @Transactional
    public BaseVo fastRegisterUser(UserVO userVO) {

      /*  //获取注册短信验证码存储的key
        String regisVerCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_REGISTER_VERIFICATION_CODE,userVO.getMobile(),userVO.getTerminalId().toString());

        //检测验证码是否存在
        String code = (String)cacheUtil.get(regisVerCodeKey);

        //验证码不存在直接返回，存在继续校验
        if(StringUtils.isNotEmpty(code)){
            //检测验证码是否正确
            if(code.equals(userVO.getVerificationCode())){
                User user = new User();
                //用户名默认用手机号代替
                user.setUsername("u"+userVO.getMobile());
                //手机号
                user.setMobile(userVO.getMobile());
                //用户真实姓名
                user.setRealName(userVO.getRealName());
                //用户身份证
                user.setIdcard(userVO.getIdcard());
                //随机密码盐
                String salt = StringRandom.getStringRandom(SysConst.RANDOM_PASSWORD_SALT);

                //密码和盐=新密码  md5加密新密码
                String password = MD5Util.compute(userVO.getPassword() + salt);
                user.setPassword(password);
                user.setSalt(salt);
                //创建时间
                user.setInsertTime(new Date());
                //账号默认状态
                user.setStatus(SysConst.USER_STATUS);
                //创建类型  2-APP注册
                user.setCreateType(SysConst.USER_REGISTER);
                user.setStatus(SysConst.USER_STATUS);

                //创建类型  2-APP注册
                user.setCreateType(SysConst.USER_REGISTER);

                //新增用户
                userDao.add(user);
                BaseVo baseVo = new BaseVo();
                baseVo.setMsg("用户注册成功");
                return baseVo;
            }else {
                throw new BusinessException("验证码错误", SysConst.SMS_VERCODE_ERROR);
            }
        }else {
            throw new BusinessException("验证码已失效，请重新获取", SysConst.SMS_VERCODE_LOSE);
        }*/
        BaseVo baseVo=new BaseVo();
        /*修改短信调用*/
        SmsCodeRequest smsCodeRequest = new SmsCodeRequest();
        smsCodeRequest.setMobileNumber(userVO.getMobile());
        smsCodeRequest.setTerminalId(Long.valueOf(userVO.getTerminalId()));
        smsCodeRequest.setSmsCode(userVO.getVerificationCode());
        BaseVo feignDto=smsFeignClient.checkCode(smsCodeRequest);
        if(feignDto.getCode()==ResultCode.SMS_CHECKED_NO_EFFECT.getCode()){
                return  new BaseVo(ResultCode.SMS_CHECKED_NO_EFFECT);
        }else if(feignDto.getCode()==ResultCode.SMS_CHECKED_FAIL.getCode()){
                 return  new BaseVo(ResultCode.SMS_CHECKED_FAIL);
        }else if(feignDto.getCode()==ResultCode.SMS_CHECKED_SUC.getCode()){
            User user = new User();
            //用户名默认用手机号代替
            user.setUsername("u"+userVO.getMobile());
            //手机号
            user.setMobile(userVO.getMobile());
            //用户真实姓名
            user.setRealName(userVO.getRealName());
            //用户身份证
            user.setIdcard(userVO.getIdcard());
            //随机密码盐
            String salt = StringRandom.getStringRandom(SysConst.RANDOM_PASSWORD_SALT);

            //密码和盐=新密码  md5加密新密码
            String password = MD5Util.compute(userVO.getPassword() + salt);
            user.setPassword(password);
            user.setSalt(salt);
            //创建时间
            user.setInsertTime(new Date());
            //账号默认状态
            user.setStatus(SysConst.USER_STATUS);
            //创建类型  2-APP注册
            user.setCreateType(SysConst.USER_REGISTER);
            user.setStatus(SysConst.USER_STATUS);

            //创建类型  2-APP注册
            user.setCreateType(SysConst.USER_REGISTER);

            //新增用户
            userDao.add(user);
            //BaseVo baseVo = new BaseVo();
            baseVo.setMsg("用户注册成功");
           // return baseVo;
        }

        return baseVo;
    }

    /**
     * 校验身份证和手机号是否存在
     * @param userVO
     * @return
     */
    @Override
    public BaseVo checkIdCardOrMobileUnique(UserVO userVO) {
        BaseVo baseVo = new BaseVo();
        Boolean flag = true;

        //先校验身份证 存在直接返回
        int countIdCard = userDao.checkIdCard(userVO);
        if (countIdCard > 0) {
            flag = false;
            baseVo.setData(flag);
            return baseVo;
        }

        //再校验手机号 存在直接返回
        int count = userDao.checkMobile(userVO);
        if (count > 0) {
            flag = false;
            baseVo.setData(flag);
            return baseVo;
        }

        baseVo.setData(flag);
        return baseVo;

    }

    /**
     * 获取政务所有用户列表
     * @return
     */
    @Override
    public BaseVo getOaDepartUsers() {
        BaseVo baseVo = new BaseVo();
        //获取appId为2的用户
        List<UserAndDepVO> users = userDao.findByAppId();
        //去重
        Set<Long> set = new HashSet<>();
        for (UserAndDepVO user : users) {
            set.add(user.getUserId());
        }
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(set);

        //根据去重后的id查询user
        List<User> userList = userDao.batchSelect(userIds);
        List<UserAndDepVO> userAndDeps = new ArrayList<>();
        for (User user : userList) {
            UserAndDepVO userAndDepVO = new UserAndDepVO();
            userAndDepVO.setUserId(user.getId());
            userAndDepVO.setUserName(user.getUsername());
            userAndDepVO.setRealName(user.getRealName());
            userAndDepVO.setMobile(user.getMobile());
            userAndDepVO.setEmail(user.getEmail());
            userAndDeps.add(userAndDepVO);
        }

        //根据userId查询部门
        for (UserAndDepVO userAndDep : userAndDeps) {
            List<Long> depIds = oaDepartmentDao.findByUserIds(userAndDep.getUserId());
            userAndDep.setDepIds(depIds);
        }

        baseVo.setData(userAndDeps);
        return baseVo;
    }
    /**
     * 根据身份证查询
     * @return
     */
    @Override
    public BaseVo queryUserByIdcard(String idcard) {
        User byIdCard = userDao.findByIdCard(idcard);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(byIdCard);
        return baseVo;
    }
    /**
     * 批量查询志愿者 根据身份证号
     * @param idlist
     * @return
     */
    @Override
    public List<Long> batchSelectByCardId(List<String> idlist) {
        List<Long> longList = userDao.batchSelectByCardId(idlist);
        return longList;
    }

    /**
     * 切换当前身份
     * @return
     */
    @Override
    public BaseVo switchIdentiy(Long identyId) {
        BaseVo baseVo = new BaseVo();
        //验证这个人有没有这个身份
        UserInfo userInfo = getCurrentUserInfo();
        List<Identity> identitys = identityDao.findByUserId(userInfo.getId());
        List<Long> identityIds = new ArrayList<>();
        for (Identity identity : identitys) {
            identityIds.add(identity.getId());
        }
        if (identityIds.contains(identyId)){
            Identity byId = identityDao.findById(identyId);
            //判断身份是党员还是管理员
            //根据默认标识判断如果是默认的就是党员身份 不是默认的就是管理员
            if (byId.getAcquiesce().equals(SysConst.IDENTITY_ACQUIESCE_DEFAULT)){
                String idcard = userInfo.getIdcard();
                BaseVo b1 = partyMemberServiceFeign.findByIdCard(idcard);
                if (b1.getData()!=null){
                    String s = JSON.toJSONString(b1.getData());
                    PartyMember partyMember = JSON.parseObject(s,PartyMember.class);
                    userInfo.setPbOrgId(partyMember.getOrgId());
                    userInfo.setCurrentIdentity(identyId);
                    BaseVo query = pbServiceFeign.query(partyMember.getOrgId());
                    if (query.getData()!=null){
                        String qs = JSON.toJSONString(query.getData());
                        OrganizationPb organizationPb = JSON.parseObject(qs,OrganizationPb.class);
                        userInfo.setPbOrgName(organizationPb.getName());
                    }
                }
            }
            if (byId.getAcquiesce().equals(SysConst.IDENTITY_ACQUIESCE_NOT_DEFAULT)){
                List<UserRelPbOrg> pbOrgs=userRelPbOrgDao.findByUserId(userInfo.getId());
                if (pbOrgs.size()>0){
                    userInfo.setPbOrgId(pbOrgs.get(0).getPborgId().toString());
                    userInfo.setCurrentIdentity(identyId);
                    BaseVo query = pbServiceFeign.query(pbOrgs.get(0).getPborgId().toString());
                    if (query.getData()!=null){
                        String qs = JSON.toJSONString(query.getData());
                        OrganizationPb organizationPb = JSON.parseObject(qs,OrganizationPb.class);
                        userInfo.setPbOrgName(organizationPb.getName());
                    }
                }
            }
            String userJson = JSON.toJSONString(userInfo);
            cacheUtil.set(SysConst.TOKEN_PREFIX+ userInfo.getTid()+":"+userInfo.getToken(), userJson,Long.valueOf(atTokenExpire));
            baseVo.setData(userInfo);
        }else {
            throw new BusinessException("没有此身份不能切换");
        }
        return baseVo;
    }


	/**
	 * 社区使用 根据手机号查询居委会账号
	 * @param mobile
	 * @return
	 */
    @Override
    public User getUserByMobile(String mobile) {
		User userByMobile = userDao.getUserByMobile(mobile);
		return userByMobile;
    }
	/**
	 * 社区使用 更新居委会账号密码
	 * @param user
	 * @return
	 */
    @Override
	@Transactional
    public Boolean updateOrgPwd(User user) {
    	UserVO userVO = new UserVO();
    	userVO.setId(user.getId());
    	userVO.setPassword(user.getPassword());
		int i = userDao.updateOrgPwd(userVO);
		if (i<=0){
			return false;
		}
		return true;
    }
    /**
     * 根据社区id查询社区管理员
     * @param orgId
     * @return
     */
    @Override
    public List<User> getUserByCboDep(Long orgId) {
        return userRelCboDepDao.getUserByCboDep(orgId);
    }
    /**
     * 编辑用户
     * @param userVO
     * @return
     */
    @Override
    @Transactional
    public BaseVo edit(UserVO userVO) {
        userDao.update(userVO);
        return successVo();
    }
	/**
	 * 配置
	 * @param userVO
	 * @return
	 */
	@Override
	public BaseVo config(UserVO userVO) {
		List<Integer> appIds = userVO.getAppIds();
		List<Long> pbOrgIds = userVO.getPbOrgIds();
		List<Integer> oaOrgIds = userVO.getOaOrgIds();
		List<Long> cboDepIds = userVO.getCboDepIds();
		List<Long> identityIds = userVO.getIdentityIds();

		Long userId = userVO.getId();
		if (userId==null){
			throwBusinessException("参数不全");
		}
		//step1 删除旧有的应用 身份 组织 关联关系
		//删除应用
		userRelAppDao.delByUserId(userId);
		//删除身份
		userRelIdentityDao.delByUserId(userId);
		//删除组织
		userRelPbOrgDao.delByUserId(userId);
		userRelOaDepDao.delByUserId(userId);
		userRelVolStationDao.delByUserId(userId);
		userRelCboDepDao.delByUserId(userId);

		//step2 批量新增新的应用 身份 组织 关联关系
		if (CollectionUtils.isNotEmpty(appIds)){
			//如果接入端code不为空，那么则必须选择组织和身份
			this.batchAddApp(userId,userVO.getAppIds());

			//志愿者
			if (userVO.getAppIds().contains(ApplicationTypeEnum.APPLICATION_VOL.getApplicationId())){
				this.addUserVolStation(userId,userVO.getStationId());
			}
			//社区
			if (userVO.getAppIds().contains(ApplicationTypeEnum.APPLICATION_CBO.getApplicationId())){
				//添加社区关系
				this.batchAddCbo(userId,cboDepIds);
			}
			//党建
			if (userVO.getAppIds().contains(ApplicationTypeEnum.APPLICATION_PB.getApplicationId())){
				this.batchAddPbOrg(userId,pbOrgIds);
			}
			//政务
			if (userVO.getAppIds().contains(ApplicationTypeEnum.APPLICATION_OA.getApplicationId())){
				this.batchAddOaOrg(userId,oaOrgIds);
			}
			//重建身份
			this.batchAddIdentitys(userId,identityIds);
		}
		return successVo();
	}

	/**
	 * 配置返显
	 * @return
	 */
	@Override
	public BaseVo configReflect(Long userId) {
		User user = new User();
		//身份关联信息
		List<Identity> identities=identityDao.findByUserId(userId);
		//党组织关联信息
        List<Long> prOrgIds = new ArrayList<>();
        //在查询党组织信息
        List<UserRelPbOrg> byUserId = userRelPbOrgDao.findByUserId(userId);
		List<PbOrganization> pbOrganizations = getPbOrganizations(prOrgIds, byUserId);

		//政务关联信息
        List<OaDepartment> oaDepartments=oaDepartmentDao.findByUserId(userId);
        user.setOaDepartments(oaDepartments);
		//应用关联信息
        List<UserRelApp> userRelApps=userRelAppDao.findByUserId(userId);
        List<Integer> integers = new ArrayList<>();
        for (UserRelApp userRelApp : userRelApps) {
            integers.add(userRelApp.getAppId());
        }
		//志愿者关联信息
		List<UserRelVolStation> userRelVolStationList = getUserRelVolStations(userId);
        //社区关联关系
		OaDepartment cboRel = userRelCboDepDao.findByUserId(userId);
		List<OaDepartment> oaDepartmentList = new ArrayList<>();
		if (cboRel!=null){
            oaDepartmentList.add(cboRel);
        }
        //设置各种关联信息
		user.setIdentities(identities);
		user.setPbOrganizations(pbOrganizations);
		user.setApps(integers);
		user.setUserRelVolStationList(userRelVolStationList);
		user.setCboDepartments(oaDepartmentList);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(user);
		return baseVo;
	}

	/**
	 * 根据用户id查询志愿者站点信息
	 * @param userId
	 * @return
	 */
	private List<UserRelVolStation> getUserRelVolStations(Long userId) {
		UserRelVolStation userRelVolStation = new UserRelVolStation();
		userRelVolStation.setUserId(userId);
		List<UserRelVolStation> userRelVolStationList = userRelVolStationDao.findAllByParam(userRelVolStation);
		for (UserRelVolStation rel : userRelVolStationList) {
			BaseVo b1 = stationServiceFeign.reflectById(rel.getStationId());
			if (b1.getData()!=null){
				String s = JSON.toJSONString(b1.getData());
				Station station = JSON.parseObject(s,Station.class);
				userRelVolStation.setStationName(station.getStationName());
			}
		}
		return userRelVolStationList;
	}
	/**
	 * 根据用户id查询党组织信息
	 * @param prOrgIds
	 * @return
	 */
	private List<PbOrganization> getPbOrganizations(List<Long> prOrgIds, List<UserRelPbOrg> byUserId) {
		for (UserRelPbOrg userRelPbOrg : byUserId) {
			prOrgIds.add(userRelPbOrg.getPborgId());
		}
		List<PbOrganization> pbOrganizations = new ArrayList<>();
		//调用feign查询组织信息
		BaseVo b11 = pbServiceFeign.findOrganizationsByIds(prOrgIds);
		if (b11.getData()!=null){
			String ss = JSON.toJSONString(b11.getData());
			List<OrganizationPb> OrganizationPbs = JSONArray.parseArray(ss,OrganizationPb.class);
			for (OrganizationPb organizationPb : OrganizationPbs) {
				PbOrganization pbOrganization = new PbOrganization();
				BeanUtils.copyProperties(organizationPb,pbOrganization);
				pbOrganization.setId(organizationPb.getId());
				pbOrganization.setPcode(organizationPb.getPCode());
				pbOrganizations.add(pbOrganization);
			}
		}
		if (CollectionUtils.isNotEmpty(pbOrganizations)){
			for (PbOrganization pbOrganization : pbOrganizations) {
				pbOrganization.setStrId(RadixUtil.toFullBinaryString(Long.valueOf(pbOrganization.getId())));
			}
		}
		return pbOrganizations;
	}
}
