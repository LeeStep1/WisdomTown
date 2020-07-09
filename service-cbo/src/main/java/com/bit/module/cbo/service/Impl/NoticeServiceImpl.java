package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.OaOrganization;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.module.cbo.bean.Notice;
import com.bit.module.cbo.bean.NoticeRelResident;
import com.bit.module.cbo.bean.ResidentExtend;
import com.bit.module.cbo.dao.NoticeDao;
import com.bit.module.cbo.dao.NoticeRelResidentDao;
import com.bit.module.cbo.dao.ResidentExtendDao;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.service.NoticeService;
import com.bit.module.cbo.vo.FileInfo;
import com.bit.module.cbo.vo.NoticePageVO;
import com.bit.module.cbo.vo.NoticeResidentReadDetailVO;
import com.bit.module.cbo.vo.NoticeVO;

import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.CommonUtil;
import com.bit.utils.DateUtil;
import com.bit.utils.StringUtil;
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
 * @Date 2019/8/8 9:03
 **/
@Service("noticeService")
public class NoticeServiceImpl extends BaseService implements NoticeService {
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private NoticeRelResidentDao noticeRelResidentDao;
	@Autowired
	private ResidentExtendDao residentExtendDao;
	@Autowired
	private FileServiceFeign fileServiceFeign;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private SendMqPushUtil sendMqPushUtil;


	/**
	 * 新增通知
	 * @param noticeVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo add(NoticeVO noticeVO) {
		Long userId = getCurrentUserInfo().getId();
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		Notice notice = new Notice();
		BeanUtils.copyProperties(noticeVO,notice);
		notice.setCreateTime(new Date());
		notice.setCreateUserId(userId);
		notice.setOrgId(orgId);
		notice.setReadNum(Const.INIT_TOTAL_NUMBER);

		if (notice.getStatus().equals(Const.ORG_NOTICE_STATUS_DRAFT)){
			//如果是草稿
			if (StringUtil.isNotEmpty(notice.getLabel())){
				List<Long> residents = getNoticeResidents(notice.getLabel(),orgId);
				notice.setTotalNum(residents.size());
			}else {
				notice.setTotalNum(Const.INIT_TOTAL_NUMBER);
			}
			int add = noticeDao.add(notice);
			if (add<=0){
				throwBusinessException("通知新增失败");
			}
			return successVo();
		}else if (notice.getStatus().equals(Const.ORG_NOTICE_STATUS_RELEASED)){
			//如果直接发布 要校验人群类型
			if (StringUtil.isEmpty(notice.getLabel())){
				throwBusinessException("通知人群类型不能为空");
			}
			notice.setPublishTime(new Date());
			notice.setPublishUserId(userId);

			List<Long> residents = getNoticeResidents(notice.getLabel(),orgId);
			notice.setTotalNum(residents.size());
			int add = noticeDao.add(notice);
			if (add<=0){
				throwBusinessException("通知新增失败");
			}

			if (CollectionUtils.isNotEmpty(residents)){
				//添加通知与居民的关系
				List<NoticeRelResident> noticeRelResidents = new ArrayList<>();
				for (Long resident : residents) {
					NoticeRelResident noticeRelResident = new NoticeRelResident();
					noticeRelResident.setResidentId(resident);
					noticeRelResident.setNoticeId(notice.getId());
					noticeRelResident.setConnectionStatus(Const.NOTICE_CONNECTION_STATUS_NO);
					noticeRelResident.setReadStatus(Const.NOTICE_READ_STATUS_NO);
					noticeRelResident.setConnectionUserId(null);
					noticeRelResidents.add(noticeRelResident);
				}
				int i = noticeRelResidentDao.batchAddNoticeRelResident(noticeRelResidents);
				if (i<=0){
					throwBusinessException("批量添加通知居民关系失败");
				}
				//发送通知推送
				sendNoticeMq(notice,residents);
			}else {
				throwBusinessException("选择人群类型居民数为0，不能发布");
			}
			return successVo();
		}
		return successVo();
	}

	/**
	 * 发送通知推送
	 * @param notice
	 * @param residents
	 */
	private void sendNoticeMq(Notice notice,List<Long> residents){
		//查询本社区内所有居民
		Long[] targetId = new Long[residents.size()];
		residents.toArray(targetId);
		if (targetId!=null && targetId.length>0){
			//查询社区名称
			List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
			String orgName = commonUtil.getOrgName(notice.getOrgId(), oaOrganizationList);
			String[] params = {notice.getTitle(),orgName};
			//组装mq
			MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(MessageTemplateEnum.CBO_REMIND_NOTICE_ORG_ADMIN,
					targetId,
					params,
					null,
					null,
					null);
			sendMqPushUtil.sendMqMessage(mqSendMessage);
		}
	}

