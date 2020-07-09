package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.OaOrganization;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.module.cbo.bean.Community;
import com.bit.module.cbo.bean.Resident;
import com.bit.module.cbo.bean.ResidentSuggestion;
import com.bit.module.cbo.dao.CommunityDao;
import com.bit.module.cbo.dao.ResidentDao;
import com.bit.module.cbo.dao.SuggestDao;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.service.SuggestionService;
import com.bit.module.cbo.vo.ResidentSuggestionVO;

import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.CommonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/6 15:23
 **/
@Service("suggestService")
public class SuggestionServiceImpl extends BaseService implements SuggestionService {

	@Autowired
	private SuggestDao suggestDao;
	@Autowired
	private SysServiceFeign sysServiceFeign;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private ResidentDao residentDao;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private SendMqPushUtil sendMqPushUtil;


	/**
	 * 新增居民意见
	 * @param residentSuggestion
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo add(ResidentSuggestion residentSuggestion) {
		Long residentId = getCurrentUserInfo().getId();
		Resident residentById = residentDao.getResidentById(residentId);
		if (residentById==null){
			throwBusinessException("该居民不存在");
		}
		//选中的小区
		Long communityId = residentSuggestion.getCommunityId();
		Community communityById = communityDao.getCommunityById(communityId);
		if (communityById==null){
			throwBusinessException("该小区不存在");
		}
		Long orgId = communityById.getOrgId();
		//所有的社区
		List<OaOrganization> oaOrganizationList = new ArrayList<>();
		BaseVo community = sysServiceFeign.getCommunity();
		if (community.getData()!=null){
			String ss = JSON.toJSONString(community.getData());
			oaOrganizationList = JSON.parseArray(ss, OaOrganization.class);
		}
		String orgName = "";
		if (CollectionUtils.isNotEmpty(oaOrganizationList)){
			for (OaOrganization oaOrganization : oaOrganizationList) {
				if (oaOrganization.getId().equals(orgId)){
					orgName = oaOrganization.getName();
					break;
				}
			}
		}

		ResidentSuggestion obj = new ResidentSuggestion();
		obj.setStatus(Const.SUGGESTION_TYPE_NOT_RESPONSE);
		obj.setOrgId(orgId);
		obj.setOrgName(orgName);
		obj.setCommunityId(communityId);
		obj.setCommunityName(communityById.getCommunityName());
		obj.setCreateTime(new Date());
		obj.setCreateResidentId(residentId);
		obj.setCreateResidentName(residentById.getRealName());
		obj.setResidentMobile(residentById.getMobile());
		obj.setTitle(residentSuggestion.getTitle());
		obj.setContent(residentSuggestion.getContent());
		obj.setVersion(Const.INIT_VERSION);
		int add = suggestDao.add(obj);
		if (add<=0){
			throwBusinessException("新增居民意见失败");
		}
		//组装待办
		//查询居委会管理员
		Long[] targetId = commonUtil.queryOrgAdminByOrgId(orgId);
		if (targetId!=null && targetId.length>0){
			String[] params = {"居民通过APP提交意见至居委会",communityById.getCommunityName()};
			//组装mq
			MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserTaskByAlias(MessageTemplateEnum.CBO_TASK_RESIDENT_LOCATION,
					targetId,
					params,
					obj.getId(),
					obj.getVersion(),
					null,
					null,
					null);
			sendMqPushUtil.sendMqMessage(mqSendMessage);
		}
		return successVo();
	}

	/**
	 * 修改居民意见
	 * @param residentSuggestion
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo modify(ResidentSuggestion residentSuggestion) {
		Long recordId = residentSuggestion.getId();
		if (recordId==null){
			throwBusinessException("记录id为空");
		}
		ResidentSuggestion suggestionById = suggestDao.getSuggestionById(recordId);
		if (suggestionById==null){
			throwBusinessException("该记录不存在");
		}
		if (!residentSuggestion.getVersion().equals(suggestionById.getVersion())){
			throwBusinessException("此条意见已反馈");
		}
		Long userId = getCurrentUserInfo().getId();
		ResidentSuggestion obj = new ResidentSuggestion();
		obj.setId(residentSuggestion.getId());
		obj.setFeedBackMsg(residentSuggestion.getFeedBackMsg());
		obj.setFeedBackUserId(userId);
		obj.setFeedBackTime(new Date());
		obj.setStatus(Const.SUGGESTION_TYPE_RESPONSED);
		obj.setVersion(residentSuggestion.getVersion());
		int modify = suggestDao.modify(obj);
		if (modify<=0){
			throwBusinessException("修改居民意见失败");
		}
		//组装消息
		Long residentId = suggestionById.getCreateResidentId();
		Long[] targetId = {residentId};
		if (targetId!=null && targetId.length>0){
			String[] params = {"居民提交意见居委会反馈后"};
			//组装mq
			MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(MessageTemplateEnum.CBO_REMIND_RESIDENT_SUGGESTION,
					targetId,
					params,
					suggestionById.getCreateResidentName(),
					null,
					null);
			sendMqPushUtil.sendMqMessage(mqSendMessage);
		}
		return successVo();
	}

	/**
	 * web端分页插查询
	 * @param residentSuggestionVO
	 * @return
	 */
	@Override
	public BaseVo webListPage(ResidentSuggestionVO residentSuggestionVO) {

		//所有小区的id
		List<Long> communityIds = new ArrayList<>();

		//所有的小区
		List<Community> allCommunity = communityDao.getAllCommunity();
		if (CollectionUtils.isNotEmpty(allCommunity)){
			for (Community community1 : allCommunity) {
				communityIds.add(community1.getId());
			}
		}
		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();
		//如果是社区办
		if (userType.equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			//如果选了社区
			if (residentSuggestionVO.getOrgId()!=null){
				//如果没选择小区
				List<Long> communityByOrgIds = new ArrayList<>();
				if (residentSuggestionVO.getCommunityId()==null){
					//根据社区id查询小区
					List<Community> communityByOrgId = communityDao.getCommunityByOrgId(residentSuggestionVO.getOrgId());

					if (CollectionUtils.isNotEmpty(communityByOrgId)){
						for (Community community1 : communityByOrgId) {
							communityByOrgIds.add(community1.getId());
						}
						residentSuggestionVO.setCommunityIds(communityByOrgIds);
					}else {
						communityByOrgIds.add(null);
						residentSuggestionVO.setCommunityIds(communityByOrgIds);
					}
				}else {
					communityByOrgIds.add(residentSuggestionVO.getCommunityId());
					residentSuggestionVO.setCommunityIds(communityByOrgIds);
				}
			}else {
				residentSuggestionVO.setCommunityIds(communityIds);
			}
		}else {
			Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
			residentSuggestionVO.setOrgId(orgId);
		}
		PageHelper.startPage(residentSuggestionVO.getPageNum(),residentSuggestionVO.getPageSize());
		List<ResidentSuggestion> residentSuggestions = suggestDao.webListPage(residentSuggestionVO);

		PageInfo<ResidentSuggestion> pageInfo = new PageInfo<>(residentSuggestions);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);

