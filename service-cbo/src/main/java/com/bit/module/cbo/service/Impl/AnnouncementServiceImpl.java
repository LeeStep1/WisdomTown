package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.OaOrganization;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.module.cbo.bean.Announcement;
import com.bit.module.cbo.bean.PmcStaffRelCommunity;
import com.bit.module.cbo.bean.ResidentRelLocation;
import com.bit.module.cbo.dao.AnnouncementDao;
import com.bit.module.cbo.dao.PmcStaffRelCommunityDao;
import com.bit.module.cbo.dao.ResidentRelLocationDao;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.service.AnnouncementService;
import com.bit.module.cbo.vo.AnnouncementPageVO;
import com.bit.module.cbo.vo.AnnouncementVO;
import com.bit.module.cbo.vo.FileInfo;

import com.bit.soft.push.msEnum.AppPushTagAliasEnum;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.CommonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/7 13:49
 **/
@Service("announcementService")
public  class  AnnouncementServiceImpl extends BaseService implements AnnouncementService {

	@Autowired
	private AnnouncementDao announcementDao;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private FileServiceFeign fileServiceFeign;
	@Autowired
	private PmcStaffRelCommunityDao pmcStaffRelCommunityDao;
	@Autowired
	private SendMqPushUtil sendMqPushUtil;

	/**
	 * 新增社区公告
	 * @param announcement
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo add(Announcement announcement) {
		Long userId = getCurrentUserInfo().getId();
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		announcement.setCreateTime(new Date());
		announcement.setCreateUserId(userId);
		announcement.setOrgId(orgId);
		if (announcement.getStatus().equals(Const.ORG_ANNOUNCEMENT_STATUS_RELEASED)){
			announcement.setPublishTime(new Date());
			announcement.setPublishUserId(userId);
		}
		int add = announcementDao.add(announcement);
		if (add<=0){
			throwBusinessException("添加社区公告失败");
		}
		//如果直接发送 就要发推送
		if (announcement.getStatus().equals(Const.ORG_ANNOUNCEMENT_STATUS_RELEASED)){
			sendAnnouncementMq(announcement,orgId);
		}
		return successVo();
	}

	/**
	 * 发送社区公告推送
	 * @param announcement
	 * @param orgId
	 */
	private void sendAnnouncementMq(Announcement announcement,Long orgId){
		//查询社区名称
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
		String orgName = commonUtil.getOrgName(orgId, oaOrganizationList);
		String[] params = {announcement.getTitle(),orgName};
		//组装mq
		//将社区的id放入targ中
		List<String> targ = new ArrayList<>();
		targ.add(AppPushTagAliasEnum.CBO_TAGPUSH_ORG.getPsuhTag(orgId.toString()));
		MqSendMessage mqSendMessage = AppPushMessageUtil.pushMessageByTag(MessageTemplateEnum.CBO_REMIND_ANNOUNCEMENT_ORG_ADMIN,
				targ,
				params,
				null,
				null,
				null);
		sendMqPushUtil.sendMqMessage(mqSendMessage);
	}