	/**
	 * 处理选中通知类型的居民
	 * @param label
	 * @return
	 */
	private List<Long> getNoticeResidents(String label,Long orgId){
		String[] ss = label.split(",");
		List<Integer> extendTypes = Arrays.asList(ss).stream().map(s -> (Integer.valueOf(s))).collect(Collectors.toList());

		//根据人群类型查询居民
		List<Long> residents = new ArrayList<>();
		for (Integer extendType : extendTypes) {
			ResidentExtend residentExtend = new ResidentExtend();
			residentExtend.setExtendType(extendType);
			residentExtend.setOrgId(orgId);
			List<ResidentExtend> byParam = residentExtendDao.findByParam(residentExtend);
			if (CollectionUtils.isNotEmpty(byParam)){
				for (ResidentExtend extend : byParam) {
					residents.add(extend.getResidentId());
				}
			}
		}
		//去重
		residents = residents.stream().distinct().collect(Collectors.toList());
		return residents;
	}

	@Override
	@Transactional
	public BaseVo modify(NoticeVO noticeVO) {
		Long userId = getCurrentUserInfo().getId();
		Long recordId = noticeVO.getId();
		Notice noticeById = noticeDao.getNoticeById(recordId);
		if (noticeById==null){
			throwBusinessException("该记录不存在");
		}
		if (!noticeById.getStatus().equals(Const.ORG_NOTICE_STATUS_DRAFT)){
			throwBusinessException("不是草稿不能编辑");
		}
		Notice notice = new Notice();
		BeanUtils.copyProperties(noticeVO,notice);
		if (noticeVO.getStatus().equals(Const.ORG_NOTICE_STATUS_DRAFT)){
			//如果不发布
			if (StringUtil.isNotEmpty(notice.getLabel())){
				List<Long> residents = getNoticeResidents(notice.getLabel(),noticeById.getOrgId());
				notice.setTotalNum(residents.size());
			}else {
				notice.setTotalNum(Const.INIT_TOTAL_NUMBER);
			}
			int modify = noticeDao.modify(notice);
			if (modify<=0){
				throwBusinessException("修改通知失败");
			}
		}else if (noticeVO.getStatus().equals(Const.ORG_NOTICE_STATUS_RELEASED)){
			//如果要发布
			if (StringUtil.isEmpty(notice.getLabel())){
				throwBusinessException("发送人群标签为空");
			}else {
				List<Long> residents = getNoticeResidents(notice.getLabel(),noticeById.getOrgId());
				notice.setTotalNum(residents.size());
				notice.setPublishTime(new Date());
				notice.setPublishUserId(userId);
				int update = noticeDao.modify(notice);
				if (update<=0){
					throwBusinessException("更新通知失败");
				}

				if (CollectionUtils.isNotEmpty(residents)){
					//添加通知与居民的关系
					List<NoticeRelResident> noticeRelResidents = new ArrayList<>();
					for (Long resident : residents) {
						NoticeRelResident noticeRelResident = new NoticeRelResident();
						noticeRelResident.setResidentId(resident);
						noticeRelResident.setNoticeId(notice.getId());
						noticeRelResident.setConnectionStatus(Const.NOTICE_CONNECTION_STATUS_NO);
						noticeRelResident.setReadStatus(Const.NOTICE_READ_STATUS_NO);
						noticeRelResident.setConnectionUserId(null);
						noticeRelResidents.add(noticeRelResident);
					}
					int i = noticeRelResidentDao.batchAddNoticeRelResident(noticeRelResidents);
					if (i<=0){
						throwBusinessException("批量添加通知居民关系失败");
					}
					//发送通知推送
					sendNoticeMq(notice,residents);
				}else {
					throwBusinessException("选择人群类型居民数为0，不能发布");
				}
			}
		}
		return successVo();
	}

