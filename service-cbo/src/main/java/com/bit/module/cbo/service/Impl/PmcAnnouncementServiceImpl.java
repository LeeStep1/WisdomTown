package com.bit.module.cbo.service.Impl;

import com.bit.base.dto.OaOrganization;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.module.cbo.bean.Community;
import com.bit.module.cbo.bean.PmcAnnouncement;
import com.bit.module.cbo.bean.PmcStaffRelCommunity;
import com.bit.module.cbo.dao.CommunityDao;
import com.bit.module.cbo.dao.PmcAnnouncementDao;
import com.bit.module.cbo.dao.PmcStaffRelCommunityDao;
import com.bit.module.cbo.vo.PmcAnnouncementPageVO;
import com.bit.module.cbo.service.PmcAnnouncementService;
import com.bit.module.cbo.vo.PmcAnnouncementVO;

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

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/5 10:22
 **/
@Service("pmcAnnouncementService")
public class PmcAnnouncementServiceImpl extends BaseService implements PmcAnnouncementService {

	@Autowired
	private PmcAnnouncementDao pmcAnnouncementDao;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private PmcStaffRelCommunityDao pmcStaffRelCommunityDao;

	@Autowired
	private SendMqPushUtil sendMqPushUtil;

	/**
	 * 新增物业公告
	 * @param pmcAnnouncementVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo appInsert(PmcAnnouncementVO pmcAnnouncementVO,HttpServletRequest request) {
		String tid = request.getHeader("tid");
		if (!Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid())){

			throwBusinessException("接入端错误");
		}
		Integer identity = getCurrentUserInfo().getCboInfo().getIdentity();
		if (!identity.equals(Const.IDENTITY_PMC_ADMIN)){
			throwBusinessException("不是管理员权限");
		}

		Long userId = getCurrentUserInfo().getId();
		PmcAnnouncement pmcAnnouncement = new PmcAnnouncement();
		pmcAnnouncement.setCreateTime(new Date());
		pmcAnnouncement.setCreatePmcUserId(userId);
		if (pmcAnnouncementVO.getStatus().equals(Const.PMC_ANNOUNCEMENT_STATUS_RELEASED)){
			pmcAnnouncement.setPublishPmcUserId(userId);
			pmcAnnouncement.setPublishTime(new Date());
		}
		pmcAnnouncement.setTitle(pmcAnnouncementVO.getTitle());
		pmcAnnouncement.setContent(pmcAnnouncementVO.getContent());
		pmcAnnouncement.setAuthorName(pmcAnnouncementVO.getAuthorName());
		pmcAnnouncement.setStatus(pmcAnnouncementVO.getStatus());
		Community communityById = communityDao.getCommunityById(pmcAnnouncementVO.getCommunityId());
		if (communityById!=null){
			pmcAnnouncement.setOrgId(communityById.getOrgId());
			pmcAnnouncement.setCommunityId(pmcAnnouncementVO.getCommunityId());
		}

		int add = pmcAnnouncementDao.add(pmcAnnouncement);
		if (add<=0){
			throwBusinessException("物业公告新增失败");
		}
		//如果是发布状态 就要推送
		if (pmcAnnouncementVO.getStatus().equals(Const.PMC_ANNOUNCEMENT_STATUS_RELEASED)){
			sendPmcAnnouncementMq(pmcAnnouncement);
		}
		return successVo();
	}

	/**
	 * 发送物业公告
	 * @param pmcAnnouncement
	 */
	private void sendPmcAnnouncementMq(PmcAnnouncement pmcAnnouncement){
		Community communityById = communityDao.getCommunityById(pmcAnnouncement.getCommunityId());
		String[] params = {pmcAnnouncement.getTitle(),communityById.getCommunityName()};
		//组装mq
		//将社区的id放入targ中
		List<String> targ = new ArrayList<>();
		targ.add(AppPushTagAliasEnum.CBO_TAGPUSH_COMMUNITY.getPsuhTag(pmcAnnouncement.getCommunityId().toString()));
		MqSendMessage mqSendMessage = AppPushMessageUtil.pushMessageByTag(MessageTemplateEnum.CBO_REMIND_ANNOUNCEMENT_PMC,
				targ,
				params,
				null,
				null,
				null);
		//mqPushMessage.assembleMqMessage(mqSendMessage);
		sendMqPushUtil.sendMqMessage(mqSendMessage);
	}

