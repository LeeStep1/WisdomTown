package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.ResidentApplyAppEnableStatusEnum;
import com.bit.common.enumerate.ResidentApplyWebStatusEnum;
import com.bit.common.enumerate.ResidentIdentityEnum;
import com.bit.common.enumerate.ResidentRoleTypeEnum;
import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.dao.*;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.vo.*;
import com.bit.module.cbo.service.LocationApplyService;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.CommonUtil;
import com.bit.utils.StringUtil;
import com.bit.utils.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/19 13:21
 **/
@Service("locationApplyService")
public class LocationApplyServiceImpl extends BaseService implements LocationApplyService {

	@Autowired
	private LocationApplyDao locationApplyDao;
	@Autowired
	private FileServiceFeign fileServiceFeign;
	@Autowired
	private ResidentRelLocationDao residentRelLocationDao;
	@Autowired
	private ResidentDao residentDao;
	@Autowired
	private TokenUtil tokenUtil;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private SendMqPushUtil sendMqPushUtil;

	/**
	 * 房屋信息dao
	 */
	@Autowired
	private LocationDao locationDao;


	/**
	 * 认证审核 分页查询
	 * @param locationApplyPageVO
	 * @return
	 */
	@Override
	public BaseVo listPage(LocationApplyPageVO locationApplyPageVO) {
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		locationApplyPageVO.setOrgId(orgId);
		PageHelper.startPage(locationApplyPageVO.getPageNum(), locationApplyPageVO.getPageSize());
		locationApplyPageVO.setApplyType(Const.LOCATION_APPLY_TYPE_APP);
		//20190903按照申请的创建时间排序
		List<LocationApplyVO> locationApplyVOS = locationApplyDao.listPage(locationApplyPageVO);
		//身份批量转换
		identityTypeConvertTypeStr(locationApplyVOS);
		PageInfo<LocationApplyVO> pageInfo = new PageInfo<LocationApplyVO>(locationApplyVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 审核
	 * @param locationApplyModelVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo audit(LocationApplyVO locationApplyModelVO) {
		Long userId = getCurrentUserInfo().getId();
		Long recordId = locationApplyModelVO.getId();
		LocationApply recordById = locationApplyDao.getRecordById(recordId);
		if (recordById==null){
			throwBusinessException("审核记录不存在");
		}
		if (!locationApplyModelVO.getVersion().equals(recordById.getVersion())){
			throwBusinessException("该条记录已经被审核");
		}
		//如果审核拒绝 要将审核的哪条记录 设置为失效
		if (locationApplyModelVO.getApplyStatus().equals(ResidentApplyWebStatusEnum.APPLY_STATUS_REJECTED.getCode())){
			locationApplyModelVO.setEnable(ResidentApplyAppEnableStatusEnum.APPLY_STATUS_REJECTED.getCode());
		}else {
			locationApplyModelVO.setEnable(locationApplyModelVO.getApplyStatus());
		}
		locationApplyModelVO.setVersion(recordById.getVersion());
		int i = locationApplyDao.updateLocationApply(locationApplyModelVO);
		if (i<=0){
			throwBusinessException("审核失败");
		}
		//通过 添加居民住房关系表 修改居民身份
		if (locationApplyModelVO.getApplyStatus().equals(ResidentApplyWebStatusEnum.APPLY_STATUS_PASSED.getCode())){
			//添加记录到居民住房关系表
			ResidentRelLocation residentRelLocation = new ResidentRelLocation();
			residentRelLocation.setResidentId(recordById.getResidentId());
			residentRelLocation.setOrgId(recordById.getOrgId());
			residentRelLocation.setCommunityId(recordById.getCommunityId());
			residentRelLocation.setAddressId(recordById.getAddressId());
			residentRelLocation.setCreateTime(new Date());
			residentRelLocation.setCreateUserId(userId);
			residentRelLocation.setUpdateTime(new Date());
			residentRelLocation.setUpdateUserId(userId);
			residentRelLocation.setIdentityType(recordById.getIdentityType());
			int add = residentRelLocationDao.add(residentRelLocation);
			if (add<=0){
				throwBusinessException("添加居民房屋关系失败");
			}
			//修改居民的身份
			Resident obj = new Resident();
			obj.setId(recordById.getResidentId());
			obj.setRole(ResidentRoleTypeEnum.RESIDENT_ROLE_TYPE_RESIDENT.getCode());
			//如果是身份证 就自动补充性别和生日
			if (recordById.getCardType()!=null && recordById.getCardType().equals(Const.CARD_NUM_TYPE_SHENFENZHENG)){
				String cardNum = recordById.getCardNum();
				String sex = cardNum.substring(cardNum.length()-2,cardNum.length()-1);
				//奇数是男 偶数是女
				if (Integer.valueOf(sex) % 2 == 1){
					obj.setSex(Const.SEX_MALE);
				}else {
					obj.setSex(Const.SEX_FEMALE);
				}
				String birthday = cardNum.substring(6,10) + "-" +
						cardNum.substring(10,12) + "-" +
						cardNum.substring(12,14);
				obj.setBirthday(birthday);
			}
			int ii = residentDao.updateResident(obj);
			if (ii<=0){
				throwBusinessException("居民身份信息更新失败");
			}
			//如果通过了就强制退出
			Long residentId = recordById.getResidentId();
			Resident residentById = residentDao.getResidentById(residentId);
			if (residentById!=null){
				tokenUtil.delToken(residentById.getToken());
			}
			//如果审核通过 发送推送
			Community communityById = communityDao.getCommunityById(recordById.getCommunityId());
			sendLocationApplyMq(recordById.getResidentId(),"房屋认证审核",communityById.getCommunityName(),MessageTemplateEnum.CBO_REMIND_LOCATION_APPLY_PASS);
		}
		//如果审核被拒绝 发送推送
		if (locationApplyModelVO.getApplyStatus().equals(ResidentApplyWebStatusEnum.APPLY_STATUS_REJECTED.getCode())){
			sendLocationApplyMq(recordById.getResidentId(),"房屋认证审核",locationApplyModelVO.getComment(), MessageTemplateEnum.CBO_REMIND_LOCATION_APPLY_REFUSE);
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
	 * app端房屋审核记录查询
	 * @param locationApplyPageVO
	 * @return
	 */
	@Override
	public BaseVo appApplyListPage(LocationApplyPageVO locationApplyPageVO) {
		Long residentId = getCurrentUserInfo().getId();
		PageHelper.startPage(locationApplyPageVO.getPageNum(), locationApplyPageVO.getPageSize());
		if (residentId!=null){
			locationApplyPageVO.setResidentId(residentId);
		}
		locationApplyPageVO.setApplyType(Const.LOCATION_APPLY_TYPE_APP);
		List<LocationApplyVO> locationApplyVOS = locationApplyDao.appApplyListPage(locationApplyPageVO);

		//身份批量转换
		identityTypeConvertTypeStr(locationApplyVOS);

		PageInfo<LocationApplyVO> pageInfo = new PageInfo<>(locationApplyVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 根据居民身份TYPE批量 转换成String
	 * @author liyang
	 * @date 2020-06-15
	 * @param locationApplyVOS :  房屋认证申请展示用 集合
	*/
	private void identityTypeConvertTypeStr(List<LocationApplyVO> locationApplyVOS) {
		for (LocationApplyVO locationApplyVO : locationApplyVOS) {
			if (locationApplyVO.getIdentityType().equals(ResidentIdentityEnum.RESIDENT_IDENTITY_OWNER.getCode())) {
				locationApplyVO.setIdentityTypeStr(ResidentIdentityEnum.RESIDENT_IDENTITY_OWNER.getInfo());
			} else if (locationApplyVO.getIdentityType().equals(ResidentIdentityEnum.RESIDENT_IDENTITY_FAMILY_MEMBER.getCode())) {
				locationApplyVO.setIdentityTypeStr(ResidentIdentityEnum.RESIDENT_IDENTITY_FAMILY_MEMBER.getInfo());
			} else if (locationApplyVO.getIdentityType().equals(ResidentIdentityEnum.RESIDENT_IDENTITY_RENT.getCode())) {
				locationApplyVO.setIdentityTypeStr(ResidentIdentityEnum.RESIDENT_IDENTITY_RENT.getInfo());
			}
			locationApplyVO.setAddressStructure(commonUtil.ignoreFloorAddressName(locationApplyVO.getAddressStructure()));
		}
	}

	/**
	 * 返显房屋认证申请记录
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo reflectById(Long id) {
		LocationApply recordById = locationApplyDao.getRecordById(id);
		if (recordById==null){
			throwBusinessException("该记录不存在");
		}
		LocationApplyVO locationApplyVO = new LocationApplyVO();
		BeanUtils.copyProperties(recordById,locationApplyVO);
		locationApplyVO.setRealName(recordById.getResidentName());
		locationApplyVO.setAddressStructure(commonUtil.ignoreFloorAddressName(recordById.getAddressStructure()));
		Resident residentById = residentDao.getResidentById(recordById.getResidentId());
		if (residentById!=null){
			locationApplyVO.setMobile(residentById.getMobile());
		}

		String credentialsPhotoIds = residentById.getCredentialsPhotoIds();
		if (StringUtil.isNotEmpty(credentialsPhotoIds)){
			List<Long> photoIds = Arrays.asList(credentialsPhotoIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFileIds(photoIds);
			BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
			if (byIds.getData()!=null){
				String ss = JSON.toJSONString(byIds.getData());
				List<FileInfo> fileInfos = JSON.parseArray(ss, FileInfo.class);
				locationApplyVO.setCredentialsPhotoFileInfos(fileInfos);
			}
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(locationApplyVO);

		return baseVo;
	}
	/**
	 * 居委会app 房屋认证记录 分页查询
	 * @param locationApplyPageVO
	 * @return
	 */
	@Override
	public BaseVo appOrgListPage(LocationApplyPageVO locationApplyPageVO) {
		//获得当前用户的社区
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		PageHelper.startPage(locationApplyPageVO.getPageNum(), locationApplyPageVO.getPageSize());
		if (orgId!=null){
			locationApplyPageVO.setOrgId(orgId);
		}
		List<LocationApplyVO> locationApplyVOS = locationApplyDao.appOrgListPage(locationApplyPageVO);
		//身份批量转换
		identityTypeConvertTypeStr(locationApplyVOS);
		PageInfo<LocationApplyVO> pageInfo = new PageInfo<>(locationApplyVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);

		return baseVo;
	}
	/**
	 * 居委会app 返显房屋认证申请记录
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo appOrgReflectById(Long id) {
		LocationApply recordById = locationApplyDao.getRecordById(id);
		if (recordById==null){
			throwBusinessException("该记录不存在");
		}

		LocationApplyVO recordResidentInfoById = locationApplyDao.getRecordResidentInfoById(id);
		String credentialsPhotoIds = recordResidentInfoById.getCredentialsPhotoIds();
		if (StringUtil.isNotEmpty(credentialsPhotoIds)){
			List<Long> photoIds = Arrays.asList(credentialsPhotoIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFileIds(photoIds);
			BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
			if (byIds.getData()!=null){
				String ss = JSON.toJSONString(byIds.getData());
				List<FileInfo> fileInfos = JSON.parseArray(ss, FileInfo.class);
				recordResidentInfoById.setCredentialsPhotoFileInfos(fileInfos);
			}
		}
		recordResidentInfoById.setAddressStructure(commonUtil.ignoreFloorAddressName(recordResidentInfoById.getAddressStructure()));
		BaseVo baseVo = new BaseVo();
		baseVo.setData(recordResidentInfoById);
		return null;
	}

	/**
	 * 插入住房信息
	 * @param residentVO 居民信息扩展类
	 * @param resident 居民信息
	 * @param userId 登录人ID
	 * @param orgId 登录人所属社区ID
	 * @param orgName	登录人所属社区名称
	 */
	public void insertLocationApply(ResidentVO residentVO, Resident resident, Long userId, Long orgId, String orgName) {
		List<ResidentRelLocation> residentRelLocationList = residentVO.getResidentRelLocationList();
		if (CollectionUtils.isNotEmpty(residentRelLocationList)){
			for (ResidentRelLocation residentRelLocation : residentRelLocationList) {
				residentRelLocation.setResidentId(resident.getId());
				residentRelLocation.setCreateUserId(userId);
				residentRelLocation.setCreateTime(new Date());
				residentRelLocation.setOrgId(orgId);
			}
			int add = residentRelLocationDao.batchAddResidentRelLocation(residentRelLocationList);
			if (add<=0){
				throwBusinessException("插入居民住房信息异常");
			}

			List<LocationApply> locationApplyList = new ArrayList<>();
			List<LocationApply> updateApplyList = new ArrayList<>();

			//插入居民房屋认证信息
			for (ResidentRelLocation residentRelLocation : residentRelLocationList){
				//判断这个人在这间房子有没有验证记录 没有就新增 有就更新
				LocationApplyVO param = new LocationApplyVO();
				param.setResidentId(resident.getId());
				param.setAddressId(residentRelLocation.getAddressId());
				param.setApplyStatus(ResidentApplyWebStatusEnum.APPLY_STATUS_NOT_VERIFY.getCode());
				List<LocationApply> byParam = locationApplyDao.findByParam(param);
				if (CollectionUtils.isNotEmpty(byParam)){
					if (byParam.size()>1){
						throwBusinessException("查询结果数量异常");
					}else {
						Long recordId = byParam.get(0).getId();
						LocationApply upd = new LocationApply();
						upd.setId(recordId);
						upd.setApplyStatus(ResidentApplyWebStatusEnum.APPLY_STATUS_PASSED.getCode());
						upd.setEnable(ResidentApplyAppEnableStatusEnum.APPLY_STATUS_PASSED.getCode());
						updateApplyList.add(upd);
					}
				}else {
					LocationApply locationApply = new LocationApply();
					locationApply.setResidentId(resident.getId());
					locationApply.setResidentName(resident.getRealName());
					locationApply.setCardNum(resident.getCardNum());
					locationApply.setCardType(resident.getCardType());
					locationApply.setCredentialsPhotoIds(resident.getCredentialsPhotoIds());

					if (residentRelLocation.getCommunityId()!=null){
						locationApply.setCommunityId(residentRelLocation.getCommunityId());
						Community communityById = communityDao.getCommunityById(residentRelLocation.getCommunityId());
						if (communityById!=null){
							locationApply.setCommunityName(communityById.getCommunityName());
						}
					}


					locationApply.setOrgId(orgId);
					locationApply.setOrgName(orgName);
					locationApply.setIdentityType(residentRelLocation.getIdentityType());
					locationApply.setAddressId(residentRelLocation.getAddressId());

					Location locationById = locationDao.getLocationById(residentRelLocation.getAddressId());
					if (locationById!=null){
						locationApply.setAddressStructure(locationById.getFullName());
					}

					locationApply.setCreateTime(new Date());
					locationApply.setUpdateTime(new Date());
					locationApply.setUpdateUserId(null);

					locationApply.setApplyStatus(ResidentApplyWebStatusEnum.APPLY_STATUS_PASSED.getCode());
					locationApply.setEnable(ResidentApplyAppEnableStatusEnum.APPLY_STATUS_PASSED.getCode());
					locationApply.setVersion(Const.INIT_VERSION);
					locationApply.setComment("");
					locationApply.setApplyType(Const.LOCATION_APPLY_TYPE_WEB);

					locationApplyList.add(locationApply);
				}
			}
			if (CollectionUtils.isNotEmpty(updateApplyList)){
				int upd = locationApplyDao.batchUpdateRecords(updateApplyList);
				if (upd<=0){
					throwBusinessException("更新居民房屋认证信息异常");
				}
			}
			if (CollectionUtils.isNotEmpty(locationApplyList)){
				int apply = locationApplyDao.batchAddLocationApplyRecords(locationApplyList);
				if (apply<=0){
					throwBusinessException("插入居民房屋认证信息异常");
				}
			}
		}
	}


	/**
	 * 编辑住房信息---对应修改居民信息时
	 * @param residentVO 居民信息扩展类
	 * @param residentById 居民信息
	 * @param orgId 登录人ID
	 * @param userId 登录人所属社区ID
	 * @param orgName 登录人所属社区名称
	 * @return List<ResidentRelLocation>
	 */
	public List<ResidentRelLocation> insertLocationForEditResident(ResidentVO residentVO,Resident residentById,Long orgId,Long userId,String orgName){
		//编辑住房信息
		List<ResidentRelLocation> residentRelLocations = residentVO.getResidentRelLocationList();

		if (CollectionUtils.isNotEmpty(residentRelLocations)){
			//删除这个人在这个社区下的房屋关联信息
			residentRelLocationDao.delByResidentIdAndOrgId(residentVO.getId(),orgId);
			for (ResidentRelLocation residentRelLocation : residentRelLocations) {
				residentRelLocation.setOrgId(orgId);
				residentRelLocation.setCreateTime(new Date());
				residentRelLocation.setCreateUserId(userId);
				residentRelLocation.setUpdateTime(new Date());
				residentRelLocation.setUpdateUserId(userId);
			}
			//批量新增居民房屋关联关系
			int locationadd = residentRelLocationDao.batchAddResidentRelLocation(residentRelLocations);
			if (locationadd<=0){
				throwBusinessException("居民住房信息新增异常");
			}

			//批量新增编辑的居民住房申请集合
			List<LocationApply> locationApplyList = new ArrayList<>();
			//插入居民房屋认证信息
			for (ResidentRelLocation residentRelLocation : residentRelLocations) {
				//如果id是空就是新增的房屋
				if (residentRelLocation.getId() == null) {
					//判断这条新增的记录是不是审核中的状态
					LocationApplyVO locationApplyVO = new LocationApplyVO();
					locationApplyVO.setAddressId(residentRelLocation.getAddressId());
					locationApplyVO.setResidentId(residentRelLocation.getResidentId());
					locationApplyVO.setApplyStatus(ResidentApplyWebStatusEnum.APPLY_STATUS_NOT_VERIFY.getCode());
					locationApplyVO.setCommunityId(residentRelLocation.getCommunityId());
					List<LocationApply> byParam = locationApplyDao.findByParam(locationApplyVO);
					if (CollectionUtils.isNotEmpty(byParam)){
						Long applyId = byParam.get(0).getId();
						LocationApplyVO applyVO = new LocationApplyVO();
						applyVO.setId(applyId);
						applyVO.setApplyStatus(ResidentApplyWebStatusEnum.APPLY_STATUS_PASSED.getCode());
						applyVO.setEnable(ResidentApplyAppEnableStatusEnum.APPLY_STATUS_PASSED.getCode());
						applyVO.setVersion(byParam.get(0).getVersion());
						locationApplyDao.updateLocationApply(applyVO);
						//20191010需求说要发送推送
						Community communityById = communityDao.getCommunityById(residentRelLocation.getCommunityId());
						sendLocationApplyMq(residentRelLocation.getResidentId(),"房屋认证审核",communityById.getCommunityName(),MessageTemplateEnum.CBO_REMIND_LOCATION_APPLY_PASS);
					}

					LocationApply locationApply = new LocationApply();
					locationApply.setResidentId(residentVO.getId());
					locationApply.setResidentName(residentById.getRealName());
					locationApply.setCardNum(residentById.getCardNum());
					locationApply.setCardType(residentById.getCardType());
					locationApply.setCredentialsPhotoIds(residentById.getCredentialsPhotoIds());

					if (residentRelLocation.getCommunityId() != null) {
						locationApply.setCommunityId(residentRelLocation.getCommunityId());
						Community communityById = communityDao.getCommunityById(residentRelLocation.getCommunityId());
						if (communityById != null) {
							locationApply.setCommunityName(communityById.getCommunityName());
						}
					}


					locationApply.setOrgId(orgId);
					locationApply.setOrgName(orgName);
					locationApply.setIdentityType(residentRelLocation.getIdentityType());
					locationApply.setAddressId(residentRelLocation.getAddressId());

					Location locationById = locationDao.getLocationById(residentRelLocation.getAddressId());
					if (locationById != null) {
						locationApply.setAddressStructure(locationById.getFullName());
					}

					locationApply.setCreateTime(new Date());
					locationApply.setUpdateTime(new Date());
					locationApply.setUpdateUserId(null);

					locationApply.setApplyStatus(ResidentApplyWebStatusEnum.APPLY_STATUS_PASSED.getCode());
					locationApply.setEnable(ResidentApplyAppEnableStatusEnum.APPLY_STATUS_PASSED.getCode());
					locationApply.setVersion(Const.INIT_VERSION);
					locationApply.setComment("");
					locationApply.setApplyType(Const.LOCATION_APPLY_TYPE_WEB);
					//如果这个房屋没有申请验证 就放入list中
					if (CollectionUtils.isEmpty(byParam)){
						locationApplyList.add(locationApply);
					}
				}
			}
			if (CollectionUtils.isNotEmpty(locationApplyList)){
				int apply = locationApplyDao.batchAddLocationApplyRecords(locationApplyList);
				if (apply<=0){
					throwBusinessException("插入居民房屋认证信息异常");
				}
			}
		}

		return residentRelLocations;
	}

}
