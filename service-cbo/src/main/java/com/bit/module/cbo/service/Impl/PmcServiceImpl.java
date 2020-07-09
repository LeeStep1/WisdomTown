package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.CboInfo;
import com.bit.base.dto.OaOrganization;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.common.consts.RedisKey;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.common.enumerate.LoginTypeEnum;
import com.bit.common.enumerate.PmcStaffStatusEnum;
import com.bit.core.utils.CacheUtil;
import com.bit.module.cbo.bean.Community;
import com.bit.module.cbo.bean.PmcCompany;
import com.bit.module.cbo.bean.PmcStaff;
import com.bit.module.cbo.bean.PmcStaffRelCommunity;
import com.bit.module.cbo.dao.CommunityDao;
import com.bit.module.cbo.dao.PmcCompanyDao;
import com.bit.module.cbo.dao.PmcStaffDao;
import com.bit.module.cbo.dao.PmcStaffRelCommunityDao;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.vo.ChangePassWordMobileVO;
import com.bit.module.cbo.vo.CommonVO;
import com.bit.module.cbo.service.AppCommonService;
import com.bit.module.cbo.service.PmcService;
import com.bit.module.cbo.vo.PmcStaffVO;
import com.bit.module.cbo.vo.RefreshTokenVO;
import com.bit.soft.sms.bean.SmsCodeRequest;
import com.bit.soft.sms.client.SmsFeignClient;
import com.bit.soft.sms.common.SmsAccountTemplateEnum;
import com.bit.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/17 16:47
 **/
@Service("pmcService")
public class PmcServiceImpl extends BaseService implements PmcService {

	@Value("${atToken.expire}")
	private String atTokenExpire;
	@Value("${rtToken.expire}")
	private String rtTokenExpire;
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private PmcStaffDao pmcStaffDao;
	@Autowired
	private PmcStaffRelCommunityDao pmcStaffRelCommunityDao;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private SysServiceFeign sysServiceFeign;
	@Autowired
	private AppCommonService appCommonService;
	@Autowired
	private PmcCompanyDao pmcCompanyDao;
	@Autowired
	private TokenUtil tokenUtil;
	@Autowired
	private SmsFeignClient smsFeignClient;