	/**
	 * 编辑物业公告
	 * @param pmcAnnouncementVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo appEdit(PmcAnnouncementVO pmcAnnouncementVO, HttpServletRequest request) {
		Long userId = getCurrentUserInfo().getId();
		String tid = request.getHeader("tid");
		if (!Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid())){
			throwBusinessException("接入端错误");
		}
		Integer identity = getCurrentUserInfo().getCboInfo().getIdentity();
		if (!identity.equals(Const.IDENTITY_PMC_ADMIN)){
			throwBusinessException("不是管理员权限");
		}

		Long recordId = pmcAnnouncementVO.getId();
		if (recordId==null){
			throwBusinessException("记录id为空");
		}
		PmcAnnouncement pmcAnnouncementById = pmcAnnouncementDao.getPmcAnnouncementById(recordId);
		if (pmcAnnouncementById==null){
			throwBusinessException("该记录不存在");
		}
		if (!pmcAnnouncementById.getStatus().equals(Const.PMC_ANNOUNCEMENT_STATUS_DRAFT)){
			throwBusinessException("只有草稿状态才能修改");
		}
		PmcAnnouncement pmcAnnouncement = new PmcAnnouncement();
		BeanUtils.copyProperties(pmcAnnouncementVO,pmcAnnouncement);
		Community communityById = communityDao.getCommunityById(pmcAnnouncementVO.getCommunityId());
		if (communityById!=null){
			pmcAnnouncement.setOrgId(communityById.getOrgId());
			pmcAnnouncement.setCommunityId(pmcAnnouncementVO.getCommunityId());
		}
		if (pmcAnnouncement.getStatus().equals(Const.PMC_ANNOUNCEMENT_STATUS_RELEASED)){
			pmcAnnouncement.setPublishTime(new Date());
			pmcAnnouncement.setPublishPmcUserId(userId);
		}
		int modify = pmcAnnouncementDao.modify(pmcAnnouncement);
		if (modify<=0){
			throwBusinessException("物业公告编辑失败");
		}
		//如果是发送状态 就要推送
		if (pmcAnnouncement.getStatus().equals(Const.PMC_ANNOUNCEMENT_STATUS_RELEASED)){
			sendPmcAnnouncementMq(pmcAnnouncement);
		}
		return successVo();
	}

	/**
	 * app分页查询物业公告
	 * @param pmcAnnouncementPageVO
	 * @return
	 */
	@Override
	public BaseVo appListPage(PmcAnnouncementPageVO pmcAnnouncementPageVO,HttpServletRequest request) {
		Long userId = getCurrentUserInfo().getId();

		String tid = request.getHeader("tid");

		List<Long> communityIds = new ArrayList<>();
		//如果是物业app
		if (Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid())){
			//如果是物业管理员
			Integer identity = getCurrentUserInfo().getCboInfo().getIdentity();
			if (!identity.equals(Const.IDENTITY_PMC_ADMIN)){
				pmcAnnouncementPageVO.setStatus(Const.PMC_ANNOUNCEMENT_STATUS_RELEASED);
			}
			PmcStaffRelCommunity pmcStaffRelCommunity = new PmcStaffRelCommunity();
			pmcStaffRelCommunity.setStaffId(userId);
			List<PmcStaffRelCommunity> byParam = pmcStaffRelCommunityDao.findByParam(pmcStaffRelCommunity);
			if (CollectionUtils.isNotEmpty(byParam)){
				for (PmcStaffRelCommunity staffRelCommunity : byParam) {
					communityIds.add(staffRelCommunity.getCommunityId());
				}
				pmcAnnouncementPageVO.setCommunityIds(communityIds);
			}

		}else if (Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid())){
			Long communityId = getCurrentUserInfo().getCboInfo().getCurrentCommunity().getId();
			//如果是居民app
			pmcAnnouncementPageVO.setStatus(Const.PMC_ANNOUNCEMENT_STATUS_RELEASED);
			pmcAnnouncementPageVO.setCommunityId(communityId);
		}else if (Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid())){

			//如果是居委会
			pmcAnnouncementPageVO.setStatus(Const.PMC_ANNOUNCEMENT_STATUS_RELEASED);
			Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
			List<Community> communityByOrgId = communityDao.getCommunityByOrgId(orgId);

			if (CollectionUtils.isNotEmpty(communityByOrgId)){
				for (Community community : communityByOrgId) {
					communityIds.add(community.getId());
				}
			}
			pmcAnnouncementPageVO.setCommunityIds(communityIds);
		}

		PageHelper.startPage(pmcAnnouncementPageVO.getPageNum(), pmcAnnouncementPageVO.getPageSize());
		List<PmcAnnouncementVO> pmcAnnouncementVOS = pmcAnnouncementDao.appListPage(pmcAnnouncementPageVO);
		for (PmcAnnouncementVO pmcAnnouncementVO : pmcAnnouncementVOS) {
			if (pmcAnnouncementVO.getStatus().equals(Const.PMC_ANNOUNCEMENT_STATUS_DRAFT)){
				pmcAnnouncementVO.setPublishTime(null);
			}
		}
		PageInfo<PmcAnnouncementVO> pageInfo = new PageInfo(pmcAnnouncementVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * web端分页查询物业公告
	 * @param pmcAnnouncementPageVO
	 * @return
	 */
	@Override
	public BaseVo webListPage(PmcAnnouncementPageVO pmcAnnouncementPageVO) {
		//当前用户的社区id
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();

		//所有小区的id
		List<Long> communityIds = new ArrayList<>();
		//所有的社区
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();

		//所有的小区
		List<Community> allCommunity = communityDao.getAllCommunity();
		if (CollectionUtils.isNotEmpty(allCommunity)){
			for (Community community1 : allCommunity) {
				communityIds.add(community1.getId());
			}
		}

		//判断是不是社区办
		if (userType.equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			pmcAnnouncementPageVO.setFlag(Const.FLAG_SHEQUBAN_YES);

			//判断是否选了社区
			if (pmcAnnouncementPageVO.getOrgId()!=null){
				//判断是否选了小区
				if (pmcAnnouncementPageVO.getCommunityId()==null){
					//根据社区id查询小区
					List<Community> communityByOrgId = communityDao.getCommunityByOrgId(pmcAnnouncementPageVO.getOrgId());
					List<Long> communityByOrgIds = new ArrayList<>();
					if (CollectionUtils.isNotEmpty(communityByOrgId)){
						for (Community community1 : communityByOrgId) {
							communityByOrgIds.add(community1.getId());
						}
						pmcAnnouncementPageVO.setCommunityIds(communityByOrgIds);
					}else {
						//如果社区下没有小区 小区id就传社区办的id
						pmcAnnouncementPageVO.setCommunityId(100113L);
					}
				}
			}else {
				//没选社区
				pmcAnnouncementPageVO.setCommunityIds(communityIds);
			}

		}else {
			pmcAnnouncementPageVO.setFlag(Const.FLAG_SHEQUBAN_NO);
			pmcAnnouncementPageVO.setOrgId(orgId);
		}

		PageHelper.startPage(pmcAnnouncementPageVO.getPageNum(), pmcAnnouncementPageVO.getPageSize());
		List<PmcAnnouncementVO> pmcAnnouncementVOS = pmcAnnouncementDao.webListPage(pmcAnnouncementPageVO);

		for (PmcAnnouncementVO pmcAnnouncementVO : pmcAnnouncementVOS) {
			pmcAnnouncementVO.setOrgName(commonUtil.getOrgName(pmcAnnouncementVO.getOrgId(),oaOrganizationList));
			if (pmcAnnouncementVO.getStatus().equals(Const.PMC_ANNOUNCEMENT_STATUS_DRAFT)){
				pmcAnnouncementVO.setPublishTime(null);
			}
		}
		PageInfo<PmcAnnouncementVO> pageInfo = new PageInfo(pmcAnnouncementVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 返显记录
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo reflectById(Long id) {

		PmcAnnouncement pmcAnnouncementById = pmcAnnouncementDao.getPmcAnnouncementById(id);
		if (pmcAnnouncementById==null){
			throwBusinessException("该记录不存在");
		}
		PmcAnnouncementVO pmcAnnouncementVO = new PmcAnnouncementVO();
		BeanUtils.copyProperties(pmcAnnouncementById,pmcAnnouncementVO);
		Community communityById = communityDao.getCommunityById(pmcAnnouncementById.getCommunityId());
		if (communityById!=null){
			pmcAnnouncementVO.setCommunityName(communityById.getCommunityName());
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(pmcAnnouncementVO);
		return baseVo;
	}
	/**
	 * 删除记录
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo delById(Long id) {
		PmcAnnouncement pmcAnnouncementById = pmcAnnouncementDao.getPmcAnnouncementById(id);
		if (pmcAnnouncementById==null){
			throwBusinessException("该记录已删除");
		}
		int delete = pmcAnnouncementDao.delete(id);
		if (delete<=0){
			throwBusinessException("删除记录失败");
		}
		return successVo();
	}
}
