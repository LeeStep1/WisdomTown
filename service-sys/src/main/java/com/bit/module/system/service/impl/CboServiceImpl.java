package com.bit.module.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.CboInfo;
import com.bit.base.dto.Community;
import com.bit.base.dto.OaOrganization;
import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.SysConst;
import com.bit.common.ResultCode;
import com.bit.common.consts.RedisKey;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.core.utils.CacheUtil;
import com.bit.module.system.bean.*;
import com.bit.module.system.dao.*;
import com.bit.module.system.feign.CboServiceFeign;
import com.bit.module.system.service.CboService;
import com.bit.module.system.service.UserService;
import com.bit.module.system.vo.ChangePassWordMobileVO;
import com.bit.module.system.vo.RefreshTokenVO;
import com.bit.module.system.vo.UserLoginVO;
import com.bit.module.system.vo.UserVO;
import com.bit.soft.sms.bean.SmsCodeRequest;
import com.bit.soft.sms.client.SmsFeignClient;
import com.bit.soft.sms.common.SmsAccountTemplateEnum;
import com.bit.utils.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zipkin2.Call;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/22 17:02
 **/
@Service("cboService")
public class CboServiceImpl extends BaseService implements CboService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRelCboDepDao userRelCboDepDao;
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private OaDepartmentDao oaDepartmentDao;
	@Autowired
	private IdentityDao identityDao;
	@Autowired
	private UserRelAppDao userRelAppDao;
	@Autowired
	private CboServiceFeign cboServiceFeign;
	@Autowired
	private UserService userService;

	@Value("${atToken.expire}")
	private String atTokenExpire;
	@Value("${rtToken.expire}")
	private String rtTokenExpire;
	@Autowired
	private SmsFeignClient smsFeignClient;


	/**
	 * 社区和 社区办的app登录
	 * @param userLoginVO
	 * @return
	 */
	@Override
	public BaseVo cboOrgAppLogin(UserLoginVO userLoginVO) {
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
		if (userLoginVO.getTerminalId() == null) {
			throw new BusinessException("接入端id不能为空");
		}
		//判断是不是居委会登录

		if (!userLoginVO.getTerminalId().equals(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid())) {
			throw new BusinessException("接入端非法");
		}
		//2. 根据用户名或身份证号或手机号查询user对象
		User user = userDao.findByUserLoginVO(userLoginVO);
		if (user != null){
			if (user.getStatus() == 1){
				String pw = MD5Util.compute(userLoginVO.getPassword() + user.getSalt());
				if (!pw.equals(user.getPassword())) {
					throw new BusinessException("密码错误");
				}
				//判断这个账户有没有社区的权限
				UserRelCboDep userRelCboDep = new UserRelCboDep();
				userRelCboDep.setUserId(user.getId());
				List<UserRelCboDep> byParam = userRelCboDepDao.findByParam(userRelCboDep);
				if (CollectionUtils.isEmpty(byParam)){
					throwBusinessException("该角色无社区关联关系");
				}

				UserInfo userInfo = new UserInfo();
				userLoginVO.setId(user.getId());
				userInfo.setIdcard(user.getIdcard());
				userInfo.setId(user.getId());
				userInfo.setMobile(user.getMobile());
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

				map.put("identitys",identitys);
				map.put("id",user.getId());
				map.put("username",user.getUsername());
				map.put("realName",user.getRealName());
				map.put("createType",user.getCreateType());
				map.put("userInfo",userInfo);
				userInfo.setAppIds(appIdList);

				//设置社区信息
				CboInfo cboInfo = getCboInfo(user.getId());
				userInfo.setCboInfo(cboInfo);
				map.put("cboInfo",cboInfo);

				//at token 失效时间为1天
				String userJson = JSON.toJSONString(userInfo);
				cacheUtil.set(SysConst.TOKEN_PREFIX+userLoginVO.getTerminalId()+":"+token, userJson,Long.valueOf(atTokenExpire));
				String rtJson = JSON.toJSONString(refreshTokenVO);
				cacheUtil.set(SysConst.REFRESHTOKEN_TOKEN_PREFIX + userLoginVO.getTerminalId() + ":" + rtToken, rtJson, Long.valueOf(rtTokenExpire));
				baseVo.setData(map);
			}else {
				throwBusinessException("该账号已经停用,请联系平台管理员");
			}
		}else {
			throwBusinessException("账号不存在,请联系平台管理员");
		}
		return baseVo;
	}

	@Override
	public BaseVo cboOrgResetPassword(CommonModel commonModel,HttpServletRequest request) {
		String mobile = commonModel.getMobile();
		String tid = request.getHeader("tid");


		if (!tid.equals(String.valueOf(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid()))){
			throwBusinessException("非法的接入端");
		}
		User user = checkUserExist(mobile);
		String smsCode = commonModel.getSmsCode();
		SmsCodeRequest smsCodeRequest=new SmsCodeRequest();
		smsCodeRequest.setMobileNumber(mobile);
		smsCodeRequest.setSmsCode(smsCode);
		smsCodeRequest.setTerminalId(Long.valueOf(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid()));
		smsCodeRequest.setSmsTemplateId(SmsAccountTemplateEnum.SMS_ACCOUNT_RESET_PASSWORD.getSmsTemplateType());
		BaseVo feignDto=smsFeignClient.checkCode(smsCodeRequest);
		if(feignDto.getCode()==ResultCode.SMS_CHECKED_FAIL.getCode()){
			throwBusinessException("验证码不正确");
		}else if((feignDto.getCode()==ResultCode.SMS_CHECKED_NO_EFFECT.getCode())){
			throwBusinessException("验证码失效");
		}
		/*String regisVerCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_BACKPWD_VERIFICATION_CODE,mobile, String.valueOf(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid()));
		String sms = (String) cacheUtil.get(regisVerCodeKey);
		if (StringUtil.isEmpty(sms)){
			throwBusinessException("验证码失效");
		}
		if (!smsCode.equals(sms)){
			throwBusinessException("验证码不正确");
		}*/
		String salt = user.getSalt();
		String encryptedPassword = MD5Util.compute(commonModel.getPassword() + salt);
		User obj = new User();
		obj.setId(user.getId());
		obj.setPassword(encryptedPassword);
		Boolean flag = userService.updateOrgPwd(obj);
		if (!flag){
			throwBusinessException("重置密码失败");
		}
		return successVo();
	}
	/**
	 * 社区app 校验手机号是否存在
	 * @param mobile
	 * @return
	 */
	@Override
	public BaseVo checkMobile(String mobile) {
		BaseVo baseVo = new BaseVo();
		User user = userService.getUserByMobile(mobile);
		if (user==null){
			return successVo();
		}else if (user.getStatus().equals(2)){
			baseVo.setData(null);
			baseVo.setMsg(ResultCode.USER_STATUS_STOP.getInfo());
			baseVo.setCode(ResultCode.USER_STATUS_STOP.getCode());
			return baseVo;
		}else {
			throwBusinessException("该账号已存在");
		}
		return null;
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
		//所有的社区集合
		List<OaDepartment> oaOrganizations = new ArrayList<>();

		OaOrganization oaOrganization = new OaOrganization();
		BeanUtils.copyProperties(byUserId,oaOrganization);
		cboInfo.setCurrentCboOrg(oaOrganization);
		//如果是社区办
		if (byUserId.getId().equals(Long.valueOf(SysConst.SHE_QU_BAN_ID))){

			oaOrganizations = oaDepartmentDao.findByIdLike(SysConst.SHE_QU_BAN_ID, null);
			if (CollectionUtils.isNotEmpty(oaOrganizations)){
				for (OaDepartment oaDepartment : oaOrganizations) {
					OaOrganization oa = new OaOrganization();
					BeanUtils.copyProperties(oaDepartment,oa);
					oaOrganizationList.add(oa);
					orgIds.add(oaDepartment.getId());
				}
			}
			cboInfo.setCboOrgs(oaOrganizationList);
			cboInfo.setUserType(SysConst.USER_TYPE_ORG_SHE_QU_BAN);
		}else {
			List<OaOrganization> cboOrgs = new ArrayList<>();
			cboOrgs.add(oaOrganization);
			orgIds.add(oaOrganization.getId());
			cboInfo.setCboOrgs(cboOrgs);
			cboInfo.setUserType(SysConst.USER_TYPE_ORG);
		}
		//调用feign 批量查询小区信息
		List<Community> communityList = cboServiceFeign.batchSelectByOrgIds(orgIds);
		if (CollectionUtils.isNotEmpty(communityList)){
			cboInfo.setCurrentCommunity(communityList.get(0));
		}
		//设置社区名称
		if (CollectionUtils.isNotEmpty(communityList)){
			if (CollectionUtils.isNotEmpty(oaOrganizationList)){
				for (Community community : communityList) {
					for (OaOrganization organization : oaOrganizationList) {
						if (community.getOrgId().equals(organization.getId())){
							community.setOrgName(organization.getName());
						}
					}
				}
			}
		}
		cboInfo.setCommunities(communityList);

		return cboInfo;
	}

	/**
	 * 校验居委会用户手机号是否存在
	 * @param mobile
	 * @return
	 */
	private User checkUserExist(String mobile){
		User user = userService.getUserByMobile(mobile);
		if (user==null){
			throwBusinessException("该账号不存在");
		}
		if (user.getStatus().equals(2)){
			throwBusinessException("该账号已经停用");
		}
		return user;
	}
	/**
	 * @description:
	 * @author liyujun
	 * @date 2019-09-03
	 * @param deps :
	 * @return : BaseVo
	 */
	@Override
	public BaseVo getUserIdsByDeps(List<Long> deps){
		BaseVo a=new BaseVo();
		a.setData(userRelCboDepDao.getUserByCboDeps(deps));
		return a;
	}


	/**
	 * 社区app 校验密码是否正确
	 * @param changePassWordMobileVO
	 * @return
	 */
	@Override
	public BaseVo checkPassword(ChangePassWordMobileVO changePassWordMobileVO) {
		Long userId = getCurrentUserInfo().getId();
		User byId = userDao.findById(userId);
		if (byId==null){
			throwBusinessException("该用户不存在");
		}
		if (StringUtil.isEmpty(changePassWordMobileVO.getOldPassWord())){
			throwBusinessException("密码不能为空");
		}
		String salt = byId.getSalt();
		//如果旧密码与数据库中密码不同
		if (!MD5Util.compute(changePassWordMobileVO.getOldPassWord() + salt).equals(byId.getPassword())){
			throwBusinessException("密码错误");
			//redis中记录错误次数
//			String key = SysConst.CBO_CHANGE_PASSWORD + SysConst.TID_CBO_ORG_APP + byId.getMobile();
//			Integer count = (Integer) cacheUtil.get(key);
//			if (count >= 5){
//				throwBusinessException("输入错误密码超过5次,请明天再试");
//			}else {
//				count = count + 1;
//				//持续时间
//				Long time = getRemainTime();
//				cacheUtil.set(key,count,time);
//				//提示密码错误 还可以在输入几次
//				throwBusinessException("密码错误!还可以在输入"+(5-count)+"次");
//			}
		}else {
			return successVo();
		}
		return null;
	}

	/**
	 * 社区app 更改密码
	 * @param changePassWordMobileVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo changePassword(ChangePassWordMobileVO changePassWordMobileVO) {
		Long userId = getCurrentUserInfo().getId();
		User byId = userDao.findById(userId);
		if (byId==null){
			throwBusinessException("该用户不存在");
		}
		if (StringUtil.isEmpty(changePassWordMobileVO.getOldPassWord()) || StringUtil.isEmpty(changePassWordMobileVO.getNewPassWord())){
			throwBusinessException("新密码或旧密码不能为空");
		}
		String salt = byId.getSalt();
		//如果旧密码与数据库中密码不同
		if (!MD5Util.compute(changePassWordMobileVO.getOldPassWord() + salt).equals(byId.getPassword())){
			throwBusinessException("旧密码错误");
			//redis中记录错误次数
//			String key = SysConst.CBO_CHANGE_PASSWORD + SysConst.TID_CBO_ORG_APP + byId.getMobile();
//			Integer count = (Integer) cacheUtil.get(key);
//			if (count >= 5){
//				throwBusinessException("输入错误密码超过5次,请明天再试");
//			}else {
//				count = count + 1;
//				//持续时间
//				Long time = getRemainTime();
//				cacheUtil.set(key,count,time);
//				//提示密码错误 还可以在输入几次
//				throwBusinessException("密码错误!还可以在输入"+(5-count)+"次");
//			}
		}


		//校验新密码是否合规
		String newpass = changePassWordMobileVO.getNewPassWord();
		String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
		if (!newpass.matches(passRegex)){
			throwBusinessException("新密码格式不符合");
		}

		//更新密码
		String encryptPass = MD5Util.compute(newpass + byId.getSalt());
		UserVO userVO = new UserVO();
		userVO.setId(userId);
		userVO.setPassword(encryptPass);
		userDao.update(userVO);
		//删除redis的key 强制退出

		String key = SysConst.TOKEN_PREFIX +String.valueOf(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid()) +":" + getCurrentUserInfo().getToken();
		cacheUtil.del(key);
		return successVo();
	}
	/**
	 * 社区app 更改手机号
	 * @param changePassWordMobileVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo changeMobile(ChangePassWordMobileVO changePassWordMobileVO) {
		Long userId = getCurrentUserInfo().getId();
		User byId = userDao.findById(userId);
		if (byId==null){
			throwBusinessException("该用户不存在");
		}
		User user = userService.getUserByMobile(changePassWordMobileVO.getMobile());
		if (user!=null){
			throwBusinessException("手机号已存在");
		}else {//todo 待改造
			//验证验证码
			/*String verCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_CBO_CHANGE_MOBILE_CODE,changePassWordMobileVO.getMobile(), String.valueOf(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid()));
			String sms = (String) cacheUtil.get(verCodeKey);
			if (StringUtil.isEmpty(sms)){
				throwBusinessException("验证码失效");
			}
			if (!changePassWordMobileVO.getSmsCode().equals(sms)){
				throwBusinessException("验证码不正确");
			}*/
			SmsCodeRequest smsCodeRequest=new SmsCodeRequest();
			smsCodeRequest.setTerminalId(Long.valueOf(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid()));
			smsCodeRequest.setSmsCode(changePassWordMobileVO.getSmsCode());
			smsCodeRequest.setMobileNumber(changePassWordMobileVO.getMobile());
			smsCodeRequest.setSmsTemplateId(SmsAccountTemplateEnum.CBO_SMS_OPERATION_CHANGE_MOBILE.getSmsTemplateType());
			BaseVo feignDto=smsFeignClient.checkCode(smsCodeRequest);
			if(feignDto.getCode()==ResultCode.SMS_CHECKED_FAIL.getCode()){
				throwBusinessException("验证码不正确");
			}else if((feignDto.getCode()==ResultCode.SMS_CHECKED_NO_EFFECT.getCode())){
				throwBusinessException("验证码失效");
			}

			UserVO userVO = new UserVO();
			userVO.setId(userId);
			userVO.setMobile(changePassWordMobileVO.getMobile());
			userDao.update(userVO);
			//删除redis的key 强制退出
			String key = SysConst.TOKEN_PREFIX +String.valueOf(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid()) +":" + getCurrentUserInfo().getToken();
			cacheUtil.del(key);
		}
		return successVo();
	}

	/**
	 * 获取当天剩余时间
	 * @return
	 */
	private Long getRemainTime(){
		//当前时间
		Date now = new Date();
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		final long endTimeThisDay = calendar.getTimeInMillis();
		Long gap = (endTimeThisDay-now.getTime())/1000;
		return gap;
	}
}