	@Override
	public BaseVo appPmcLogin(PmcStaffVO pmcStaffModelVO) {
		//step 1: 校验参数是否合规
		Integer loginType = pmcStaffModelVO.getLoginType();
		String mobile = pmcStaffModelVO.getMobile();
		String password = pmcStaffModelVO.getPassword();
		String smsCode = pmcStaffModelVO.getSmsCode();
		Integer terminalId = pmcStaffModelVO.getTerminalId();
		if (!terminalId.equals(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid())){
			throwBusinessException("接入端非法");
		}
		if (StringUtil.isEmpty(mobile)){
			throwBusinessException("手机号不能为空");
		}
		if (loginType==null){
			throwBusinessException("登录方式未确认");
		}
		//如果是验证码登录
		if (loginType.equals(LoginTypeEnum.LOGIN_TYPE_MOBILE_SMS_CODE.getCode())){
			if (StringUtil.isEmpty(smsCode)){
				throwBusinessException("验证码不能为空");
			}
		}
		//如果是密码登录
		if (loginType.equals(LoginTypeEnum.LOGIN_TYPE_MOBILE_PASSWORD.getCode())){
			if (StringUtil.isEmpty(password)){
				throwBusinessException("密码不能为空");
			}
		}
		//step2 校验手机号是否存在
		Boolean flag = checkPmcStaffExist(mobile);
		if (!flag){
			throwBusinessException("账号不存在,请联系管理员");
		}
		//step3 校验物业人员记录是否有密码
		PmcStaff pmcStaffByMobile = pmcStaffDao.getPmcStaffByMobile(mobile);
		if (pmcStaffByMobile.getStatus().equals(PmcStaffStatusEnum.PMC_STAFF_STATUS_INACTIVE.getCode())){
			throwBusinessException("账号已停用,请联系管理员");
		}
		//登录前踢掉原来的token
		if (StringUtil.isNotEmpty(pmcStaffByMobile.getToken())){
			tokenUtil.delToken(pmcStaffByMobile.getToken());
		}
		Map map = pmclogin(loginType,mobile,smsCode,password,pmcStaffByMobile,terminalId);
		BaseVo baseVo = new BaseVo();
		if (StringUtil.isEmpty(pmcStaffByMobile.getPassword())){
			//首次登录使用验证码方式 登录后要设置登录密码 选择小区
			baseVo.setMsg(ResultCode.PASSWORD_IS_NULL.getInfo());
			baseVo.setCode(ResultCode.PASSWORD_IS_NULL.getCode());
		}
		baseVo.setData(map);
		return baseVo;
	}
	/**
	 * 重置密码
	 * @param commonVO
	 * @return
	 */
	@Override
	public BaseVo resetPassword(CommonVO commonVO, HttpServletRequest request) {
		return appCommonService.resetPassword(commonVO,request);
	}
	/**
	 * 初次更新物业密码
	 * @param commonVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo updatePmcPassword(CommonVO commonVO) {
		Long id = commonVO.getId();
		PmcStaff pmcStaffById = pmcStaffDao.getPmcStaffById(id);
		if (pmcStaffById==null){
			throwBusinessException("物业人员信息不存在");
		}
		String password = commonVO.getPassword();
		String npass = MD5Util.compute(password + pmcStaffById.getSalt());

		PmcStaff obj = new PmcStaff();
		obj.setId(id);
		obj.setPassword(npass);
		int i = pmcStaffDao.updatePmcStaff(obj);
		if (i<=0){
			throwBusinessException("更新物业人员密码失败");
		}
		return successVo();
	}
	/**
	 * 物业人员校验手机号
	 * @param mobile
	 * @return
	 */
	@Override
	public BaseVo checkMobile(String mobile) {
		PmcStaff pmcStaffByMobile = pmcStaffDao.getPmcStaffByMobile(mobile);
		if (pmcStaffByMobile==null){
			return successVo();
		}else {
			if (pmcStaffByMobile.getStatus().equals(PmcStaffStatusEnum.PMC_STAFF_STATUS_INACTIVE.getCode())){
				BaseVo baseVo = new BaseVo();
				baseVo.setCode(ResultCode.USER_STATUS_STOP.getCode());
				baseVo.setMsg(ResultCode.USER_STATUS_STOP.getInfo());
				baseVo.setData(null);
				return baseVo;
			}else {
				throwBusinessException("该账号已存在");
			}
		}
//		int i = pmcStaffDao.checkPmcStaffExist(mobile);
//		if (i>0){
//			throwBusinessException("该账号已存在");
//		}else {
//			return successVo();
//		}
		return null;
	}