		return baseVo;
	}
	/**
	 * 返显或单查意见
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo reflectById(Long id) {
		BaseVo baseVo = new BaseVo();
		ResidentSuggestion suggestionById = suggestDao.getSuggestionById(id);
		if (suggestionById==null){
			baseVo.setCode(ResultCode.RECORD_ALREADY_DELETED.getCode());
			baseVo.setMsg(ResultCode.RECORD_ALREADY_DELETED.getInfo());
			return baseVo;
		}else {
			baseVo.setData(suggestionById);
			return baseVo;
		}
	}
	/**
	 * app 居民端 居委会端 分页查询
	 * @param residentSuggestionVO
	 * @return
	 */
	@Override
	public BaseVo appListPage(ResidentSuggestionVO residentSuggestionVO) {
		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();


		List<Long> communityIds = new ArrayList<>();
		//如果是社区
		if (userType.equals(Const.USER_TYPE_ORG)){
			Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
			//根据社区id查询小区
			List<Community> communityByOrgId = communityDao.getCommunityByOrgId(orgId);
			if (CollectionUtils.isNotEmpty(communityByOrgId)){
				for (Community community : communityByOrgId) {
					communityIds.add(community.getId());
				}
				residentSuggestionVO.setCommunityIds(communityIds);
			}
		}else if (userType.equals(Const.USER_TYPE_RESIDENT)){
//			Long communityId = getCurrentUserInfo().getCboInfo().getCurrentCommunity().getId();
			//如果是居民端
//			residentSuggestionVO.setCommunityId(communityId);
			Long residentId = getCurrentUserInfo().getId();
			residentSuggestionVO.setCreateResidentId(residentId);
		}

		PageHelper.startPage(residentSuggestionVO.getPageNum(),residentSuggestionVO.getPageSize());
		List<ResidentSuggestion> residentSuggestions = suggestDao.appListPage(residentSuggestionVO);

		PageInfo<ResidentSuggestion> pageInfo = new PageInfo<>(residentSuggestions);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);

		return baseVo;
	}
	/**
	 * 删除id
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo delById(Long id) {
		ResidentSuggestion suggestionById = suggestDao.getSuggestionById(id);
		if (suggestionById==null){
			throwBusinessException("此条意见已删除");
		}
		int i = suggestDao.delById(id);
		if (i<=0){
			throwBusinessException("删除意见失败");
		}

		return successVo();
	}

}