	/**
	 * web端返显或单查通知
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo webReflectById(Long id) {
		Notice noticeById = noticeDao.getNoticeById(id);
		if (noticeById==null){
			throwBusinessException("该记录不存在");
		}
		NoticeVO noticeVO = new NoticeVO();
		BeanUtils.copyProperties(noticeById,noticeVO);
		BaseVo byId = fileServiceFeign.findById(noticeById.getPic());
		if (byId.getData()!=null){
			String s = JSON.toJSONString(byId.getData());
			FileInfo fileInfo = JSON.parseObject(s,FileInfo.class);
			noticeVO.setPicFileInfo(fileInfo);
		}
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
		noticeVO.setOrgName(commonUtil.getOrgName(noticeVO.getOrgId(),oaOrganizationList));
		BaseVo baseVo = new BaseVo();
		baseVo.setData(noticeVO);
		return baseVo;
	}
	/**
	 * app端返显或单查通知 并且 改变已读状态
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo appReflectById(Long id,HttpServletRequest request) {
		String tid = request.getHeader("tid");
		//居民id
		Long residentId = getCurrentUserInfo().getId();
		Notice noticeById = noticeDao.getNoticeById(id);
		if (noticeById==null){
			throwBusinessException("该记录不存在");
		}
		NoticeVO noticeVO = new NoticeVO();
		BeanUtils.copyProperties(noticeById,noticeVO);
		BaseVo byId = fileServiceFeign.findById(noticeById.getPic());
		if (byId.getData()!=null){
			String s = JSON.toJSONString(byId.getData());
			FileInfo fileInfo = JSON.parseObject(s,FileInfo.class);
			noticeVO.setPicFileInfo(fileInfo);
		}
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
		noticeVO.setOrgName(commonUtil.getOrgName(noticeVO.getOrgId(),oaOrganizationList));
		//如果是居民端 要更新记录状态
		if (Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid())){

			//更新记录的已读状态
			NoticeRelResident recordByNoticeIdAndResidentId = noticeRelResidentDao.getRecordByNoticeIdAndResidentId(id, residentId);
			if (recordByNoticeIdAndResidentId==null){
				throwBusinessException("无此人的通知记录");
			}

			NoticeRelResident noticeRelResident = new NoticeRelResident();
			noticeRelResident.setId(recordByNoticeIdAndResidentId.getId());
			noticeRelResident.setReadStatus(Const.NOTICE_READ_STATUS_YES);
			noticeRelResident.setReadTime(new Date());
			int update = noticeRelResidentDao.update(noticeRelResident);
			if (update<=0){
				throwBusinessException("更新居民通知关联关系失败");
			}

			Notice notice = new Notice();
			notice.setId(id);
			NoticeRelResident temp = new NoticeRelResident();
			temp.setNoticeId(id);
			temp.setReadStatus(Const.NOTICE_READ_STATUS_YES);
			List<NoticeRelResident> byParam = noticeRelResidentDao.findByParam(temp);
			if (CollectionUtils.isNotEmpty(byParam)){
				notice.setReadNum(byParam.size());
			}else {
				notice.setReadNum(Const.INIT_TOTAL_NUMBER);
			}
			int modify = noticeDao.modify(notice);
			if (modify<=0){
				throwBusinessException("更新通知已读人数失败");
			}
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(noticeVO);
		return baseVo;
	}

	@Override
	public BaseVo webListPage(NoticePageVO noticePageVO) {
		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();
		List<Long> orgIds = new ArrayList<>();
		//所有的社区
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();

		//判断是不是社区办
		if (userType.equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			if (noticePageVO.getOrgId()==null){
				if (CollectionUtils.isNotEmpty(oaOrganizationList)){
					for (OaOrganization oaOrganization : oaOrganizationList) {
						orgIds.add(oaOrganization.getId());
					}
					noticePageVO.setOrgIds(orgIds);
				}
			}else {
				orgIds.add(noticePageVO.getOrgId());
				noticePageVO.setOrgIds(orgIds);
			}

			noticePageVO.setFlag(Const.FLAG_SHEQUBAN_YES);
		}else {
			//不是社区办
			Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
			noticePageVO.setOrgId(orgId);
			noticePageVO.setFlag(Const.FLAG_SHEQUBAN_NO);
		}
		PageHelper.startPage(noticePageVO.getPageNum(), noticePageVO.getPageSize());
		List<NoticeVO> notices = noticeDao.webListPage(noticePageVO);
		for (NoticeVO noticeVO : notices) {
			noticeVO.setOrgName(commonUtil.getOrgName(noticeVO.getOrgId(),oaOrganizationList));
			if (noticeVO.getStatus().equals(Const.ORG_NOTICE_STATUS_DRAFT)){
				noticeVO.setPublishTime(null);
			}
		}
		PageInfo<NoticeVO> pageInfo = new PageInfo<>(notices);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);

		return baseVo;
	}
	/**
	 * app端分页查询
	 * @param noticePageVO
	 * @return
	 */
	@Override
	public BaseVo appListPage(NoticePageVO noticePageVO, HttpServletRequest request) {

		String tid = request.getHeader("tid");
		List<NoticeVO> noticeVOS = new ArrayList<>();

		PageHelper.startPage(noticePageVO.getPageNum(),noticePageVO.getPageSize());
		if (Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid())){
			//如果是居民端
			Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
			Long userId = getCurrentUserInfo().getId();
			noticePageVO.setOrgId(orgId);
			noticePageVO.setResidentId(userId);
			noticeVOS = noticeDao.appResidentListPage(noticePageVO);

		}else if (Integer.valueOf(tid).equals(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid())){
			//如果是居委会端
			Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
			noticePageVO.setOrgId(orgId);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			if (noticePageVO.getTimeLimit().equals(Const.NOTICE_APP_FILTER_TIME_LIMIT_THREE_DAYS)){
				//三天内
				calendar.add(Calendar.DATE, -3);
				String begintTime = DateUtil.date2String(calendar.getTime(),DateUtil.DatePattern.YYYYMMDD.getValue());
				noticePageVO.setBeginTime(begintTime);
			}else if (noticePageVO.getTimeLimit().equals(Const.NOTICE_APP_FILTER_TIME_LIMIT_ONE_WEEK)){
				//一周内
				calendar.add(Calendar.DATE, -7);
				String begintTime = DateUtil.date2String(calendar.getTime(),DateUtil.DatePattern.YYYYMMDD.getValue());
				noticePageVO.setBeginTime(begintTime);

			}else if (noticePageVO.getTimeLimit().equals(Const.NOTICE_APP_FILTER_TIME_LIMIT_ONE_MONTH)){
				//一月内
				calendar.add(Calendar.MONTH, -1);
				String begintTime = DateUtil.date2String(calendar.getTime(),DateUtil.DatePattern.YYYYMMDD.getValue());
				noticePageVO.setBeginTime(begintTime);

			}else if (noticePageVO.getTimeLimit().equals(Const.NOTICE_APP_FILTER_TIME_LIMIT_THREE_MONTH)){
				//三月内
				calendar.add(Calendar.MONTH, -3);
				String begintTime = DateUtil.date2String(calendar.getTime(),DateUtil.DatePattern.YYYYMMDD.getValue());
				noticePageVO.setBeginTime(begintTime);
			}
			noticeVOS = noticeDao.appOrgListPage(noticePageVO);

		}else {
			throwBusinessException("非法的接入端");
		}
		for (NoticeVO noticeVO : noticeVOS) {
			if (noticeVO.getPic()!=null){
				BaseVo byId = fileServiceFeign.findById(noticeVO.getPic());
				if (byId.getData()!=null){
					String s = JSON.toJSONString(byId.getData());
					FileInfo fileInfo = JSON.parseObject(s,FileInfo.class);
					noticeVO.setPicFileInfo(fileInfo);
				}	
			}
		}
		PageInfo<NoticeVO> pageInfo = new PageInfo<>(noticeVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 删除通知
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo delById(Long id) {
		Notice noticeById = noticeDao.getNoticeById(id);
		if (noticeById==null){
			throwBusinessException("该记录已删除");
		}
		int i = noticeDao.delById(id);
		if (i<=0){
			throwBusinessException("删除通知失败");
		}
		noticeRelResidentDao.delByNoticeId(id);

		return successVo();
	}
	/**
	 * 查看已读状态
	 * @param noticePageVO
	 * @return
	 */
	@Override
	public BaseVo checkReadDetail(NoticePageVO noticePageVO) {
		PageHelper.startPage(noticePageVO.getPageNum(),noticePageVO.getPageSize());

		List<NoticeResidentReadDetailVO> noticeResidentReadDetailVOS = noticeRelResidentDao.checkReadDetail(noticePageVO);
		PageInfo<NoticeResidentReadDetailVO> pageInfo = new PageInfo<>(noticeResidentReadDetailVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 更新联系状态
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo updateConnectionStatus(Long id) {
		Long userId = getCurrentUserInfo().getId();
		NoticeRelResident noticeRelResident = new NoticeRelResident();
		noticeRelResident.setId(id);
		noticeRelResident.setConnectionStatus(Const.NOTICE_CONNECTION_STATUS_YES);
		noticeRelResident.setConnectionTime(new Date());
		noticeRelResident.setConnectionUserId(userId);
		int update = noticeRelResidentDao.update(noticeRelResident);
		if (update<=0){
			throwBusinessException("更新联系状态失败");
		}
		return successVo();
	}
	/**
	 * 校验扩展类型有多少居民
	 * @param extendType
	 * @return
	 */
	@Override
	public BaseVo checkOptionGroupNum(Integer extendType) {
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		ResidentExtend residentExtend = new ResidentExtend();
		residentExtend.setExtendType(extendType);
		residentExtend.setOrgId(orgId);
		List<ResidentExtend> byParam = residentExtendDao.findByParam(residentExtend);
		BaseVo baseVo = new BaseVo();
		if (CollectionUtils.isEmpty(byParam)){
			baseVo.setCode(ResultCode.RESIDNET_EXTEND_TYPE_NUM_ZERO.getCode());
			baseVo.setMsg(ResultCode.RESIDNET_EXTEND_TYPE_NUM_ZERO.getInfo());
		}
		return baseVo;
	}
}