	@Override
	public BaseVo checkPassword(ChangePassWordMobileVO changePassWordMobileVO) {
		Long pmcId = getCurrentUserInfo().getId();
		PmcStaff pmcStaffById = pmcStaffDao.getPmcStaffById(pmcId);
		if (pmcStaffById==null){
			throwBusinessException("该用户不存在");
		}
		if (StringUtil.isEmpty(changePassWordMobileVO.getOldPassWord())){
			throwBusinessException("密码不能为空");
		}
		String salt = pmcStaffById.getSalt();
		//如果旧密码与数据库中密码不同
		if (!MD5Util.compute(changePassWordMobileVO.getOldPassWord() + salt).equals(pmcStaffById.getPassword())){
			throwBusinessException("密码错误");
			//redis中记录错误次数
//			String key = Const.CBO_CHANGE_PASSWORD + Const.TID_CBO_PMC_APP + pmcStaffById.getMobile();
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
	 * 物业app 更改密码
	 * @param changePassWordMobileVO
	 * @return
	 */
	@Override
	public BaseVo changePassword(ChangePassWordMobileVO changePassWordMobileVO) {
		return appCommonService.changePassWord(changePassWordMobileVO);
	}
	/**
	 * 物业app 更改手机号
	 * @param changePassWordMobileVO
	 * @return
	 */
	@Override
	public BaseVo changeMobile(ChangePassWordMobileVO changePassWordMobileVO) {
		return appCommonService.changeMobile(changePassWordMobileVO);
	}
	/**
	 * 物业app 删除物业员工
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo delPmcStaff(Long id) {
		PmcStaff pmcStaffById = pmcStaffDao.getPmcStaffById(id);
		if (pmcStaffById==null){
			throwBusinessException("该员工不存在");
		}

		//删除员工
		int i = pmcStaffDao.delById(id);
		if (i<=0){
			throwBusinessException("删除物业员工失败");
		}
		//删除token
		tokenUtil.delToken(pmcStaffById.getToken());
		//删除员工与社区的关系
		pmcStaffRelCommunityDao.deleteByPmcStaffId(id);
		return successVo();
	}

	/**
	 * 校验物业工作人员手机号是否存在
	 * @param mobile
	 * @return
	 */
	private boolean checkPmcStaffExist(String mobile){
		int i = pmcStaffDao.checkPmcStaffExist(mobile);
		if (i>0){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 物业人员登录
	 * @param loginType
	 * @param mobile
	 * @param smsCode
	 * @param password
	 * @param pmcStaff
	 * @param terminalId
	 * @return
	 */
	private Map pmclogin(Integer loginType,String mobile,String smsCode,String password,PmcStaff pmcStaff,Integer terminalId){
		//如果是验证码登录
		if (loginType.equals(LoginTypeEnum.LOGIN_TYPE_MOBILE_SMS_CODE.getCode())){
			/*String loginCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_CBO_LOGIN_CODE,mobile, String.valueOf(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid()));
			String sms = (String) cacheUtil.get(loginCodeKey);
			if (StringUtil.isEmpty(sms)){
				throwBusinessException("验证码失效,请重新获取");
			}
			if (!smsCode.equals(sms)){
				throwBusinessException("验证码错误");
			}*/
			/*修改后*/
			checkSmsCode(mobile,TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid(), SmsAccountTemplateEnum.SMS_ACCOUNT_LOGIN.getSmsTemplateType(),smsCode);
		}
		//如果是密码登录
		if (loginType.equals(LoginTypeEnum.LOGIN_TYPE_MOBILE_PASSWORD.getCode())){
			String salt = pmcStaff.getSalt();
			String encrptedPassword = MD5Util.compute(password + salt);
			if (!encrptedPassword.equals(pmcStaff.getPassword())){
				throwBusinessException("密码错误");
			}
		}

		//小区id的集合
		List<Long> communityIds = new ArrayList<>();
		//社区id的集合
		List<Long> orgIds = new ArrayList<>();
		Map map = new HashMap<>();
		String tokenStr = "";
		//设置token
		CboInfo cboInfo = new CboInfo();
		cboInfo.setIdentity(2);

		//确定物业人员身份
		cboInfo.setIdentity(pmcStaff.getRole());

		PmcStaffRelCommunity rel = new PmcStaffRelCommunity();
		rel.setStaffId(pmcStaff.getId());
		List<PmcStaffRelCommunity> byParam = pmcStaffRelCommunityDao.findByParam(rel);
		if (CollectionUtils.isNotEmpty(byParam)){
			cboInfo.setUserType(Const.USER_TYPE_PMC);
			for (PmcStaffRelCommunity pmcStaffRelCommunity : byParam) {
				communityIds.add(pmcStaffRelCommunity.getCommunityId());
				orgIds.add(pmcStaffRelCommunity.getOrgId());
			}
			//调用feign 批量查询社区信息
			List<OaOrganization> oaOrganizations = sysServiceFeign.batchSelectOaDepartmentByIds(orgIds);
			if (CollectionUtils.isNotEmpty(oaOrganizations)){
				//设置自己所有的社区的实体
				cboInfo.setCboOrgs(oaOrganizations);
			}
			List<com.bit.base.dto.Community> communityList = new ArrayList<>();
			//批量查询小区
			List<Community> communities = communityDao.batchSelectByIds(communityIds);
			if (CollectionUtils.isNotEmpty(communities)){
				for (Community comm : communities) {
					com.bit.base.dto.Community community = new com.bit.base.dto.Community();
					BeanUtils.copyProperties(comm,community);
					communityList.add(community);
				}
				//设置社区名称
				if (CollectionUtils.isNotEmpty(oaOrganizations)){
					for (com.bit.base.dto.Community community : communityList) {
						for (OaOrganization oaOrganization : oaOrganizations) {
							if (oaOrganization.getId().equals(community.getOrgId())){
								community.setOrgName(oaOrganization.getName());
							}
						}

					}
				}
				//设置自己所有的小区的实体
				cboInfo.setCommunities(communityList);
			}
		}else {
			throwBusinessException("没有关联小区不能登录");
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setCboInfo(cboInfo);
		String token = UUIDUtil.getUUID();
		userInfo.setToken(token);
		userInfo.setTid(terminalId);
		userInfo.setId(pmcStaff.getId());
		userInfo.setMobile(pmcStaff.getMobile());
		userInfo.setRealName(pmcStaff.getName());
		userInfo.setMobile(pmcStaff.getMobile());

		String rtToken = UUIDUtil.getUUID();
		RefreshTokenVO refreshTokenVO = new RefreshTokenVO();
		refreshTokenVO.setUserInfo(userInfo);
		refreshTokenVO.setAtKey(Const.TOKEN_PREFIX + terminalId +":"+token);

		String userJson = JSON.toJSONString(userInfo);
		tokenStr = Const.TOKEN_PREFIX + terminalId +":"+token;
		cacheUtil.set(Const.TOKEN_PREFIX + terminalId +":"+token, userJson,Long.valueOf(atTokenExpire));
		String rtJson = JSON.toJSONString(refreshTokenVO);
		cacheUtil.set(Const.REFRESHTOKEN_TOKEN_PREFIX + terminalId + ":" + rtToken, rtJson, Long.valueOf(rtTokenExpire));


		map.put("token", token);
		map.put("refreshToken", rtToken);
		map.put("id",pmcStaff.getId());
		map.put("mobile",pmcStaff.getMobile());
		map.put("realName",pmcStaff.getName());
		map.put("userInfo",userInfo);
		//设置物业人员的物业公司id和名称

		map.put("pmcCompanyId",pmcStaff.getCompanyId());
		PmcCompany pmcCompanyById = pmcCompanyDao.getPmcCompanyById(pmcStaff.getCompanyId());
		if (pmcCompanyById!=null){
			map.put("pmcCompanyName",pmcCompanyById.getCompanyName());
		}

		//更新物业人员信息表中的token
		PmcStaff obj = new PmcStaff();
		obj.setId(pmcStaff.getId());
		obj.setToken(tokenStr);
		int i = pmcStaffDao.updatePmcStaff(obj);
		if (i<=0){
			throwBusinessException("token设置失败");
		}
		return map;
	}


	/**
	 * @param mobile        :
	 * @param tid           :
	 * @param smsTemplateId :
	 * @param smsCode       :
	 * @return : void
	 * @description:
	 * @author liyujun
	 * @date 2020-06-10
	 */
	private void checkSmsCode(String mobile, long tid, int smsTemplateId, String smsCode) {
		SmsCodeRequest smsCodeRequest = new SmsCodeRequest();
		smsCodeRequest.setMobileNumber(mobile);
		smsCodeRequest.setSmsCode(smsCode);
		smsCodeRequest.setTerminalId(tid);
		smsCodeRequest.setSmsTemplateId(smsTemplateId);
		BaseVo feignDto = smsFeignClient.checkCode(smsCodeRequest);
		if (feignDto.getCode() == ResultCode.SMS_CHECKED_FAIL.getCode()) {
			throwBusinessException("验证码不正确");
		} else if ((feignDto.getCode() == ResultCode.SMS_CHECKED_NO_EFFECT.getCode())) {
			throwBusinessException("验证码失效");
		}
	}
}