	/**
	 * 修改社区公告
	 * @param announcement
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo modify(Announcement announcement) {
		Long id = announcement.getId();
		Announcement announcementById = announcementDao.getAnnouncementById(id);
		if (announcementById==null){
			throwBusinessException("该记录不存在");
		}
		if (!announcementById.getStatus().equals(Const.ORG_ANNOUNCEMENT_STATUS_DRAFT)){
			throwBusinessException("只有草稿状态可以编辑");
		}
		//如果不发布
		if (announcement.getStatus().equals(Const.ORG_ANNOUNCEMENT_STATUS_DRAFT)){
			int modify = announcementDao.modify(announcement);
			if (modify<=0){
				throwBusinessException("修改社区公告失败");
			}
		}
		//如果发布
		if (announcement.getStatus().equals(Const.ORG_ANNOUNCEMENT_STATUS_RELEASED)){
			announcement.setPublishTime(new Date());
			Long userId = getCurrentUserInfo().getId();
			announcement.setPublishUserId(userId);
			int modify = announcementDao.modify(announcement);
			if (modify<=0){
				throwBusinessException("修改社区公告失败");
			}
			sendAnnouncementMq(announcement,announcementById.getOrgId());
		}


		return successVo();
	}

	/**
	 * web端分页查询
	 * @param announcementPageVO
	 * @return
	 */
	@Override
	public BaseVo webListPage(AnnouncementPageVO announcementPageVO) {

		//所有的社区
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();

		List<Long> orgIds = new ArrayList<>();

		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();
		if (userType.equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			//如果是社区办
			announcementPageVO.setStatus(Const.PMC_ANNOUNCEMENT_STATUS_RELEASED);
			if (announcementPageVO.getOrgId()==null){
				if (CollectionUtils.isNotEmpty(oaOrganizationList)){
					for (OaOrganization oaOrganization : oaOrganizationList) {
						orgIds.add(oaOrganization.getId());
					}
				}
			}else {
				orgIds.add(announcementPageVO.getOrgId());
			}
			announcementPageVO.setOrgIds(orgIds);
			announcementPageVO.setFlag(Const.FLAG_SHEQUBAN_YES);
		}else {
			//如果是居委会
			//当前社区id
			Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
			announcementPageVO.setOrgId(orgId);
			announcementPageVO.setFlag(Const.FLAG_SHEQUBAN_NO);
		}
		PageHelper.startPage(announcementPageVO.getPageNum(),announcementPageVO.getPageSize());
		List<AnnouncementVO> announcementVOS = announcementDao.webListPage(announcementPageVO);
		for (AnnouncementVO announcementVO : announcementVOS) {
			announcementVO.setOrgName(commonUtil.getOrgName(announcementVO.getOrgId(),oaOrganizationList));
			if (announcementVO.getStatus().equals(Const.ORG_ANNOUNCEMENT_STATUS_DRAFT)){
				announcementVO.setPublishTime(null);
			}
		}
		PageInfo<AnnouncementVO> pageInfo = new PageInfo<>(announcementVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * app端分页查询
	 * @param announcementPageVO
	 * @return
	 */
	@Override
	public BaseVo appListPage(AnnouncementPageVO announcementPageVO,HttpServletRequest request) {
		//所有的社区
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();

		List<Long> orgIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(oaOrganizationList)){
			for (OaOrganization oaOrganization : oaOrganizationList) {
				orgIds.add(oaOrganization.getId());
			}
		}
		//这里要区分接入端
		String tid = request.getHeader("tid");

		if (Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid())){

			//如果是居民端
			Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
			announcementPageVO.setOrgId(orgId);

		}else if (Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid())){

			//如果是物业端
			Long userId = getCurrentUserInfo().getId();
			PmcStaffRelCommunity pmcStaffRelCommunity = new PmcStaffRelCommunity();
			pmcStaffRelCommunity.setStaffId(userId);
			List<PmcStaffRelCommunity> byParam = pmcStaffRelCommunityDao.findByParam(pmcStaffRelCommunity);

			List<Long> staffOrgIds = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(byParam)){
				for (PmcStaffRelCommunity staffRelCommunity : byParam) {
					staffOrgIds.add(staffRelCommunity.getOrgId());
				}
			}
			//去重
			staffOrgIds = staffOrgIds.stream().distinct().collect(Collectors.toList());
			announcementPageVO.setOrgIds(staffOrgIds);

		}else if (Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid())){

			//如果是居委会
			Integer userType = getCurrentUserInfo().getCboInfo().getUserType();
			if (userType.equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
				//如果是社区办
				announcementPageVO.setStatus(Const.PMC_ANNOUNCEMENT_STATUS_RELEASED);
				announcementPageVO.setOrgIds(orgIds);
			}else {
				//如果是居委会
				//当前社区id
				Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
				announcementPageVO.setOrgId(orgId);
			}
		}else {
			throwBusinessException("非法的接入端");
		}



		PageHelper.startPage(announcementPageVO.getPageNum(),announcementPageVO.getPageSize());
		List<AnnouncementVO> announcementVOS = announcementDao.appListPage(announcementPageVO);
		for (AnnouncementVO announcementVO : announcementVOS) {
			BaseVo byId = fileServiceFeign.findById(announcementVO.getPic());
			if (byId.getData()!=null){
				String s = JSON.toJSONString(byId.getData());
				FileInfo fileInfo = JSON.parseObject(s,FileInfo.class);
				announcementVO.setPicFileInfo(fileInfo);
			}
		}
		PageInfo<AnnouncementVO> pageInfo = new PageInfo<>(announcementVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 返显或单查记录
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo reflectById(Long id) {
		Announcement announcementById = announcementDao.getAnnouncementById(id);
		if (announcementById==null){
			throwBusinessException("该记录不存在");
		}
		AnnouncementVO announcementVO = new AnnouncementVO();
		BeanUtils.copyProperties(announcementById,announcementVO);
		BaseVo byId = fileServiceFeign.findById(announcementById.getPic());
		if (byId.getData()!=null){
			String s = JSON.toJSONString(byId.getData());
			FileInfo fileInfo = JSON.parseObject(s,FileInfo.class);
			announcementVO.setPicFileInfo(fileInfo);
		}
		BaseVo baseVo = new BaseVo();
		baseVo.setData(announcementVO);
		return baseVo;
	}
	/**
	 * 删除社区公告
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo delById(Long id) {
		Announcement announcementById = announcementDao.getAnnouncementById(id);
		if (announcementById==null){
			throwBusinessException("该记录已删除");
		}
		int i = announcementDao.delById(id);
		if (i<=0){
			throwBusinessException("删除通知失败");
		}
		return successVo();
	}
}
