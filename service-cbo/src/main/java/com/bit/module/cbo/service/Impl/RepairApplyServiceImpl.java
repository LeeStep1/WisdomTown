package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.dto.OaOrganization;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.common.enumerate.*;
import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.dao.*;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.service.RepairApplyService;
import com.bit.module.cbo.vo.*;

import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.CommonUtil;
import com.bit.utils.StringUtil;
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
 * @Date 2019/8/30 9:58
 **/
@Service("repairApplyService")
public class RepairApplyServiceImpl extends BaseService implements RepairApplyService {
	@Autowired
	private RepairApplyDao repairApplyDao;
	@Autowired
	private ResidentDao residentDao;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private RepairApplyProgressDao repairApplyProgressDao;
	@Autowired
	private ResidentRelLocationDao residentRelLocationDao;
	@Autowired
	private FileServiceFeign fileServiceFeign;
	@Autowired
	private PmcStaffRelCommunityDao pmcStaffRelCommunityDao;
	@Autowired
	private SendMqPushUtil sendMqPushUtil;
	/**
	 * app端故障报修
	 * @param repairApply
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo appInsert(RepairApply repairApply) {
		Long residentId = getCurrentUserInfo().getId();
		Resident residentById = residentDao.getResidentById(residentId);
		repairApply.setResidentId(residentId);
		repairApply.setResidentName(residentById.getRealName());
		repairApply.setResidentMobile(residentById.getMobile());

		Long communityId = repairApply.getCommunityId();
		Community communityById = communityDao.getCommunityById(communityId);
		repairApply.setOrgId(communityById.getOrgId());
		//调用feign查询社区名称
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
		String orgName = commonUtil.getOrgName(communityById.getOrgId(), oaOrganizationList);
		repairApply.setOrgName(orgName);
		repairApply.setCommunityName(communityById.getCommunityName());
		repairApply.setApplyStatus(RepairApplyStatusEnum.REPAIR_APPLY_STATUS_NOT_HANDLE.getCode());
		repairApply.setDataStatus(RepairDataStatusEnum.REPAIR_DATA_STATUS_PMC_NOT_HANDLE.getCode());
		repairApply.setCreateTime(new Date());
		repairApply.setVersion(Const.INIT_VERSION);

		int add = repairApplyDao.add(repairApply);
		if (add<=0){
			throwBusinessException("添加报修失败");
		}

		Long userId = getCurrentUserInfo().getId();
		String userName = getCurrentUserInfo().getRealName();
		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();
		//新增进度记录
		RepairApplyProgress progress = new RepairApplyProgress();
		progress.setRepairApplyId(repairApply.getId());
		progress.setUserId(userId);
		progress.setUserName(userName);
		progress.setUserType(userType);
		progress.setOperationTime(new Date());
		progress.setOperationName(OpearationTypeEnum.OPEARATION_TYPE_RESIDENT_SUBMIT.getInfo());
		progress.setOperationType(OpearationTypeEnum.OPEARATION_TYPE_RESIDENT_SUBMIT.getCode());
		int progressadd = repairApplyProgressDao.add(progress);
		if (progressadd<=0){
			throwBusinessException("新增记录失败");
		}
		//组装待办
		//查询物业管理员和物业维修人员
		Long[] targetId = commonUtil.queryPmcStaffByCommunityId(repairApply.getCommunityId());
		if (targetId!=null && targetId.length>0){
			String[] params = {"居民报修",repairApply.getCommunityName()};
			//组装mq
			MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserTaskByAlias(MessageTemplateEnum.CBO_TASK_RESIDENT_REPAIR_APPLY_RETURN,
					targetId,
					params,
					repairApply.getId(),
					repairApply.getVersion(),
					repairApply.getResidentName(),
					null,
					null);
			sendMqPushUtil.sendMqMessage(mqSendMessage);
		}
		return successVo();
	}
	/**
	 * 取消报修
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo cancel(RepairApplyVO repairApplyVO) {
		Long recordId = repairApplyVO.getId();
		RepairApply requireApplyById = repairApplyDao.getRequireApplyById(recordId);
		if (requireApplyById==null){
			throwBusinessException("报修记录不存在");
		}
		if (!repairApplyVO.getVersion().equals(requireApplyById.getVersion())){
			throwBusinessException("该记录已被处理");
		}
		if (requireApplyById.getApplyStatus()>RepairApplyStatusEnum.REPAIR_APPLY_STATUS_NOT_HANDLE.getCode()){
			throwBusinessException("只有待处理状态的才能取消");
		}

		Long userId = getCurrentUserInfo().getId();
		String userName = getCurrentUserInfo().getRealName();
		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();

		RepairApply repairApply = new RepairApply();
		repairApply.setId(recordId);
		repairApply.setApplyStatus(RepairApplyStatusEnum.REPAIR_APPLY_STATUS_CANCELED.getCode());
		repairApply.setDataStatus(RepairDataStatusEnum.REPAIR_DATA_STATUS_RESIDENT_CANCELED.getCode());
		repairApply.setVersion(requireApplyById.getVersion());
		repairApply.setUpdateUserId(userId);
		repairApply.setUpdateTime(new Date());
		repairApply.setUpdateUserType(CboUserTypeEnum.getValueByCode(userType));
		int update = repairApplyDao.update(repairApply);
		if (update<=0){
			throwBusinessException("取消报修失败");
		}
		//新增进度记录
		RepairApplyProgress progress = new RepairApplyProgress();
		progress.setRepairApplyId(recordId);
		progress.setUserId(userId);
		progress.setUserName(userName);
		progress.setUserType(userType);
		progress.setOperationTime(new Date());
		progress.setOperationName(OpearationTypeEnum.OPEARATION_TYPE_RESIDENT_CANCEL.getInfo());
		progress.setOperationType(OpearationTypeEnum.OPEARATION_TYPE_RESIDENT_CANCEL.getCode());
		int add = repairApplyProgressDao.add(progress);
		if (add<=0){
			throwBusinessException("新增记录失败");
		}
		return successVo();
	}
	/**
	 * 返显或单查记录
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo reflectById(Long id) {
		RepairApply requireApplyById = repairApplyDao.getRequireApplyById(id);
		if (requireApplyById==null){
			throwBusinessException("报修记录不存在");
		}
		RepairApplyVO repairApplyVO = new RepairApplyVO();
		BeanUtils.copyProperties(requireApplyById,repairApplyVO);
		RepairApplyAndProgressVO repairApplyAndProgressVO = new RepairApplyAndProgressVO();
		repairApplyVO.setTroubleTypeDesc(TroubleTypeEnum.getValueByCode(repairApplyVO.getTroubleType()));
		//查询处理记录
		RepairApplyProgress temp = new RepairApplyProgress();
		temp.setRepairApplyId(id);
		List<RepairApplyProgress> byParam = repairApplyProgressDao.findByParam(temp);
		if (CollectionUtils.isNotEmpty(byParam)){
			repairApplyAndProgressVO.setRepairApplyProgressList(byParam);
			for (RepairApplyProgress progress : byParam) {
				if (progress.getOperationType().equals(OpearationTypeEnum.OPEARATION_TYPE_ORG_REJECT.getCode()) ||
						progress.getOperationType().equals(OpearationTypeEnum.OPEARATION_TYPE_PMC_REJECT.getCode())){
					repairApplyVO.setReason(progress.getReason());
				}
			}
		}
		repairApplyAndProgressVO.setRepairApplyVO(repairApplyVO);
		repairApplyAndProgressVO.setVersion(repairApplyVO.getVersion());
		//查房屋信息 仅限故障发生的小区
		List<String> addressNames = new ArrayList<>();
		List<ResidentRelLocationVO> addressByResidentId = residentRelLocationDao.getAddressByResidentId(requireApplyById.getResidentId(),requireApplyById.getCommunityId());
		if (CollectionUtils.isNotEmpty(addressByResidentId)){
			for (ResidentRelLocationVO residentRelLocationVO : addressByResidentId) {
				addressNames.add(commonUtil.ignoreFloorAddressName(residentRelLocationVO.getAddressFullName()));
			}
			repairApplyAndProgressVO.setAddressNames(addressNames);
		}

		String attchedIds = requireApplyById.getAttchedIds();
		if (StringUtil.isNotEmpty(attchedIds)){
			List<Long> photoIds = Arrays.asList(attchedIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFileIds(photoIds);
			BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
			if (byIds.getData()!=null){
				String s = JSON.toJSONString(byIds.getData());
				List<FileInfo> fileInfos = JSONArray.parseArray(s,FileInfo.class);
				repairApplyAndProgressVO.setFileInfos(fileInfos);
			}
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(repairApplyAndProgressVO);
		return baseVo;
	}
	/**
	 * app三端查询报修
	 * @param repairApplyPageVO
	 * @return
	 */
	@Override
	public BaseVo appRecordListPage(RepairApplyPageVO repairApplyPageVO) {
		List<RepairApplyVO> repairApplyVOS = new ArrayList<>();
		List<Long> communityIds = new ArrayList<>();
		Integer tid = getCurrentUserInfo().getTid();
		if (tid.equals(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid())){
			//如果是社区
			if (repairApplyPageVO.getCommunityId()==null){
				Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
				List<Community> communityByOrgId = communityDao.getCommunityByOrgId(orgId);
				if (CollectionUtils.isNotEmpty(communityByOrgId)){
					for (Community community : communityByOrgId) {
						communityIds.add(community.getId());
					}
					repairApplyPageVO.setCommunityIds(communityIds);
				}
			}
		}
		else if (tid.equals(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid())){
			//如果是物业 从token里拿小区信息
			if (repairApplyPageVO.getCommunityId()==null){
				List<com.bit.base.dto.Community> communities = getCurrentUserInfo().getCboInfo().getCommunities();
				if (CollectionUtils.isNotEmpty(communities)){
					for (com.bit.base.dto.Community community : communities) {
						communityIds.add(community.getId());
					}
					repairApplyPageVO.setCommunityIds(communityIds);
				}
			}
		}
		else if (tid.equals(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid())){
			//如果是居民端 查询自己的报修
			Long residentId = getCurrentUserInfo().getId();
			repairApplyPageVO.setResidentId(residentId);
		}
		PageHelper.startPage(repairApplyPageVO.getPageNum(),repairApplyPageVO.getPageSize());
		List<RepairApply> repairApplies = repairApplyDao.appRecordListPage(repairApplyPageVO);
		if (CollectionUtils.isNotEmpty(repairApplies)){
			for (RepairApply repairApply : repairApplies) {
				RepairApplyVO repairApplyVO = new RepairApplyVO();
				BeanUtils.copyProperties(repairApply,repairApplyVO);
				repairApplyVO.setTroubleTypeDesc(TroubleTypeEnum.getValueByCode(repairApply.getTroubleType()));
				repairApplyVOS.add(repairApplyVO);
			}
		}
		PageInfo<RepairApplyVO> pageInfo = new PageInfo<>(repairApplyVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 报修记录处理
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo applyHandle(RepairApplyVO repairApplyVO) {
		Long recordId = repairApplyVO.getId();
		RepairApply requireApplyById = repairApplyDao.getRequireApplyById(recordId);
		if (requireApplyById==null){
			throwBusinessException("报修记录不存在");
		}
		if (!repairApplyVO.getVersion().equals(requireApplyById.getVersion())){
			throwBusinessException("该记录已被处理");
		}
		Integer tid = getCurrentUserInfo().getTid();
		if (tid.equals(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid())){

			throwBusinessException("非法的接入端");
		}
		//处理人id
		Long userId = getCurrentUserInfo().getId();
		String userName = getCurrentUserInfo().getRealName();
		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();
		//报修记录实体
		RepairApply repairApply = new RepairApply();
		BeanUtils.copyProperties(repairApplyVO,repairApply);

		//报修进度实体
		RepairApplyProgress progress = new RepairApplyProgress();
		progress.setRepairApplyId(recordId);
		progress.setUserType(userType);
		progress.setUserId(userId);
		progress.setUserName(userName);
		progress.setOperationTime(new Date());
		progress.setReason(repairApplyVO.getReason());

		if (tid.equals(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid())){
			//物业端
			//分状态 处理 , 拒绝 , 转交居委会
			if (repairApplyVO.getDataStatus().equals(RepairDataStatusEnum.REPAIR_DATA_STATUS_PMC_HANDLEING.getCode())){
				//物业受理 处理中
				repairApply.setApplyStatus(RepairApplyStatusEnum.REPAIR_APPLY_STATUS_HANDLEING.getCode());
				progress.setOperationType(OpearationTypeEnum.OPEARATION_TYPE_PMC_CONFIRM.getCode());
				progress.setOperationName(OpearationTypeEnum.OPEARATION_TYPE_PMC_CONFIRM.getInfo());
			}
			if (repairApply.getDataStatus().equals(RepairDataStatusEnum.REPAIR_DATA_STATUS_PMC_REJECTED.getCode())){
				//物业拒绝 已拒绝
				repairApply.setApplyStatus(RepairApplyStatusEnum.REPAIR_APPLY_STATUS_REJECTED.getCode());
				progress.setOperationType(OpearationTypeEnum.OPEARATION_TYPE_PMC_REJECT.getCode());
				progress.setOperationName(OpearationTypeEnum.OPEARATION_TYPE_PMC_REJECT.getInfo());
				//组装消息
				Long[] targetId = {requireApplyById.getResidentId()};
				if (targetId!=null && targetId.length>0){
					String[] params = {"故障报修",repairApplyVO.getReason()};
					//组装mq
					MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(MessageTemplateEnum.CBO_REMIND_RESIDENT_REPAIR_REFUSE,
							targetId,
							params,
							null,
							null,
							null);
					//mqPushMessage.assembleMqMessage(mqSendMessage);
					sendMqPushUtil.sendMqMessage(mqSendMessage);
				}
			}
			if (repairApply.getDataStatus().equals(RepairDataStatusEnum.REPAIR_DATA_STATUS_ORG_NOT_HANDLE.getCode())){
				//转交居委会 处理中
				repairApply.setApplyStatus(RepairApplyStatusEnum.REPAIR_APPLY_STATUS_HANDLEING.getCode());
				progress.setOperationType(OpearationTypeEnum.OPEARATION_TYPE_PMC_TRANSFER_TO_ORG.getCode());
				progress.setOperationName(OpearationTypeEnum.OPEARATION_TYPE_PMC_TRANSFER_TO_ORG.getInfo());
				//组装待办
				//查询 居委会管理员
				Long[] targetId = commonUtil.queryOrgAdminByOrgId(requireApplyById.getOrgId());
				if (targetId!=null && targetId.length>0){
					String[] params = {"故障报修",requireApplyById.getCommunityName()};
					//组装mq
					MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserTaskByAlias(MessageTemplateEnum.CBO_TASK_RESIDENT_REPAIR_APPLY_TRANSFER,
							targetId,
							params,
							repairApply.getId(),
							repairApply.getVersion()+1,
							requireApplyById.getResidentName(),
							null,
							null);
					//mqPushMessage.assembleMqMessage(mqSendMessage);
					sendMqPushUtil.sendMqMessage(mqSendMessage);
				}
			}
			if (repairApply.getDataStatus().equals(RepairDataStatusEnum.REPAIR_DATA_STATUS_PMC_FINISHED.getCode())){
				//物业 完结
				repairApply.setApplyStatus(RepairApplyStatusEnum.REPAIR_APPLY_STATUS_FINISHED.getCode());
				progress.setOperationType(OpearationTypeEnum.OPEARATION_TYPE_PMC_FINISH.getCode());
				progress.setOperationName(OpearationTypeEnum.OPEARATION_TYPE_PMC_FINISH.getInfo());
				//组装消息
				Long[] targetId = {requireApplyById.getResidentId()};
				if (targetId!=null && targetId.length>0){
					String[] params = {"故障报修"};
					//组装mq
					MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(MessageTemplateEnum.CBO_REMIND_RESIDENT_REPAIR_PROCESSING_COMPLETION,
							targetId,
							params,
							null,
							null,
							null);
					//mqPushMessage.assembleMqMessage(mqSendMessage);
					sendMqPushUtil.sendMqMessage(mqSendMessage);
				}
			}
		}

		else if (tid.equals(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid()) || tid.equals(TerminalTypeEnum.TERMINALTOWEB.getTid())){

			//居委会端 or web端
			//分状态 处理 , 拒绝 , 转交物业
			if (repairApplyVO.getDataStatus().equals(RepairDataStatusEnum.REPAIR_DATA_STATUS_ORG_HANDLEING.getCode())){
				//居委会受理 处理中
				repairApply.setApplyStatus(RepairApplyStatusEnum.REPAIR_APPLY_STATUS_HANDLEING.getCode());
				progress.setOperationType(OpearationTypeEnum.OPEARATION_TYPE_ORG_CONFIRM.getCode());
				progress.setOperationName(OpearationTypeEnum.OPEARATION_TYPE_ORG_CONFIRM.getInfo());
			}
			if (repairApply.getDataStatus().equals(RepairDataStatusEnum.REPAIR_DATA_STATUS_ORG_REJECTED.getCode())){
				//居委会拒绝 已拒绝
				repairApply.setApplyStatus(RepairApplyStatusEnum.REPAIR_APPLY_STATUS_REJECTED.getCode());
				progress.setOperationType(OpearationTypeEnum.OPEARATION_TYPE_ORG_REJECT.getCode());
				progress.setOperationName(OpearationTypeEnum.OPEARATION_TYPE_ORG_REJECT.getInfo());
				//组装消息
				Long[] targetId = {requireApplyById.getResidentId()};
				if (targetId!=null && targetId.length>0){
					String[] params = {"故障报修",repairApplyVO.getReason()};
					//组装mq
					MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(MessageTemplateEnum.CBO_REMIND_RESIDENT_REPAIR_REFUSE,
							targetId,
							params,
							null,
							null,
							null);
					//mqPushMessage.assembleMqMessage(mqSendMessage);
					sendMqPushUtil.sendMqMessage(mqSendMessage);
				}
			}
			if (repairApply.getDataStatus().equals(RepairDataStatusEnum.REPAIR_DATA_STATUS_PMC_NOT_HANDLE.getCode())){
				//转交物业 处理中
				repairApply.setApplyStatus(RepairApplyStatusEnum.REPAIR_APPLY_STATUS_HANDLEING.getCode());
				progress.setOperationType(OpearationTypeEnum.OPEARATION_TYPE_ORG_TRANSFER_TO_PMC.getCode());
				progress.setOperationName(OpearationTypeEnum.OPEARATION_TYPE_ORG_TRANSFER_TO_PMC.getInfo());
				//组装待办
				//查询物业管理员和维修人员
				Long[] targetId = commonUtil.queryPmcStaffByCommunityId(requireApplyById.getCommunityId());
				if (targetId!=null && targetId.length>0){
					String[] params = {"故障报修",requireApplyById.getCommunityName()};
					//组装mq
					MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserTaskByAlias(MessageTemplateEnum.CBO_TASK_RESIDENT_REPAIR_APPLY_RETURN,
							targetId,
							params,
							repairApply.getId(),
							repairApply.getVersion()+1,
							requireApplyById.getResidentName(),
							null,
							null);
					//mqPushMessage.assembleMqMessage(mqSendMessage);
					sendMqPushUtil.sendMqMessage(mqSendMessage);
				}
			}
			if (repairApplyVO.getDataStatus().equals(RepairDataStatusEnum.REPAIR_DATA_STATUS_ORG_FINISHED.getCode())){
				//居委会完结 已完结
				repairApply.setApplyStatus(RepairApplyStatusEnum.REPAIR_APPLY_STATUS_FINISHED.getCode());
				progress.setOperationType(OpearationTypeEnum.OPEARATION_TYPE_ORG_FINISH.getCode());
				progress.setOperationName(OpearationTypeEnum.OPEARATION_TYPE_ORG_FINISH.getInfo());
				//组装消息
				Long[] targetId = {requireApplyById.getResidentId()};
				if (targetId!=null && targetId.length>0){
					String[] params = {"故障报修"};
					//组装mq
					MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(MessageTemplateEnum.CBO_REMIND_RESIDENT_REPAIR_PROCESSING_COMPLETION,
							targetId,
							params,
							null,
							null,
							null);
					//mqPushMessage.assembleMqMessage(mqSendMessage);
					sendMqPushUtil.sendMqMessage(mqSendMessage);
				}
			}
		}

		repairApply.setUpdateTime(new Date());
		repairApply.setUpdateUserId(userId);
		repairApply.setUpdateUserType(CboUserTypeEnum.getValueByCode(userType));
		//更新报修状态
		int update = repairApplyDao.update(repairApply);
		if (update<=0){
			throwBusinessException("更新报修状态失败");
		}
		//新增报修处理进度
		int add = repairApplyProgressDao.add(progress);
		if (add<=0){
			throwBusinessException("报修进度新增失败");
		}
		return successVo();
	}



	/**
	 * web 端故障申报分页查询
	 * @param repairApplyPageVO
	 * @return
	 */
	@Override
	public BaseVo webListPage(RepairApplyPageVO repairApplyPageVO) {
		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();
		List<Long> communityIds = new ArrayList<>();
		//如果是社区办
		if (userType.equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			if (repairApplyPageVO.getOrgId()!=null){
				if (repairApplyPageVO.getCommunityId()!=null){
					communityIds.add(repairApplyPageVO.getCommunityId());
				}else {
					List<Community> communityByOrgId = communityDao.getCommunityByOrgId(repairApplyPageVO.getOrgId());
					if (CollectionUtils.isNotEmpty(communityByOrgId)){
						for (Community community : communityByOrgId) {
							communityIds.add(community.getId());
						}
					}
				}
			}else {
				List<Community> allCommunity = communityDao.getAllCommunity();
				if (CollectionUtils.isNotEmpty(allCommunity)){
					for (Community community : allCommunity) {
						communityIds.add(community.getId());
					}
				}
			}
		}else {
			//是社区
			if (repairApplyPageVO.getCommunityId()!=null){
				communityIds.add(repairApplyPageVO.getCommunityId());
			}else {
				Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
				List<Community> communityByOrgId = communityDao.getCommunityByOrgId(orgId);
				if (CollectionUtils.isNotEmpty(communityByOrgId)){
					for (Community community : communityByOrgId) {
						communityIds.add(community.getId());
					}
				}
			}
		}
		//如果是空集合 就弄个空的进去
		if (CollectionUtils.isEmpty(communityIds)){
			communityIds.add(null);
		}
		repairApplyPageVO.setCommunityIds(communityIds);
		PageHelper.startPage(repairApplyPageVO.getPageNum(),repairApplyPageVO.getPageSize());
		List<RepairApplyVO> repairApplies = repairApplyDao.webListPage(repairApplyPageVO);
		if (CollectionUtils.isNotEmpty(repairApplies)){
			for (RepairApplyVO repairApply : repairApplies) {
				repairApply.setTroubleTypeDesc(TroubleTypeEnum.getValueByCode(repairApply.getTroubleType()));
			}
		}


		PageInfo<RepairApplyVO> pageInfo = new PageInfo<>(repairApplies);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 根据token 查询小区信息
	 * @return
	 */
	@Override
	public BaseVo getCommunityByToken() {
		BaseVo baseVo = new BaseVo();
		Integer tid = getCurrentUserInfo().getTid();
		if (tid.equals(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid())){
			Long pmcId = getCurrentUserInfo().getId();
			PmcStaffRelCommunity temp = new PmcStaffRelCommunity();
			temp.setStaffId(pmcId);
			List<PmcStaffRelCommunity> byParam = pmcStaffRelCommunityDao.findByParam(temp);
			List<Long> communityIds = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(byParam)){
				for (PmcStaffRelCommunity pmcStaffRelCommunity : byParam) {
					communityIds.add(pmcStaffRelCommunity.getCommunityId());
				}
				List<Community> communities = communityDao.batchSelectByIds(communityIds);
				baseVo.setData(communities);
			}
		}
		else if (tid.equals(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid())){
			OaOrganization currentCboOrg = getCurrentUserInfo().getCboInfo().getCurrentCboOrg();
			Long orgId = currentCboOrg.getId();
			List<Community> communityByOrgId = communityDao.getCommunityByOrgId(orgId);
			baseVo.setData(communityByOrgId);
		}
		return baseVo;
	}
}
