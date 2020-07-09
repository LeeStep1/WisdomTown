package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bit.base.dto.CboInfo;
import com.bit.base.dto.OaOrganization;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.common.consts.RedisKey;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.common.enumerate.*;
import com.bit.core.utils.CacheUtil;
import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.dao.*;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.service.AppCommonService;
import com.bit.module.cbo.service.LocationApplyService;
import com.bit.module.cbo.service.ResidentService;
import com.bit.module.cbo.vo.*;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.soft.sms.bean.SmsCodeRequest;
import com.bit.soft.sms.client.SmsFeignClient;
import com.bit.soft.sms.common.SmsAccountTemplateEnum;
import com.bit.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static com.bit.common.Const.USER_TYPE_ORG_SHE_QU_BAN;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/16 15:49
 **/
@Service("residentService")
public class ResidentServiceImpl extends BaseService implements ResidentService {
	@Value("${atToken.expire}")
	private String atTokenExpire;
	@Value("${rtToken.expire}")
	private String rtTokenExpire;

	@Autowired
	private ResidentDao residentDao;
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private ResidentRelLocationDao residentRelLocationDao;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private SysServiceFeign sysServiceFeign;
	@Autowired
	private AppCommonService appCommonService;
	@Autowired
	private LocationApplyDao locationApplyDao;
	@Autowired
	private LocationDao locationDao;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private ResidentExtendDao residentExtendDao;
	@Autowired
	private FileServiceFeign fileServiceFeign;
	@Autowired
	private TokenUtil tokenUtil;
    @Autowired
    private SendMqPushUtil sendMqPushUtil;
	@Autowired
	private SmsFeignClient smsFeignClient;

	/**
	 * 房屋认证相关service
	 */
	@Autowired
    private LocationApplyService locationApplyService;
	/**
	 * app端居民登录
	 * @param residentVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo appResidentLogin(ResidentVO residentVO) {
		//step 1: 校验参数是否合规
		Integer loginType = residentVO.getLoginType();
		String mobile = residentVO.getMobile();
		String password = residentVO.getPassword();
		String smsCode = residentVO.getSmsCode();
		Integer terminalId = residentVO.getTerminalId();
		if (!terminalId.equals(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid())){

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
		Boolean flag = checkResidentExist(mobile);
		if (!flag){
			throwBusinessException("账号不存在,请先注册");
		}
		//step3 校验居民记录是否有密码
		Resident residentByMobile = residentDao.getResidentByMobile(mobile);
		if (residentByMobile.getStatus().equals(ResidentStatusEnum.RESIDENT_STATUS_STOP.getCode())){
			throwBusinessException("账号已停用");
		}
		//登录前踢掉原来的token
		if (StringUtil.isNotEmpty(residentByMobile.getToken())){
			tokenUtil.delToken(residentByMobile.getToken());
		}
		BaseVo baseVo = new BaseVo();
		Map map = residentlogin(loginType,mobile,smsCode,password,residentByMobile,terminalId);
		if (StringUtil.isEmpty(residentByMobile.getPassword())){
			//首次登录使用验证码方式 登录后要设置登录密码 选择小区
			baseVo.setMsg(ResultCode.PASSWORD_IS_NULL.getInfo());
			baseVo.setCode(ResultCode.PASSWORD_IS_NULL.getCode());
		}
		baseVo.setData(map);
		return baseVo;
	}


	/**
	 * 校验手机号是否存在
	 * @param mobile
	 * @return
	 */
	private boolean checkResidentExist(String mobile){
		//查询居民表中这个手机号是否存在
		int i = residentDao.checkResidentExist(mobile);
		if (i>0){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 居民登录
	 * @param loginType
	 * @param mobile
	 * @param smsCode
	 * @param password
	 * @param resident
	 */
	private Map residentlogin(Integer loginType,String mobile,String smsCode,String password,Resident resident,Integer terminalId){
		//如果是验证码登录
		if (loginType.equals(LoginTypeEnum.LOGIN_TYPE_MOBILE_SMS_CODE.getCode())){
			/*String loginCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_CBO_LOGIN_CODE,mobile, String.valueOf(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid()));
			String sms = (String) cacheUtil.get(loginCodeKey);
			if (StringUtil.isEmpty(sms)){
				throwBusinessException("验证码失效,请重新获取");
			}
			if (!smsCode.equals(sms)){
				throwBusinessException("验证码错误");
			}*/
			checkSmsCode(mobile,TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid(), SmsAccountTemplateEnum.SMS_ACCOUNT_LOGIN.getSmsTemplateType(),smsCode);
		}
		//如果是密码登录
		if (loginType.equals(LoginTypeEnum.LOGIN_TYPE_MOBILE_PASSWORD.getCode())){
			String salt = resident.getSalt();
			String encrptedPassword = MD5Util.compute(password + salt);
			if (!encrptedPassword.equals(resident.getPassword())){
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
		cboInfo.setIdentity(resident.getRole());
		//查询所属小区的实体 集合
		ResidentRelLocation rel = new ResidentRelLocation();
		rel.setResidentId(resident.getId());

		List<ResidentRelLocation> byParam = residentRelLocationDao.findByParam(rel);
		if (CollectionUtils.isNotEmpty(byParam)){
			for (ResidentRelLocation residentRelLocation : byParam) {
				communityIds.add(residentRelLocation.getCommunityId());
			}
			//批量查询小区
			List<Community> communities = communityDao.batchSelectByIds(communityIds);
			List<com.bit.base.dto.Community> communityList = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(communities)){
				for (Community comm : communities) {
					com.bit.base.dto.Community community = new com.bit.base.dto.Community();
					BeanUtils.copyProperties(comm,community);
					communityList.add(community);
					orgIds.add(comm.getOrgId());
				}
			}
			if (CollectionUtils.isNotEmpty(communityList)){
				//设置当前小区 取列表中 第一个
				com.bit.base.dto.Community currenyCommunity = communityList.get(0);
				cboInfo.setCurrentCommunity(currenyCommunity);
				//批量查询社区
				List<OaOrganization> oaOrganizations = sysServiceFeign.batchSelectOaDepartmentByIds(orgIds);

				if(CollectionUtils.isNotEmpty(oaOrganizations)){
					//转成map
					Map<Long, OaOrganization> oaOrganizationMap = oaOrganizations.stream().collect(Collectors.toMap(OaOrganization::getId, oaOrganization -> oaOrganization));

					//设置自己所有的社区的实体
					cboInfo.setCboOrgs(oaOrganizations);

					OaOrganization oa = oaOrganizationMap.get(currenyCommunity.getOrgId());
					if(oa !=null){
						cboInfo.setCurrentCboOrg(oa);
					}

					for (com.bit.base.dto.Community community : communityList) {
						OaOrganization oaTemp = oaOrganizationMap.get(community.getOrgId());
						if(oa != null){
							community.setOrgName(oaTemp.getName());
						}
					}
				}
			}

			//设置自己所有的小区的实体
			cboInfo.setCommunities(communityList);
			cboInfo.setUserType(Const.USER_TYPE_RESIDENT);
		}else {

			cboInfo.setIdentity(Const.IDENTITY_RESIDENT_TOURIST);
		}

		UserInfo userInfo = new UserInfo();
		userInfo.setCboInfo(cboInfo);
		String token = UUIDUtil.getUUID();
		userInfo.setToken(token);
		userInfo.setId(resident.getId());
		userInfo.setIdcard(resident.getCardNum());
		userInfo.setRealName(resident.getRealName());
		userInfo.setTid(terminalId);
		userInfo.setMobile(resident.getMobile());

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
		map.put("id",resident.getId());
		map.put("mobile",resident.getMobile());
		map.put("realName",resident.getRealName());
		map.put("userInfo",userInfo);
		//更新居民表中的token
		Resident obj = new Resident();
		obj.setId(resident.getId());
		obj.setToken(tokenStr);
		int i = residentDao.updateResident(obj);
		if (i<=0){
			throwBusinessException("token设置失败");
		}
		return map;
	}

	/**
	 * app端居民注册
	 * @param residentVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo appRegister(ResidentVO residentVO) {
		Long userId = getCurrentUserInfo().getId();
		String mobile = residentVO.getMobile();

		if (StringUtil.isEmpty(mobile)){
			throwBusinessException("手机号为空");
		}
		boolean flag = checkResidentExist(mobile);
		if (flag){
			throwBusinessException("账号已存在,请直接登录");
		}
		/*String smsCode = residentVO.getSmsCode();
		String regisVerCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_REGISTER_VERIFICATION_CODE,mobile,  String.valueOf(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid()));
		String sms = (String) cacheUtil.get(regisVerCodeKey);
		if (StringUtil.isEmpty(sms)){
			throwBusinessException("验证码失效,请重新获取");
		}
		if (!smsCode.equals(sms)){
			throwBusinessException("验证码不正确");
		}*/
		checkSmsCode(mobile,TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid(), SmsAccountTemplateEnum.SMS_ACCOUNT_REGISTER.getSmsTemplateType(),residentVO.getSmsCode());
		//生成盐和密码
		String password = residentVO.getPassword();
		if (StringUtil.isEmpty(password)){
			throwBusinessException("密码不能为空");
		}
		//随机密码盐
		String salt = StringRandom.getStringRandom(Const.RANDOM_PASSWORD_SALT);
		//密码和盐=新密码  md5加密新密码
		String encryptedPassword = MD5Util.compute(password + salt);
		residentVO.setPassword(encryptedPassword);
		residentVO.setSalt(salt);
		residentVO.setRole(ResidentRoleTypeEnum.RESIDENT_ROLE_TYPE_TOURIST.getCode());
		residentVO.setStatus(ResidentStatusEnum.RESIDENT_STATUS_NEED_FILL.getCode());
		residentVO.setCreateType(ResidentCreateTypeEnum.RESIDENT_CREATE_TYPE_APP_ENUM.getCode());
		residentVO.setCredentialsPhotoIds("");

		Resident resident = new Resident();
		BeanUtils.copyProperties(residentVO,resident);
		resident.setCreateTime(new Date());
		resident.setCreateUserId(userId);
		int i = residentDao.insertResident(resident);
		if (i<=0){
			throwBusinessException("居民注册失败");
		}
		return successVo();
	}
	/**
	 * 居民app重置密码
	 * @param commonVO
	 * @return
	 */
	@Override
	public BaseVo resetPassword(CommonVO commonVO, HttpServletRequest request) {
		return appCommonService.resetPassword(commonVO,request);
	}
	/**
	 * app端房屋验证
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo appLocationApply(LocationApplyVO locationApplyModelVO) {
		UserInfo userInfo = getCurrentUserInfo();
		Long residentId = userInfo.getId();
		Resident residentById = residentDao.getResidentById(residentId);
		if (residentById==null){
			throwBusinessException("该居民不存在");
		}
		//先查询审核验证表中有没有这个居民的申请信息
		List<LocationApply> recordByResidentId = locationApplyDao.getRecordByResidentId(residentId);
		//如果是第一次申请 校验身份证是否重复
		if (CollectionUtils.isEmpty(recordByResidentId)){
			//验证身份证是否存在
			ResidentVO test = new ResidentVO();
			test.setCardNum(locationApplyModelVO.getCardNum());
			test.setCardType(locationApplyModelVO.getCardType());
			Resident residentByCardNumAndCardType = residentDao.getResidentByCardNumAndCardType(test);
			if (residentByCardNumAndCardType!=null){
				throwBusinessException("证件号码已存在");
			}
		}

		//验证提交的认证信息是否存在该居民信息的住房信息
		ResidentRelLocation residentRelLocation = new ResidentRelLocation();
		residentRelLocation.setAddressId(locationApplyModelVO.getAddressId());
		residentRelLocation.setResidentId(residentId);
		residentRelLocation.setCommunityId(locationApplyModelVO.getCommunityId());
		List<ResidentRelLocation> byParam = residentRelLocationDao.findByParam(residentRelLocation);
		if (CollectionUtils.isNotEmpty(byParam)){
			throwBusinessException("房屋信息已认证");
		}
		//验证提交的住房信息是否已存在状态为‘认证中’的认证审核表中，存在则提示“认证申请正在审核，请不要重复提交！”；
		LocationApplyVO apply = new LocationApplyVO();
		apply.setResidentId(residentId);
		apply.setAddressId(locationApplyModelVO.getAddressId());
		apply.setCommunityId(locationApplyModelVO.getCommunityId());
		apply.setOrgId(locationApplyModelVO.getOrgId());
		apply.setApplyStatus(ResidentApplyWebStatusEnum.APPLY_STATUS_NOT_VERIFY.getCode());

		List<LocationApply> byParam1 = locationApplyDao.findByParam(apply);
		if (CollectionUtils.isNotEmpty(byParam1)){
			throwBusinessException("认证申请正在审核，请不要重复提交");
		}
		//判断这个居民在这个社区是不是第一次房屋验证 先查询 防止脏读
		List<LocationApply> byParam2 = locationApplyDao.getRecordByResidentId(residentId);

		Community communityById = communityDao.getCommunityById(locationApplyModelVO.getCommunityId());
		//查询社区id
		Long orgId = communityById.getOrgId();
//		String orgName = userInfo.getCboInfo().getCurrentCboOrg().getName();
		//查询社区名称
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
		String orgName = commonUtil.getOrgName(orgId,oaOrganizationList);

		LocationApply locationApply = new LocationApply();
		locationApply.setResidentId(residentId);
		locationApply.setCommunityId(locationApplyModelVO.getCommunityId());
		locationApply.setCommunityName(communityById.getCommunityName());
		locationApply.setOrgId(orgId);
		locationApply.setOrgName(orgName);
		if (locationApplyModelVO.getAddressId()!=null){
			//获得addressStructure
			Location locationById = locationDao.getLocationById(locationApplyModelVO.getAddressId());
			String addressCode = locationById.getAddressCode();
			locationApply.setAddressId(locationApplyModelVO.getAddressId());
			locationApply.setAddressStructure(commonUtil.getAddressStructure(addressCode,communityById.getCommunityName()));
		}

		locationApply.setCreateTime(new Date());
		locationApply.setApplyStatus(ResidentApplyWebStatusEnum.APPLY_STATUS_NOT_VERIFY.getCode());
		locationApply.setEnable(ResidentApplyAppEnableStatusEnum.APPLY_STATUS_NOT_VERIFY.getCode());
		locationApply.setVersion(Const.INIT_VERSION);
		locationApply.setIdentityType(locationApplyModelVO.getIdentityType());

		locationApply.setCardType(locationApplyModelVO.getCardType());
		locationApply.setCardNum(locationApplyModelVO.getCardNum());
		locationApply.setCredentialsPhotoIds(locationApplyModelVO.getCredentialsPhotoIds());
		locationApply.setResidentName(locationApplyModelVO.getResidentName());
		locationApply.setApplyType(Const.LOCATION_APPLY_TYPE_APP);
		int i = locationApplyDao.addRecord(locationApply);
		if (i<=0){
			throwBusinessException("审核记录添加失败");
		}
		//判断这个居民在这个社区是不是第一次房屋验证
		if (CollectionUtils.isEmpty(byParam2)){
			Resident resident = new Resident();
			resident.setId(residentId);
			//查询这个居民在居民表中的证件照片是不是空
			//如果是空就更新
			String photos = residentById.getCredentialsPhotoIds();
			if (StringUtil.isEmpty(photos)){
				if (StringUtil.isNotEmpty(locationApply.getCredentialsPhotoIds())){
					resident.setCredentialsPhotoIds(locationApplyModelVO.getCredentialsPhotoIds());
				}
			}
			resident.setRealName(locationApplyModelVO.getResidentName());
			resident.setCardType(locationApplyModelVO.getCardType());
			resident.setCardNum(locationApplyModelVO.getCardNum());
			int upd = residentDao.updateResident(resident);
			if (upd <= 0) {
				throwBusinessException("更新居民证件照片失败");
			}
		}
		//组装待办
		//查询居委会管理员
		Long[] targetId = commonUtil.queryOrgAdminByOrgId(orgId);
		if (targetId!=null && targetId.length>0){
			String[] params = {"居民APP提交房屋认证申请后",orgName};
			//组装mq
			MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserTaskByAlias(MessageTemplateEnum.CBO_TASK_RESIDENT_LOCATION_APPLY,
					targetId,
					params,
					locationApply.getId(),
					locationApply.getVersion(),
					residentById.getRealName(),
					null,
					null);
			sendMqPushUtil.sendMqMessage(mqSendMessage);
		}
		return successVo();
	}
	/**
	 * 点击房屋验证按钮出发事件
	 * @return
	 */
	@Override
	public BaseVo beforeLocationApply() {
		BaseVo baseVo = new BaseVo();
		UserInfo userInfo = getCurrentUserInfo();
		Long residentId = userInfo.getId();

		//先查询审核验证表中有没有这个居民的申请信息
		List<LocationApply> recordByResidentId = locationApplyDao.getRecordByResidentId(residentId);

		//如果有 则查询出该居民的基本信息 返给前端
		if (CollectionUtils.isNotEmpty(recordByResidentId)){
			Resident residentById = residentDao.getResidentById(residentId);
			BeforeLocationApplyVO beforeLocationApplyVO = new BeforeLocationApplyVO();
			beforeLocationApplyVO.setRealName(residentById.getRealName());
			beforeLocationApplyVO.setCardNum(residentById.getCardNum());
			beforeLocationApplyVO.setCardType(residentById.getCardType());
			String credentialsPhotoIds = residentById.getCredentialsPhotoIds();
			if (StringUtil.isNotEmpty(credentialsPhotoIds)){
				List<Long> photoIds = Arrays.asList(credentialsPhotoIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
				FileInfoVO fileInfoVO = new FileInfoVO();
				fileInfoVO.setFileIds(photoIds);
				BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
				if (byIds.getData()!=null){
					String ss = JSON.toJSONString(byIds.getData());
					List<FileInfo> fileInfos = JSON.parseArray(ss, FileInfo.class);
					beforeLocationApplyVO.setCredentialsPhotoFileInfos(fileInfos);
				}
			}
			baseVo.setData(beforeLocationApplyVO);
		}

		return baseVo;
	}
	/**
	 * 切换小区
	 * @param communityId
	 * @return
	 */
	@Override
	public BaseVo switchCommunity(Long communityId) {
		BaseVo baseVo = new BaseVo();
		UserInfo userInfo = getCurrentUserInfo();
		CboInfo cboInfo = userInfo.getCboInfo();
		//查询出所有的小区
		List<Community> allCommunity = communityDao.getAllCommunity();
		if (CollectionUtils.isEmpty(allCommunity)){
			throwBusinessException("小区数据异常");
		}
		//接收所有的小区id
		List<Long> communityIds = new ArrayList<>();
		for (Community community : allCommunity) {
			communityIds.add(community.getId());
		}
		if (!communityIds.contains(communityId)){
			throwBusinessException("切换的小区不存在");
		}else {
			//设置当前小区为要切换的小区
			int index = communityIds.indexOf(communityId);
			Community community = allCommunity.get(index);
			com.bit.base.dto.Community dtCommunity = new com.bit.base.dto.Community();

			BeanUtils.copyProperties(community,dtCommunity);

			//根据要切换的小区查询所属的社区
			List<Long> orgId = new ArrayList<>();
			orgId.add(community.getOrgId());
			List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(orgId);
			if (CollectionUtils.isNotEmpty(oaOrganizationList) && oaOrganizationList.size()==1){
				cboInfo.setCurrentCboOrg(oaOrganizationList.get(0));
			}
			//设置社区名称
			dtCommunity.setOrgName(commonUtil.getOrgName(dtCommunity.getOrgId(),oaOrganizationList));
			cboInfo.setCurrentCommunity(dtCommunity);
		}
		//替换token
		String userJson = JSON.toJSONString(userInfo);
		cacheUtil.set(Const.TOKEN_PREFIX+ userInfo.getTid()+":"+userInfo.getToken(), userJson,Long.valueOf(atTokenExpire));
		baseVo.setData(userInfo);

		return baseVo;
	}
	/**
	 * 居民信息分页查询
	 * @param residentPageModelVO
	 * @return
	 */
	@Override
	public BaseVo listPage(ResidentPageVO residentPageModelVO) {
		//判断当前用户是不是社区办
//		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();
//		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		//如果不是社区办
//		if (!userType.equals(USER_TYPE_ORG_SHE_QU_BAN)){
//			residentPageModelVO.setOrgId(orgId);
//			residentPageModelVO.setFlag(Const.FLAG_SHEQUBAN_NO);
//		}else {
//			residentPageModelVO.setFlag(Const.FLAG_SHEQUBAN_YES);
//		}
		List<Long> communityIds = new ArrayList<>();
		if (residentPageModelVO.getOrgId()!=null){
			if (residentPageModelVO.getCommunityId()!=null){
				communityIds.add(residentPageModelVO.getCommunityId());
			}else {
				//查询社区下所有小区
				List<Community> communityByOrgId = communityDao.getCommunityByOrgId(residentPageModelVO.getOrgId());
				if (CollectionUtils.isNotEmpty(communityByOrgId)){
					for (Community community : communityByOrgId) {
						communityIds.add(community.getId());
					}
				}
			}
		}else {
			Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
			List<Community> communityByOrgId = communityDao.getCommunityByOrgId(orgId);
			if (CollectionUtils.isNotEmpty(communityByOrgId)){
				for (Community community : communityByOrgId) {
					communityIds.add(community.getId());
				}
			}
		}
		residentPageModelVO.setCommunityIds(communityIds);


		PageHelper.startPage(residentPageModelVO.getPageNum(), residentPageModelVO.getPageSize());
		List<ResidentPageVO> residentPageVOS = residentDao.listPage(residentPageModelVO);
		//社区办身份 补全社区名称 和 小区名称
//		if (userType.equals(USER_TYPE_ORG_SHE_QU_BAN)){
			if (CollectionUtils.isNotEmpty(residentPageVOS)){
				List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();

				for (ResidentPageVO residentPageVO : residentPageVOS) {
					residentPageVO.setOrgName(commonUtil.getOrgName(residentPageVO.getOrgId(),oaOrganizationList));
				}
			}
//		}
		PageInfo<ResidentPageVO> pageInfo = new PageInfo<>(residentPageVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 居民信息导出excel
	 * @param resident 查询条件
	 * @return
	 */
	@Override
	public void exportToExcel(Resident resident,HttpServletResponse response) {
		UserInfo userInfo = getCurrentUserInfo();

		String name = "居民信息";
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();

		List<ResidentExcelVO> residentExcelVOList = residentDao.exportToExcel(resident);

		if(residentExcelVOList.isEmpty()){

			String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
			ExcelHandler.exportExcel(residentExcelVOList,fileName,name,ResidentExcelVO.class,response);
		}

		//判断当前用户是不是社区办
		Integer userType = userInfo.getCboInfo().getUserType();
		if(!userType.equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			residentExcelVOList.forEach(re->re.setOrgName(userInfo.getCboInfo().getCboOrgs().get(0).getName()));
		}else {
			Map<Long,OaOrganization> oaOrganizationMap = new HashMap<>();

			//如果是社区办
			List<Long> ids = new ArrayList<>();
			residentExcelVOList.forEach(base->ids.add(base.getOrgId()));
			List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

			//转换Map
			oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

			//社区办增加社区名称
			if(userType.equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
				for(ResidentExcelVO re : residentExcelVOList){
					re.setOrgName(oaOrganizationMap.get(re.getOrgId()).getName());
				}
			}
		}

		//获取国籍	民族	政治面貌   居民扩展信息
		Dict dict = new Dict();
		List<String> modules = new ArrayList<>();
		modules.add(Const.NATIONALITYNAME);
		modules.add(Const.ETHNICITYNAME);
		modules.add(Const.POLITICALNAME);
		modules.add(Const.RESIDENT_EXTEND_TYPE);
		dict.setModules(modules);
		BaseVo baseVo = sysServiceFeign.findByModules(dict);
		String dicts = JSON.toJSONString(baseVo.getData());
		Map<String,List<Dict>> mapType = JSON.parseObject(dicts,Map.class);

		//国家名称map转换
		Map<String, Dict> nationalityMap = getDictMapByModuleName(mapType, Const.NATIONALITYNAME);

		//民族名称map转换
		Map<String, Dict> ethnicityMap = getDictMapByModuleName(mapType, Const.ETHNICITYNAME);

		//政治面貌map转换
		Map<String, Dict> politicalMap = getDictMapByModuleName(mapType, Const.POLITICALNAME);

		//居民扩展信息map转换
		Map<String, Dict> residentTypeMap = getDictMapByModuleName(mapType, Const.RESIDENT_EXTEND_TYPE);

		residentExcelVOList.forEach(re->{

			//国家
			if(re.getNationality() != null && !re.getNationality().equals("")){
				if (nationalityMap.get(String.valueOf(re.getNationality()))!=null){
					re.setNationalityName(nationalityMap.get(String.valueOf(re.getNationality())).getDictName());
				}
			}

			//民族
			if(re.getEthnicity() != null && !re.getEthnicity().equals("")){
				if (ethnicityMap.get(String.valueOf(re.getEthnicity()))!=null){
					re.setEthnicityName(ethnicityMap.get(String.valueOf(re.getEthnicity())).getDictName());
				}
			}

			//政治面貌
			if(re.getFaith() != null && !re.getFaith().equals("")){
				if (politicalMap.get(String.valueOf(re.getFaith()))!=null){
					re.setFaithName(politicalMap.get(String.valueOf(re.getFaith())).getDictName());
				}
			}

			//居民扩展信息
			if(re.getExtendType() != null && !re.getExtendType().equals("")){
				String[] extendTypes = re.getExtendType().split(",");
				String residentExtendTypes = "";
				for(String extendType : extendTypes){
					if (residentTypeMap.get(String.valueOf(extendType))!=null){
						residentExtendTypes = residentExtendTypes+(residentExtendTypes.equals("")?residentTypeMap.get(String.valueOf(extendType)).getDictName():","+residentTypeMap.get(String.valueOf(extendType)).getDictName());
					}
				}
				re.setExtendType(residentExtendTypes);
			}

		});

		//导出excel
		String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
		ExcelHandler.exportExcel(residentExcelVOList,fileName,name,ResidentExcelVO.class,response);

	}

	/**
	 * 根据字典表 module获取 module集合
	 * @param mapType 需要获取的dict集合
	 * @param moduleName module名称
	 * @return Map<String, Dict>
	 */
	private Map<String, Dict> getDictMapByModuleName(Map<String, List<Dict>> mapType, String moduleName) {
		List<Dict> dictList = JSON.parseArray(JSON.toJSONString(mapType.get(moduleName)), Dict.class);
		return dictList.stream().collect(Collectors.toMap(Dict::getDictCode, d -> d));
	}

	/**
	 * 新增居民
	 * @param residentVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo webadd(ResidentVO residentVO) {
		UserInfo userInfo = getCurrentUserInfo();
		Long userId = userInfo.getId();
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		String orgName = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getName();

		Resident resident = new Resident();
		BeanUtils.copyProperties(residentVO,resident);
		resident.setId(userId);
		resident.setRole(ResidentRoleTypeEnum.RESIDENT_ROLE_TYPE_RESIDENT.getCode());
		resident.setStatus(ResidentStatusEnum.RESIDENT_STATUS_NORMAL.getCode());
		resident.setCreateType(ResidentCreateTypeEnum.RESIDENT_CREATE_TYPE_WEB_ENUM.getCode());
		resident.setCreateUserId(userId);
		resident.setCreateTime(new Date());
		if (StringUtil.isEmpty(residentVO.getCredentialsPhotoIds())){
			resident.setCredentialsPhotoIds("");
		}
		int i = residentDao.insertResident(resident);
		if (i<=0){
			throwBusinessException("插入居民数据异常");
		}
		//插入住房信息
		locationApplyService.insertLocationApply(residentVO,resident,userId,orgId,orgName);

		//插入扩展信息
		List<ResidentExtend> residentExtendList = residentVO.getResidentExtendList();
		if (CollectionUtils.isNotEmpty(residentExtendList)){
			for (ResidentExtend extendType : residentExtendList) {
				extendType.setResidentId(resident.getId());
				extendType.setOrgId(orgId);
			}

			int add1 = residentExtendDao.batchAddResidentExtend(residentExtendList);
			if (add1<=0){
				throwBusinessException("插入居民扩展信息异常");
			}
		}
		return successVo();
	}

	/**
	 * 单查或返显居民信息
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo reflectById(Long id) {
		UserInfo userInfo = getCurrentUserInfo();
		//获取当前用户所在的社区
		Long orgId = userInfo.getCboInfo().getCurrentCboOrg().getId();
		Integer userType = userInfo.getCboInfo().getUserType();
		ResidentVO residentVO = new ResidentVO();
		Resident residentById = residentDao.getResidentById(id);
		if (residentById==null){
			throwBusinessException("居民信息不存在");
		}
		BeanUtils.copyProperties(residentById,residentVO);
		//设置扩展信息
		ResidentExtend extend = new ResidentExtend();
		extend.setResidentId(id);
		if (!userType.equals(USER_TYPE_ORG_SHE_QU_BAN)){
			extend.setOrgId(orgId);
		}
		List<ResidentExtend> byParam = residentExtendDao.findByParam(extend);
		List<Integer> extendTypes = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(byParam)){
			for (ResidentExtend residentExtend : byParam) {
				extendTypes.add(residentExtend.getExtendType());
			}
		}
		residentVO.setExtendTypeIds(extendTypes);

		//设置居民住房信息
		ResidentRelLocation location = new ResidentRelLocation();
		location.setResidentId(id);
		if (!userType.equals(USER_TYPE_ORG_SHE_QU_BAN)){
			location.setOrgId(orgId);
		}
		List<ResidentRelLocationVO> byParam1 = residentRelLocationDao.findByParamWithLocation(location);
		if (CollectionUtils.isNotEmpty(byParam1)){
			for (ResidentRelLocationVO residentRelLocationVO : byParam1) {
				commonUtil.getAddressNames(residentRelLocationVO.getAddressCode(),residentRelLocationVO.getCommunityId(),residentRelLocationVO);
			}
		}
		residentVO.setResidentRelLocationVOS(byParam1);

		if (residentById.getIcon()!=null){
			//查询头像文件
			BaseVo byId = fileServiceFeign.findById(residentById.getIcon());
			if (byId.getData()!=null){
				String ss = JSON.toJSONString(byId.getData());
				FileInfo fileInfo = JSON.parseObject(ss, FileInfo.class);
				residentVO.setIconFile(fileInfo);
			}
		}

		//查询身份证照片
		String credentialsPhotoIds = residentById.getCredentialsPhotoIds();
		if (StringUtil.isNotEmpty(credentialsPhotoIds)){
			List<Long> photoIds = Arrays.asList(credentialsPhotoIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFileIds(photoIds);
			BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
			if (byIds.getData()!=null){
				String ss = JSON.toJSONString(byIds.getData());
				List<FileInfo> fileInfos = JSON.parseArray(ss, FileInfo.class);
				residentVO.setCredentialsPhotoFileInfos(fileInfos);
			}
		}

		//国籍、民族，2选一 就去查
		Map<String,List<Dict>> mapType = null;
		if(residentVO.getEthnicity()!=null || residentVO.getNationality()!=null){
			//获取国籍	民族
			Dict dict = new Dict();
			List<String> modules = new ArrayList<>();
			modules.add(Const.NATIONALITYNAME);
			modules.add(Const.ETHNICITYNAME);
			dict.setModules(modules);
			BaseVo baseVo = sysServiceFeign.findByModules(dict);
			String dicts = JSON.toJSONString(baseVo.getData());
			mapType = JSON.parseObject(dicts,Map.class);

		}

		if (residentVO.getEthnicity()!=null){
			//设置民族
			Map<String, Dict> ethnicityMap = getDictMapByModuleName(mapType, Const.ETHNICITYNAME);
			if (!ethnicityMap.isEmpty()){
				residentVO.setEthnicityName(ethnicityMap.get(String.valueOf(residentVO.getEthnicity())).getDictName());
			}
		}

		if (residentVO.getNationality()!=null){
			//设置国籍
			Map<String, Dict> nationalityMap = getDictMapByModuleName(mapType, Const.NATIONALITYNAME);
			if (!nationalityMap.isEmpty()){
				residentVO.setNationalityName(nationalityMap.get(String.valueOf(residentVO.getNationality())).getDictName());
			}
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(residentVO);
		return baseVo;
	}

	/**
	 * 编辑居民信息
	 * @param residentVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo editResident(ResidentVO residentVO) {
		Long userId = getCurrentUserInfo().getId();
		OaOrganization currentCboOrg = getCurrentUserInfo().getCboInfo().getCurrentCboOrg();
		Long orgId = currentCboOrg.getId();
		String orgName = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getName();
		//校验证件号码是否重复
		Integer count = residentDao.countResidentCardNumExist(residentVO.getCardType(), residentVO.getCardNum(), residentVO.getId());
		if (count>0){
			throwBusinessException("身份证信息重复");
		}

		//编辑扩展信息
		List<ResidentExtend> residentExtends = residentVO.getResidentExtendList();
		if (CollectionUtils.isNotEmpty(residentExtends)){
			//删除社区下这个人的扩展信息
			residentExtendDao.delByResidentIdAndOrgId(residentVO.getId(), orgId);
			for (ResidentExtend residentExtend : residentExtends) {
				residentExtend.setOrgId(orgId);
			}
			int extendadd = residentExtendDao.batchAddResidentExtend(residentExtends);
			if (extendadd<=0){
				throwBusinessException("居民扩展信息新增异常");
			}
		}else {
			//删除社区下这个人的扩展信息
			residentExtendDao.delByResidentIdAndOrgId(residentVO.getId(), orgId);
		}
		Resident residentById = residentDao.getResidentById(residentVO.getId());
		if (residentById==null){
			throwBusinessException("居民不存在");
		}

		//编辑住房信息
		List<ResidentRelLocation> residentRelLocations = locationApplyService.insertLocationForEditResident(residentVO, residentById, orgId, userId, orgName);

		Resident obj = new Resident();
		BeanUtils.copyProperties(residentVO,obj);
		//判断基本信息是否完善
		if (StringUtil.isNotEmpty(obj.getRealName()) &&
				StringUtil.isNotEmpty(obj.getCardNum()) &&
				obj.getCardType()!=null &&
				StringUtil.isNotEmpty(obj.getMobile()) &&
				obj.getSex()!=null &&
				StringUtil.isNotEmpty(obj.getBirthday()) &&
				obj.getEthnicity()!=null &&
				obj.getNationality()!=null &&
				obj.getResidentType()!=null){
			obj.setStatus(ResidentStatusEnum.RESIDENT_STATUS_NORMAL.getCode());
		}else {
			obj.setStatus(ResidentStatusEnum.RESIDENT_STATUS_NEED_FILL.getCode());
		}
		//改变居民身份
		//如果房屋关联关系不为空 就设置居民身份
		if (CollectionUtils.isNotEmpty(residentRelLocations)){
			obj.setRole(ResidentRoleTypeEnum.RESIDENT_ROLE_TYPE_RESIDENT.getCode());
		}

		//编辑居民信息
		int i = residentDao.updateResident(obj);
		if (i <= 0) {
			throwBusinessException("居民信息更新异常");
		}
		//去除token
		String o = (String) cacheUtil.get(residentById.getToken());
		if (StringUtil.isNotEmpty(o)){
			tokenUtil.delToken(residentById.getToken());
		}

		return successVo();
	}

	/**
	 * 发送房屋验证推送
	 * @param residentId
	 * @param firstparam
	 */
	private void sendLocationApplyMq(Long residentId,String firstparam,String secondParam,MessageTemplateEnum messageTemplateEnum){
		Long[] targetId = {residentId};
		if (targetId!=null && targetId.length>0){
			String[] params = {firstparam,secondParam};
			//组装mq
			MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(messageTemplateEnum,
					targetId,
					params,
					null,
					null,
					null);
			sendMqPushUtil.sendMqMessage(mqSendMessage);
		}
	}

	/**
	 * 根据证件号码和证件类型返显居民记录
	 * @param residentVO
	 * @return
	 */
	@Override
	public BaseVo copyByCardNum(ResidentVO residentVO) {
		//如果有id就直接返回
		if (residentVO.getId()!=null){
			return successVo();
		}
		BaseVo baseVo = new BaseVo();
		//当前社区id
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		//先传本社区id 校验本社区是否存在
		residentVO.setOrgId(orgId);
		Resident resident = residentDao.getResidentByCardNumAndCardType(residentVO);
		if (resident!=null){
			ResidentVO rvo = new ResidentVO();
			BeanUtils.copyProperties(resident,rvo);
			baseVo.setCode(ResultCode.RESIDENT_EXIST_IN_ORG.getCode());
			baseVo.setMsg(ResultCode.RESIDENT_EXIST_IN_ORG.getInfo());
			//查询居民的头像和证件照等信息
			rvo = this.returnResident(resident,rvo);
			baseVo.setData(rvo);
		}else {
			//校验其他社区是否存在
			residentVO.setOrgId(null);
			residentVO.setNorgId(orgId);
			Resident otherresident = residentDao.getResidentByCardNumAndCardType(residentVO);
			if (otherresident!=null){
				baseVo.setCode(ResultCode.RESIDENT_EXIST_IN_OTHER_ORG.getCode());
				baseVo.setMsg(ResultCode.RESIDENT_EXIST_IN_OTHER_ORG.getInfo());
				//处理头像和证件照
				ResidentVO rvo = new ResidentVO();
				BeanUtils.copyProperties(otherresident,rvo);
				//查询居民的头像和证件照等信息
				rvo = this.returnResident(otherresident,rvo);
				baseVo.setData(rvo);
			}else {
				//游客也要带出去
				residentVO.setOrgId(null);
				residentVO.setNorgId(null);
				Resident tour = residentDao.getResidentByCardNumAndCardType(residentVO);
				ResidentVO tourvo = new ResidentVO();
				BeanUtils.copyProperties(tour,tourvo);
				//查询居民的头像和证件照等信息
				tourvo = this.returnResident(tour,tourvo);
				baseVo.setCode(ResultCode.RESIDENT_NOT_EXIST_IN_ANY_ORG.getCode());
				baseVo.setMsg(ResultCode.RESIDENT_NOT_EXIST_IN_ANY_ORG.getInfo());
				baseVo.setData(tourvo);
			}

		}

		return baseVo;
	}

	/**
	 * 查询居民的头像和证件照等信息
	 * @param resident
	 * @return
	 */
	private ResidentVO returnResident(Resident resident,ResidentVO rvo){
		if (resident==null){
			throwBusinessException("居民参数不能为空");
		}
		if (resident.getIcon()!=null){
			BaseVo byId = fileServiceFeign.findById(resident.getIcon());
			if (byId.getData()!=null){
				String s = JSON.toJSONString(byId.getData());
				FileInfo iconFile = JSONObject.parseObject(s,FileInfo.class);
				rvo.setIconFile(iconFile);
			}
		}
		if (StringUtil.isNotEmpty(resident.getCredentialsPhotoIds())){
			List<Long> photoIds = Arrays.asList(resident.getCredentialsPhotoIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFileIds(photoIds);
			BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
			if (byIds.getData()!=null){
				String s = JSON.toJSONString(byIds.getData());
				List<FileInfo> fileInfos = JSONArray.parseArray(s,FileInfo.class);
				rvo.setCredentialsPhotoFileInfos(fileInfos);
			}
		}
		return rvo;
	}

	/**
	 * 删除居民住房关联关系
	 * @param residentVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo delResidentRelLocation(ResidentVO residentVO) {
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		Long residentId = residentVO.getResidentId();
		Long relId = residentVO.getLocationRelId();

		ResidentRelLocation residentRelLocationById = residentRelLocationDao.getResidentRelLocationById(relId);
		if (residentRelLocationById==null){
			throwBusinessException("关联关系不存在");
		}
		//先删除住房
		int del = residentRelLocationDao.delById(relId);
		if (del>0){
			//每删除一条住房信息，则该居民APP中的房屋认证记录状态由‘已认证’修改为‘已失效

			//现在记录表里查询有没有认证记录
			LocationApplyVO locationApplyModelVO = new LocationApplyVO();
			locationApplyModelVO.setResidentId(residentId);
			locationApplyModelVO.setOrgId(orgId);
			locationApplyModelVO.setAddressId(residentRelLocationById.getAddressId());
			List<LocationApply> byParam = locationApplyDao.findByParam(locationApplyModelVO);
			if (CollectionUtils.isNotEmpty(byParam)){
				for (LocationApply locationApply : byParam) {
					if (locationApply.getAddressId().equals(residentRelLocationById.getAddressId())){
						LocationApplyVO la = new LocationApplyVO();
						la.setId(locationApply.getId());
						la.setEnable(ResidentApplyAppEnableStatusEnum.APPLY_STATUS_LOST_FUNCTION.getCode());
						la.setVersion(locationApply.getVersion());
						int i = locationApplyDao.updateLocationApply(la);
						if (i<=0){
							throwBusinessException("认证记录更新失败");
						}
					}
				}
			}
		}

		ResidentRelLocation residentRelLocation = new ResidentRelLocation();
		residentRelLocation.setResidentId(residentId);
		//查询这个人在所有社区一共多少套房
		List<ResidentRelLocation> byParam = residentRelLocationDao.findByParam(residentRelLocation);
		//如果为空 变游客
		if (CollectionUtils.isEmpty(byParam)){
			Resident rm = new Resident();
			rm.setId(residentId);
			rm.setRole(ResidentRoleTypeEnum.RESIDENT_ROLE_TYPE_TOURIST.getCode());
			int upd = residentDao.updateResident(rm);
			if (upd<=0){
				throwBusinessException("居民游客身份更新失败");
			}
			//删除这个人在这个社区的扩展信息
			residentExtendDao.delByResidentIdAndOrgId(residentId, orgId);

		}
		//去除token
		Resident residentById = residentDao.getResidentById(residentId);
		if (residentById==null){
			throwBusinessException("居民不存在");
		}
		String o = (String) cacheUtil.get(residentById.getToken());
		if (StringUtil.isNotEmpty(o)){
			tokenUtil.delToken(residentById.getToken());
		}
		return successVo();
	}

	/**
	 * 获取所有居民基础信息
	 * @author liyang
	 * @date 2019-07-23
	 * @param residentPageVO : 分页
	 * @return : BaseVo
	 */
	@Override
	public BaseVo baseResidentInfo(ResidentPageVO residentPageVO) {

		String orderBy=" t.birthday";
		PageHelper.startPage(residentPageVO.getPageNum(),residentPageVO.getPageSize(),orderBy);
		List<Resident> residentList = residentDao.findBaseResidentInfoSql(residentPageVO);
		PageInfo<Resident> pageInfo = new PageInfo<>(residentList);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 校验手机号是否存在
	 * @param mobile
	 * @return
	 */
	@Override
	public BaseVo checkMobile(String mobile) {
		boolean b = checkResidentExist(mobile);
		if (b){
			throwBusinessException("手机号已存在");
		}else {
			return successVo();
		}
		return null;
	}
	/**
	 * 初次更新居民密码
	 * @param commonVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo updateResidentPassword(CommonVO commonVO) {
		Long id = commonVO.getId();
		Resident residentById = residentDao.getResidentById(id);
		if (residentById==null){
			throwBusinessException("居民不存在");
		}
		String password = commonVO.getPassword();
		String npass = MD5Util.compute(password + residentById.getSalt());

		Resident obj = new Resident();
		obj.setId(id);
		obj.setPassword(npass);
		int i = residentDao.updateResident(obj);
		if (i<=0){
			throwBusinessException("更新居民密码失败");
		}
		return successVo();
	}
	/**
	 * 居民app 校验密码是否正确
	 * @param changePassWordMobileVO
	 * @return
	 */
	@Override
	public BaseVo checkPassword(ChangePassWordMobileVO changePassWordMobileVO) {
		Long residentId = getCurrentUserInfo().getId();
		Resident residentById = residentDao.getResidentById(residentId);
		if (residentById==null){
			throwBusinessException("该用户不存在");
		}
		if (StringUtil.isEmpty(changePassWordMobileVO.getOldPassWord())){
			throwBusinessException("密码不能为空");
		}
		String salt = residentById.getSalt();
		//如果旧密码与数据库中密码不同
		if (!MD5Util.compute(changePassWordMobileVO.getOldPassWord() + salt).equals(residentById.getPassword())){
			throwBusinessException("密码错误");
			//redis中记录错误次数
//			String key = Const.CBO_CHANGE_PASSWORD + Const.TID_CBO_RESIDENT_APP + residentById.getMobile();
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
	 * 居民app 更改密码
	 * @param changePassWordMobileVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo changePassword(ChangePassWordMobileVO changePassWordMobileVO) {
		return appCommonService.changePassWord(changePassWordMobileVO);
	}
	/**
	 * 居民app 更改手机号
	 * @param changePassWordMobileVO
	 * @return
	 */
	@Override
	public BaseVo changeMobile(ChangePassWordMobileVO changePassWordMobileVO) {
		return appCommonService.changeMobile(changePassWordMobileVO);
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
