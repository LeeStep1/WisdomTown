package com.bit.module.vol.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.common.consts.RedisKey;
import com.bit.common.enumerate.CampaignAuditEnum;
import com.bit.common.enumerate.CampaignStatusEnum;
import com.bit.core.utils.CacheUtil;

import com.bit.module.vol.bean.*;
import com.bit.module.vol.dao.*;
import com.bit.module.vol.feign.FileServiceFeign;
import com.bit.module.vol.feign.SysServiceFeign;
import com.bit.module.vol.service.CampaignService;
import com.bit.module.vol.service.StationService;
import com.bit.module.vol.vo.CampaignVO;
import com.bit.module.vol.vo.FavouriteVO;
import com.bit.module.vol.vo.FileInfoVO;
import com.bit.module.vol.vo.StationVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqDelay;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.*;
import com.bit.utils.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chenduo
 * @create 2019-03-04 14:53
 */
@Service("campaignService")
public class CampaignServiceImpl extends BaseService implements CampaignService {
	@Autowired
	private CampaignDao campaignDao;
	@Autowired
	private CampaignVolunteerRecordDao campaignVolunteerRecordDao;
	@Autowired
	private StationDao stationDao;
	@Autowired
	private VolunteerDao volunteerDao;
	@Autowired
	private FavouriteDao favouriteDao;
	@Autowired
	private FileServiceFeign fileServiceFeign;
	@Autowired
	private StationService stationService;
	@Autowired
	private SysServiceFeign sysServiceFeign;
	@Autowired
	private CacheUtil cacheUtil;
	@Autowired
	private VolRedisUtil volRedisUtil;
	@Autowired
	private VolUtil volUtil;
	@Autowired
	private TypeUtil typeUtil;
	@Autowired
	private CampaignUtil campaignUtil;
	@Autowired
	private SendMqPushUtil sendMqPushUtil;
	@Autowired
	private CampaignService campaignService;

	/**
	 * 添加活动记录
	 *
	 * @param campaign
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo add(Campaign campaign) {
		Volunteer vi = volUtil.getVolunteerInfo();
		//记录初始为草稿状态
		campaign.setCampaignStatus(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_DRAFT.getCode());
		campaign.setCampaignFull(Const.CAMPAIGN_FULL_NO);
		campaign.setStationId(vi.getStationId());
		campaign.setVersion(Const.CAMPAIGN_VERSION);
		//计算活动持续时长
		//得到date类型的开始时间
		Date start = DateUtil.getStartOrCancelDate(String.valueOf(campaign.getStartDate()), campaign.getStartTime());
		//得到date类型的结束时间
		Date end = DateUtil.getStartOrCancelDate(String.valueOf(campaign.getEndDate()), campaign.getEndTime());
		Long time = end.getTime() - start.getTime();
		double gap = time.doubleValue() / 1000 / 60 / 60;
		campaign.setCampaignHour(new BigDecimal(gap));
		campaign.setCampaignDonateMoney(new BigDecimal(0));
		Long stationId = vi.getStationId();
		Station byId = stationDao.findById(stationId);
		if (byId == null) {
			throwBusinessException("机构为空");
		}
		campaign.setCampaignAudit(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_DRAFT.getCode());
		//default 状态 为4
		campaign.setCampaignStatus(CampaignStatusEnum.CAMPAIGN_STATUS_DEFAULT.getCode());
		campaign.setCreateTime(new Date());

		campaign.setBeginDate(start);
		campaign.setFinishDate(end);
		campaignDao.add(campaign);
		return new BaseVo();
	}

	/**
	 * 发布草稿
	 *
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo release(Long id, HttpServletRequest request) {
		BaseVo baseVo = new BaseVo();
		Campaign obj = campaignDao.queryById(id);
		if (!(obj.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_DRAFT.getCode()) ||
				obj.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_REJECTED.getCode()))) {
			throwBusinessException("该记录不能发布");
		}
		//发布活动
		Campaign campaign = new Campaign();
		campaign.setId(id);

		//如果是站内发布的活动
		if (obj.getCampaignScale().equals(Const.CAMPAIGN_SCALE_IN)) {
			campaign.setCampaignStatus(CampaignStatusEnum.CAMPAIGN_STATUS_NOT_START.getCode());
			campaign.setCampaignAudit(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_PASSED.getCode());
		}
		//如果是站外发布的活动
		if (obj.getCampaignScale().equals(Const.CAMPAIGN_SCALE_OUT)) {
			Station station = stationDao.findById(obj.getStationId());
			if (station.getStationLevel().equals(1)) {
				campaign.setCampaignStatus(CampaignStatusEnum.CAMPAIGN_STATUS_NOT_START.getCode());
				campaign.setCampaignAudit(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_PASSED.getCode());
			} else {
				campaign.setCampaignAudit(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_WAIT_VERIFY.getCode());
			}
		}
		String key = RedisKey.VOL_ENROLL.getKey() + id;
		Date now = new Date();
		//判断活动是否过期
		if (now.after(obj.getFinishDate())) {
			baseVo.setData(null);
			baseVo.setCode(ResultCode.WRONG.getCode());
			baseVo.setMsg("活动已经过期");
			return baseVo;
		}

		Integer version = obj.getVersion();
		if (version == null) {
			version = 0;
		}
		campaign.setVersion(version);
		campaignDao.update(campaign);
		//放到redis里
		Long time = (obj.getBeginDate().getTime() - now.getTime()) / 1000;
		if (!campaign.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_WAIT_VERIFY.getCode())) {
			volRedisUtil.createCampaign(key, time);
		}
		// 延迟队列
		if (obj.getCampaignScale().equals(Const.CAMPAIGN_SCALE_IN)) {
			Long xx = obj.getBeginDate().getTime() - now.getTime();
			double gap = xx.doubleValue() / 1000 / 60 / 60;
			if (xx.doubleValue() < new Double(0)) {
				throwBusinessException("活动时间已过");
			}
			String[] param = {obj.getCampaignTheme(), obj.getCampaignPlace(), DateUtil.format(obj.getBeginDate())};
			List<Long> uids = campaignService.queryEnrollId(obj.getId());

			if (gap < new Double(2)) {
				//如果当前时间距离活动开始时间小于2小时 延迟传0  立刻发送推送消息
				if (CollectionUtils.isNotEmpty(uids)) {
					Long[] uid = uids.toArray(new Long[uids.size()]);
					MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageWithBuzIdByAlias(obj.getId(), MessageTemplateEnum.VOL_CAMPAIGN_DELAY, uid, param, null, new Date(), null);
					sendMqPushUtil.sendMqMessage(mqSendMessage);
				}
			} else {
				//如果当前时间距离活动开始时间大于2小时 延迟传差值 毫秒
				Long delay = (obj.getBeginDate().getTime() - now.getTime() - 2 * 60 * 60 * 1000);
				if (delay > 0) {
					MqDelay mqDelayNew = AppPushMessageUtil.pushOrgDelayMessageByAlias(obj.getId(), MessageTemplateEnum.VOL_CAMPAIGN_DELAY, param, delay);
					sendMqPushUtil.sendMqDelayMessage(mqDelayNew);
				} else {
					//消息立即发送
					if (CollectionUtils.isNotEmpty(uids)) {
						Long[] uid = uids.toArray(new Long[uids.size()]);
						MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageWithBuzIdByAlias(obj.getId(), MessageTemplateEnum.VOL_CAMPAIGN_DELAY, uid, param, null, new Date(), null);
						sendMqPushUtil.sendMqMessage(mqSendMessage);
					}
				}
			}
		}
		Station station = stationDao.findById(obj.getStationId());
		// 发送mq消息
		//发送到app端
		//站内活动 推送app端
		if (obj.getCampaignScale().equals(Const.CAMPAIGN_SCALE_IN)) {
			Volunteer par = new Volunteer();
			par.setStationId(obj.getStationId());
			List<Volunteer> byParam = volunteerDao.findByParam(par);
			List<String> carIds = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(byParam)) {
				for (Volunteer vo : byParam) {
					carIds.add(vo.getCardId());
				}
				List<Long> longList = sysServiceFeign.batchSelectByCardId(carIds);

				if (CollectionUtils.isNotEmpty(longList)) {
					Long[] targetIds = new Long[longList.size()];
					longList.toArray(targetIds);
					String[] mobile = {station.getStationName()};
					MqSendMessage mq = AppPushMessageUtil.pushUserMessageWithBuzIdByAlias(campaign.getId(), MessageTemplateEnum.CAMPAIGN_NEW_RELEASE, targetIds, mobile, mobile[0], new Date(), null);
					sendMqPushUtil.sendMqMessage(mq);
				}
			}
		}
		//站外活动 推送web端
		if (obj.getCampaignScale().equals(Const.CAMPAIGN_SCALE_OUT)) {
			//查询一级机构id
			Long topStation = stationDao.findTopStation();
			Long[] targetIds = new Long[]{topStation};
			String[] mobile = {station.getStationName(), obj.getCampaignTheme()};
			MqSendMessage mq = AppPushMessageUtil.pushOrgTaskByAlias(MessageTemplateEnum.CAMPAIGN_RELEASE, targetIds, mobile, campaign.getId(), version, station.getStationName(), new Date(), null);
			sendMqPushUtil.sendMqMessage(mq);
		}
		return new BaseVo();
	}


	/**
	 * 分页查询活动记录
	 *
	 * @param campaignVO
	 * @return
	 */
	@Override
	public BaseVo listPage(CampaignVO campaignVO) {
		Volunteer vi = volUtil.getVolunteerInfo();
		BaseVo baseVo = new BaseVo();
		if (campaignVO.getCampaignStatus() != null) {
			Date nowdate = new Date();
			campaignVO.setNowDate(nowdate);
		}
		Station station = stationDao.findById(vi.getStationId());
		List<Long> stationIds = new ArrayList<>();
		//如果是一级机构
		if (station.getStationLevel() == 1) {
			// 取出下级机构和自己
			List<Station> tree = stationService.childTree(vi.getStationId());
			for (Station s : tree) {
				stationIds.add(s.getId());
			}
			campaignVO.setStationList(stationIds);
		} else {
			stationIds.add(vi.getStationId());
			campaignVO.setStationList(stationIds);
		}

		//合成前端的参数 复制给begindate 和 finishdate
		if (campaignVO.getStartDate() != null) {
			Date t1 = campaignUtil.convertIntegerDateToBeginDate(campaignVO.getStartDate());
			campaignVO.setBeginDate(t1);
		}
		if (campaignVO.getEndDate() != null) {

			Date t2 = campaignUtil.convertIntegerDateToEndDate(campaignVO.getEndDate());
			campaignVO.setFinishDate(t2);
		}
		PageHelper.startPage(campaignVO.getPageNum(), campaignVO.getPageSize());
		List<CampaignPage> campaignPageList = campaignDao.findByConditionPage(campaignVO);
		for (CampaignPage campaignPage : campaignPageList) {
			String start = DateUtil.date2String(campaignPage.getBeginDate(), DateUtil.DatePattern.YYYYMMDD.getValue());
			start = start.replaceAll("-", "");
			campaignPage.setStartDate(Integer.valueOf(start));
			String end = DateUtil.date2String(campaignPage.getFinishDate(), DateUtil.DatePattern.YYYYMMDD.getValue());
			end = end.replaceAll("-", "");
			campaignPage.setEndDate(Integer.valueOf(end));
			if (!campaignPage.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_CANCELED.getCode())) {
				campaignPage.setCampaignStatus(campaignUtil.setCampaignStatusWithDate(campaignPage.getBeginDate(), campaignPage.getFinishDate()));
			}
			//查询签到人数
			CampaignVolunteerRecord param = new CampaignVolunteerRecord();
			param.setCampaignId(campaignPage.getId());
			param.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_YES);
			param.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
			List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findVolunteerCampaign(param);
			int signNumber = 0;
			if (CollectionUtils.isNotEmpty(byParam)) {
				signNumber = byParam.size();
			}
			campaignPage.setCampaignHour(campaignPage.getCampaignHour().multiply(new BigDecimal(signNumber)));
			CalculateParam calculateParam = new CalculateParam();
			calculateParam.setCampaignId(campaignPage.getId());
			Calculate calculate = campaignVolunteerRecordDao.countTimeAndMoneyAndNumber(calculateParam);
			if (calculate != null) {
				if (calculate.getMoney() != null) {
					campaignPage.setCampaignDonateMoney(calculate.getMoney());
				} else {
					campaignPage.setCampaignDonateMoney(new BigDecimal(0));
				}
			}

		}
		PageInfo<CampaignPage> pageInfo = new PageInfo<CampaignPage>(campaignPageList);
		baseVo.setData(pageInfo);
		return baseVo;
	}


	/**
	 * 反显单条记录
	 *
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo reflectById(Long id) {
		Campaign campaign = campaignDao.queryById(id);
		if (campaign == null) {
			throwBusinessException("该记录不存在");
		}
		if (campaign.getCampaignImage() != null) {
			// 查询图片
			FileInfo fileInfo = getFileInfo(campaign);
			campaign.setFileInfo(fileInfo);
		}

		Map<String, Object> result = campaignUtil.getStartOrEndTime(campaign.getBeginDate(), campaign.getFinishDate());
		campaign.setStartDate((Integer) result.get("startDate"));
		campaign.setEndDate((Integer) result.get("endDate"));
		campaign.setStartTime((String) result.get("startTime"));
		campaign.setEndTime((String) result.get("endTime"));
		BaseVo baseVo = new BaseVo();
		baseVo.setData(campaign);
		return baseVo;
	}

	/**
	 * 获取图片文件
	 *
	 * @param campaign
	 * @return
	 */
	private FileInfo getFileInfo(Campaign campaign) {
		BaseVo b1 = fileServiceFeign.findById(campaign.getCampaignImage());
		String s = JSON.toJSONString(b1.getData());
		FileInfo fileInfo = JSON.parseObject(s, FileInfo.class);
		if (fileInfo == null) {
			throwBusinessException("文件不存在");
		}
		return fileInfo;
	}

	/**
	 * 取消活动
	 *
	 * @param campaignVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo cancelById(CampaignVO campaignVO) {
		// 判断记录能不能取消
		Campaign obj = campaignDao.queryById(campaignVO.getId());

		if (obj.getCampaignStatus() != null && obj.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_DRAFT.getCode())) {
			throwBusinessException("该活动不能取消");
		}

		Date date = obj.getBeginDate();
		//判断如果活动时间已过
		Date now = new Date();
		if (now.after(date)) {
			throwBusinessException("该活动不能取消");
		}
		double time = date.getTime() - now.getTime();
		double gap = time / 1000 / 60 / 60;
		//如果离活动开始小于2小时不能取消
		if (gap <= new Double(2)) {
			throwBusinessException("距离活动开始不到2小时活动不能取消");
		}
		Station station = stationDao.findById(obj.getStationId());

		Campaign campaign = new Campaign();
		campaign.setId(campaignVO.getId());
		campaign.setCampaignStatus(CampaignStatusEnum.CAMPAIGN_STATUS_CANCELED.getCode());
		campaign.setCampaignCancelReason(campaignVO.getCampaignCancelReason());
		Integer version = obj.getVersion();
		if (version == null) {
			version = 0;
		}
		campaign.setVersion(version);
		campaignDao.update(campaign);

		//更改报名记录的启用状态
		List<Long> ids = new ArrayList<>();
		List<Long> volIds = new ArrayList<>();
		CampaignVolunteerRecord temp = new CampaignVolunteerRecord();
		temp.setCampaignId(campaignVO.getId());
		temp.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
		List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(temp);
		if (CollectionUtils.isNotEmpty(byParam)) {
			for (CampaignVolunteerRecord campaignVolunteerRecord : byParam) {
				ids.add(campaignVolunteerRecord.getId());
				volIds.add(campaignVolunteerRecord.getVolunteerId());
			}
			if (ids.size() > 0) {
				campaignVolunteerRecordDao.batchUpdateInactive(ids);
			}
		}
		String key = RedisKey.VOL_ENROLL.getKey() + campaignVO.getId();
		//取消了活动就要删除redis中的缓存
		cacheUtil.del(key);
		//发送mq消息
		Long[] longs = null;
		//批量查询志愿者 得到身份证号
		if (CollectionUtils.isNotEmpty(volIds)) {
			List<String> idlist = volunteerDao.batchSelectByIds(volIds);
			if (CollectionUtils.isNotEmpty(idlist)) {
				List<Long> longList = sysServiceFeign.batchSelectByCardId(idlist);
				if (CollectionUtils.isNotEmpty(longList)) {
					Long[] targetId = new Long[longList.size()];
					longs = longList.toArray(targetId);
				}
			}
		}
		String[] params = {obj.getCampaignTheme(), campaignVO.getCampaignCancelReason()};

		if (longs != null) {
			MqSendMessage mq = AppPushMessageUtil.pushUserMessageWithBuzIdByAlias(campaign.getId(), MessageTemplateEnum.CAMPAIGN_CANCEL, longs, params, station.getStationName(), new Date(), null);
			sendMqPushUtil.sendMqMessage(mq);
		}

		return new BaseVo();
	}

	/**
	 * 删除活动
	 *
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo deleteById(Long id) {
		Campaign campaign = campaignDao.queryById(id);
		if (campaign.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_DRAFT.getCode()) ||
				campaign.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_REJECTED.getCode())) {
			campaignDao.deleteById(id);
		} else {
			throwBusinessException("活动状态错误不能删除");
		}

		return new BaseVo();
	}

	/**
	 * 根据机构id 查询活动
	 *
	 * @param stationVO
	 * @return
	 */
	@Override
	public BaseVo queryStationCampaign(StationVO stationVO) {
		Long stationId = stationVO.getId();
		List<VolunteerCampaign> volunteerCampaignList = new ArrayList<>();
		Campaign obj = new Campaign();
		obj.setStationId(stationId);
		obj.setCampaignTheme(stationVO.getCampaignTheme());
		obj.setNowDate(new Date());
		obj.setCampaignStatus(stationVO.getCampaignStatus());
		PageHelper.startPage(stationVO.getPageNum(), stationVO.getPageSize());
		List<Campaign> byStationIdAndTheme = campaignDao.findByStationIdAndTheme(obj);
		for (Campaign campaign : byStationIdAndTheme) {
			// 活动人数  活动时长 捐款
			VolunteerCampaign volunteerCampaign = new VolunteerCampaign();
			volunteerCampaign.setId(campaign.getId());
			volunteerCampaign.setCampaignTheme(campaign.getCampaignTheme());

			volunteerCampaign.setCampaignPlace(campaign.getCampaignPlace());
			volunteerCampaign.setCampaignNumber(campaign.getCampaignNumber());
			volunteerCampaign.setEnrollNumber(campaign.getEnrollNumber());
			volunteerCampaign.setCampaignTypeList(typeUtil.getType(campaign.getCampaignType()));

			// 计算活动人数
			Calculate calculate = campaignVolunteerRecordDao.countTimeAndMoneyByCampaignId(campaign.getId());
			calculate.setMoney(calculate.getMoney() == null ? new BigDecimal(0) : calculate.getMoney());
			calculate.setHour(calculate.getHour() == null ? new BigDecimal(0) : calculate.getHour());
			calculate.setNumber(calculate.getNumber() == null ? 0 : calculate.getNumber());

			BigDecimal money = calculate.getMoney();
			volunteerCampaign.setServiceHour(calculate.getHour());
			volunteerCampaign.setDonateMoney(money);

			BaseVo b1 = fileServiceFeign.findById(campaign.getCampaignImage());
			String s = JSON.toJSONString(b1.getData());
			FileInfo fileInfo = JSON.parseObject(s, FileInfo.class);
			volunteerCampaign.setFileInfo(fileInfo);

			//设置startDate等字段
			Map<String, Object> result = campaignUtil.getStartOrEndTime(campaign.getBeginDate(), campaign.getFinishDate());
			volunteerCampaign.setStartDate((Integer) result.get("startDate"));
			volunteerCampaign.setEndDate((Integer) result.get("endDate"));
			volunteerCampaign.setStartTime((String) result.get("startTime"));
			volunteerCampaign.setEndTime((String) result.get("endTime"));
			if (!campaign.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_CANCELED.getCode())) {
				volunteerCampaign.setCampaignStatus(campaignUtil.setCampaignStatusWithDate(campaign.getBeginDate(), campaign.getFinishDate()));
			}

			volunteerCampaignList.add(volunteerCampaign);
		}

		PageInfo<VolunteerCampaign> pageInfo = new PageInfo<VolunteerCampaign>(volunteerCampaignList);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 获取资料
	 *
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo getdataById(Long id) {
		Campaign obj = campaignDao.queryById(id);
		if (obj == null) {
			throwBusinessException("该活动不存在");
		}
		List<Long> fileids = new ArrayList<>();
		List<FileInfo> fileInfoList = new ArrayList<>();
		String informationFile = obj.getInformationFile();
		if (StringUtil.isNotEmpty(informationFile)) {
			String[] str = informationFile.split(",");
			for (String s : str) {
				fileids.add(Long.parseLong(s));
			}
			fileInfoList = getFileInfos(fileids);
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(fileInfoList);
		return baseVo;
	}

	private List<FileInfo> getFileInfos(List<Long> fileids) {
		List<FileInfo> fileInfoList;
		FileInfoVO fileInfoVO = new FileInfoVO();
		fileInfoVO.setFileIds(fileids);
		BaseVo b1 = fileServiceFeign.findByIds(fileInfoVO);
		String s1 = JSON.toJSONString(b1.getData());
		fileInfoList = JSON.parseArray(s1, FileInfo.class);
		return fileInfoList;
	}

	/**
	 * 更新活动
	 *
	 * @param campaign
	 * @return
	 */
	@Override
	public BaseVo update(Campaign campaign) {
		if (campaign.getStartDate() != null && campaign.getEndDate() != null
				&& StringUtil.isNotEmpty(campaign.getStartTime()) && StringUtil.isNotEmpty(campaign.getEndTime())) {
			campaign.setBeginDate(DateUtil.getStartOrCancelDate(campaign.getStartDate().toString(), campaign.getStartTime()));
			campaign.setFinishDate(DateUtil.getStartOrCancelDate(campaign.getEndDate().toString(), campaign.getEndTime()));
		}

		campaignDao.update(campaign);
		return new BaseVo();
	}

	/**
	 * 导出所有数据到excel
	 *
	 * @param response
	 * @return
	 */
	@Override
	public void exportToExcel(String campaignTheme,
							  Integer campaignStatus,
							  Integer startDate,
							  Integer endDate,
							  String startTime,
							  String endTime,
							  String stationName,
							  HttpServletResponse response) {
		Campaign campaign = new Campaign();
		campaign.setCampaignTheme(campaignTheme);
		campaign.setCampaignStatus(campaignStatus);
		campaign.setStartDate(startDate);
		campaign.setEndDate(endDate);
		campaign.setStartTime(startTime);
		campaign.setEndTime(endTime);
		campaign.setStationName(stationName);
		if (campaign.getCampaignStatus() != null) {
			Date nowdate = new Date();
			campaign.setNowDate(nowdate);
		}
		if (campaign.getStartDate() != null) {
			Date t1 = campaignUtil.convertIntegerDateToBeginDate(campaign.getStartDate());
			campaign.setBeginDate(t1);
		}
		if (campaign.getEndDate() != null) {

			Date t2 = campaignUtil.convertIntegerDateToEndDate(campaign.getEndDate());
			campaign.setFinishDate(t2);
		}


		List<CampaignPage> campaignPageList = this.listPageForExcel(campaign);
		List<CampaignExcel> campaignExcelList = new ArrayList<>();
		int i = 1;
		for (CampaignPage campaignPage : campaignPageList) {

			CampaignExcel campaignExcel = new CampaignExcel();
			BeanUtils.copyProperties(campaignPage, campaignExcel);
			campaignExcel.setNum(i);
			//设置活动范围
			if (campaignPage.getCampaignScale().equals(Const.CAMPAIGN_SCALE_IN)) {
				campaignExcel.setCampaignScale("站内");
			}
			if (campaignPage.getCampaignScale().equals(Const.CAMPAIGN_SCALE_OUT)) {
				campaignExcel.setCampaignScale("站外");
			}
			//动态设置状态
			campaignPage.setCampaignStatus(campaignUtil.setCampaignStatusWithDate(campaignPage.getBeginDate(), campaignPage.getFinishDate()));
			//设置活动状态
			if (campaignPage.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_NOT_START.getCode())) {
				campaignExcel.setCampaignStatus("未开始");
			}
			if (campaignPage.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_RUNNING.getCode())) {
				campaignExcel.setCampaignStatus("已开始");
			}
			if (campaignPage.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_CANCELED.getCode())) {
				campaignExcel.setCampaignStatus("已取消");
			}
			if (campaignPage.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_ENDED.getCode())) {
				campaignExcel.setCampaignStatus("已结束");
			}
			//设置捐款
			campaignExcel.setDonateMoney(campaignPage.getCampaignDonateMoney());
			//设置活动时长
			campaignExcel.setServiceHour(campaignPage.getCampaignHour());
			//设置活动类型
			campaignExcel.setCampaignType(volUtil.getServiceType(campaignPage.getCampaignType()));
			//设置活动时间
			String campaignTime = DateUtil.date2String(campaignPage.getBeginDate(), DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
			campaignExcel.setCampaignTime(campaignTime);
			campaignExcelList.add(campaignExcel);
			i++;

		}

		try {
			String fileName = "志愿者活动列表_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
			fileName = new String(fileName.getBytes("UTF-8"), "UTF-8") + ".xls";
			response.setHeader("content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			//导出操作
			ExcelUtil.exportExcel(campaignExcelList, null, "活动列表", CampaignExcel.class, fileName, response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 检查签到信息
	 *
	 * @param campaignVO
	 * @return
	 */
	@Override
	public BaseVo checkSign(CampaignVO campaignVO) {
		CampaignVolunteerRecord obj = new CampaignVolunteerRecord();
		obj.setCampaignId(campaignVO.getId());
		obj.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
		PageHelper.startPage(campaignVO.getPageNum(), campaignVO.getPageSize());
		List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(obj);
		if (CollectionUtils.isNotEmpty(byParam)) {
			for (CampaignVolunteerRecord campaignVolunteerRecord : byParam) {
				Volunteer byId = volunteerDao.findById(campaignVolunteerRecord.getVolunteerId());
				if (byId != null) {
					campaignVolunteerRecord.setRealName(byId.getRealName());
				} else {
					campaignVolunteerRecord.setRealName("");
				}
			}
		}
		PageInfo<CampaignVolunteerRecord> pageInfo = new PageInfo<CampaignVolunteerRecord>(byParam);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);


		return baseVo;
	}

	/**
	 * 检查捐款信息
	 *
	 * @param campaignVO
	 * @return
	 */
	@Override
	public BaseVo checkDonateMoney(CampaignVO campaignVO) {
		CampaignVolunteerRecord obj = new CampaignVolunteerRecord();
		obj.setCampaignId(campaignVO.getId());
		obj.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_YES);
		obj.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
		PageHelper.startPage(campaignVO.getPageNum(), campaignVO.getPageSize());
		List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(obj);
		for (CampaignVolunteerRecord campaignVolunteerRecord : byParam) {
			campaignVolunteerRecord.setDonateMoney(campaignVolunteerRecord.getDonateMoney() == null ? (new BigDecimal(0)) : campaignVolunteerRecord.getDonateMoney());
		}
		PageInfo<CampaignVolunteerRecord> pageInfo = new PageInfo<CampaignVolunteerRecord>(byParam);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 审核活动
	 *
	 * @param campaign
	 * @return
	 */
	@Override
	public BaseVo audit(Campaign campaign) {
		BaseVo baseVo = new BaseVo();
		Campaign obj = campaignDao.queryById(campaign.getId());
		//审批前验证审批状态
		if (obj.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_PASSED.getCode()) ||
				obj.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_REJECTED.getCode())) {
			return new BaseVo(ResultCode.ADUITS_ALREADY_HANDLE.getCode(), ResultCode.ADUITS_ALREADY_HANDLE.getInfo());
		}
		if (obj == null) {
			throwBusinessException("该活动不存在");
		}
		if (obj.getCampaignAudit() != null && obj.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_DRAFT.getCode())) {
			throwBusinessException("该活动不能审批");
		}

		Campaign temp = new Campaign();

		Integer version = obj.getVersion();
		if (version == null) {
			version = 0;
		}
		temp.setVersion(version);
		temp.setCampaignStatus(CampaignStatusEnum.CAMPAIGN_STATUS_NOT_START.getCode());
		temp.setId(campaign.getId());
		temp.setCampaignAudit(campaign.getCampaignAudit());
		//如果退回活动 直接更新数据库 接口退出
		if (campaign.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_REJECTED.getCode())) {
			temp.setCampaignRejectReason(campaign.getCampaignRejectReason());
			campaignDao.update(temp);
			return baseVo;
		}

		Date date = obj.getBeginDate();
		Date now = new Date();
		//判断活动是否过期
		if (date.getTime() - now.getTime() <= 0) {
			baseVo.setData(null);
			baseVo.setCode(ResultCode.WRONG.getCode());
			baseVo.setMsg("活动已经过期");
			return baseVo;
		}

		//如果通过生成redis缓存
		if (campaign.getCampaignAudit().equals(CampaignAuditEnum.CAMPAIGN_AUDIT_STATUS_PASSED.getCode())) {
			//放到redis里
			String key = RedisKey.VOL_ENROLL.getKey() + obj.getId();

			Long time = (date.getTime() - now.getTime()) / 1000;
			volRedisUtil.createCampaign(key, time);

			//发送延迟消息
			double timegap = date.getTime() - now.getTime();
			double gap = timegap / 1000 / 60 / 60;

			String[] param = {""};
			if (gap <= new Double(2)) {
				//如果当前时间距离活动开始时间小于2小时 延迟传0
				List<Long> uids = campaignService.queryEnrollId(obj.getId());
				if (uids != null) {
					Long[] uid = uids.toArray(new Long[uids.size()]);
					MqSendMessage mq = AppPushMessageUtil.pushUserMessageWithBuzIdByAlias(campaign.getId(), MessageTemplateEnum.VOL_CAMPAIGN_DELAY, uid, param, "系统", new Date(), null);
					sendMqPushUtil.sendMqMessage(mq);
				}
			} else {
				//如果当前时间距离活动开始时间大于2小时 延迟传差值
				Long delay = (date.getTime() - now.getTime() - 2 * 60 * 60 * 1000);
				if (delay > 0) {
					MqDelay mqDelayNew = AppPushMessageUtil.pushOrgDelayMessageByAlias(obj.getId(), MessageTemplateEnum.VOL_CAMPAIGN_DELAY, param, delay);
					sendMqPushUtil.sendMqDelayMessage(mqDelayNew);
				} else {
					List<Long> uids = campaignService.queryEnrollId(obj.getId());
					if (uids != null) {
						Long[] uid = uids.toArray(new Long[uids.size()]);
						MqSendMessage mq1 = AppPushMessageUtil.pushUserMessageWithBuzIdByAlias(campaign.getId(), MessageTemplateEnum.VOL_CAMPAIGN_DELAY, uid, param, "系统", new Date(), null);
						sendMqPushUtil.sendMqMessage(mq1);
					}
				}
			}
			Station station = stationDao.findById(obj.getStationId());
			Volunteer par = new Volunteer();
			par.setStationId(obj.getStationId());
			List<Volunteer> byParam = volunteerDao.findByParam(par);
			List<String> carIds = new ArrayList<>();
			for (Volunteer vo : byParam) {
				carIds.add(vo.getCardId());
			}
			List<Long> longList = sysServiceFeign.batchSelectByCardId(carIds);

			if (CollectionUtils.isNotEmpty(longList)) {

				Long[] targetIds = new Long[longList.size()];
				longList.toArray(targetIds);
				String[] params = {station.getStationName(), obj.getCampaignTheme()};
				MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageWithBuzIdByAlias(campaign.getId(), MessageTemplateEnum.CAMPAIGN_NEW_RELEASE, targetIds, params, station.getStationName(), new Date(), null);
				sendMqPushUtil.sendMqMessage(mqSendMessage);
			}
		}
		campaignDao.update(temp);
		return baseVo;
	}


	/**
	 * app使用查询活动详情
	 *
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo appReflect(Long id) {

		Volunteer vi = volUtil.getVolunteerInfo();
		Long userId = vi.getId();

		Campaign campaign = campaignDao.queryById(id);
		if (campaign == null) {
			throwBusinessException("该记录不存在");
		}
		// 查询图片
		FileInfo fileInfo = getFileInfo(campaign);
		campaign.setFileInfo(fileInfo);
		String key = RedisKey.VOL_ENROLL.getKey() + id;
		Boolean flag = cacheUtil.hasKey(key);
		if (flag) {
			Long l = cacheUtil.sGetSetSize(key);
			//因为key中值有个默认-1的值 所有要-1
			campaign.setEnrollNumber(l.intValue() - 1);
		} else {
			int num = campaignVolunteerRecordDao.countByCampaignId(campaign.getId());
			campaign.setEnrollNumber(num);
		}


		Integer x = campaignVolunteerRecordDao.countByCampaignIdAndVolunteerId(id, userId);
		if (x == 1) {
			campaign.setEnrollStatus(Const.VOLUNTEER_STATUS_ENROLL);
		}
		if (x <= 0) {
			campaign.setEnrollStatus(Const.VOLUNTEER_STATUS_NOT_ENROLL);
		}
		CampaignVolunteerRecord temp = new CampaignVolunteerRecord();
		temp.setVolunteerId(userId);
		temp.setCampaignId(id);
		temp.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
		List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(temp);
		if (CollectionUtils.isNotEmpty(byParam)) {
			campaign.setSignTime(byParam.get(0).getSignTime());
		}
		//处理活动类型
		List<String> campaignTypeList = typeUtil.getType(campaign.getCampaignType());
		campaign.setCampaignTypeList(campaignTypeList);
		//是否收藏
		FavouriteVO favouriteVO = new FavouriteVO();
		favouriteVO.setCampaignId(id);
		favouriteVO.setVolunteerId(userId);
		List<Favourite> favourites = favouriteDao.listPage(favouriteVO);
		if (favourites == null) {
			campaign.setIsFavourite(Const.CAMPAIGN_FAVOURITE_NO);
		} else {
			if (favourites.size() > 0) {
				campaign.setIsFavourite(Const.CAMPAIGN_FAVOURITE_YES);
			}
			if (favourites.size() == 0) {
				campaign.setIsFavourite(Const.CAMPAIGN_FAVOURITE_NO);
			}
		}
		//设置startDate等字段
		Map<String, Object> result = campaignUtil.getStartOrEndTime(campaign.getBeginDate(), campaign.getFinishDate());
		campaign.setStartDate((Integer) result.get("startDate"));
		campaign.setEndDate((Integer) result.get("endDate"));
		campaign.setStartTime((String) result.get("startTime"));
		campaign.setEndTime((String) result.get("endTime"));
		if (!campaign.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_CANCELED.getCode())) {
			campaign.setCampaignStatus(campaignUtil.setCampaignStatusWithDate(campaign.getBeginDate(), campaign.getFinishDate()));
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(campaign);
		return baseVo;
	}

	/**
	 * 分页查询审核活动记录
	 *
	 * @param campaignVO
	 * @return
	 */
	@Override
	public BaseVo auditlistPage(CampaignVO campaignVO) {
		BaseVo baseVo = new BaseVo();
		Volunteer vi = volUtil.getVolunteerInfo();
		if (campaignVO.getStartDate() != null) {
			Date t1 = campaignUtil.convertIntegerDateToBeginDate(campaignVO.getStartDate());
			campaignVO.setBeginDate(t1);
		}
		if (campaignVO.getEndDate() != null) {
			Date t2 = campaignUtil.convertIntegerDateToEndDate(campaignVO.getEndDate());
			campaignVO.setFinishDate(t2);
		}
		Station station = stationDao.findById(vi.getStationId());
		List<Long> stationIds = new ArrayList<>();
		//如果是一级机构
		if (station.getStationLevel() == 1) {
			campaignVO.setStationId(vi.getStationId());
			campaignVO.setStationLevel(station.getStationLevel());
		} else {
			campaignVO.setStationId(vi.getStationId());
			campaignVO.setStationList(stationIds);
			campaignVO.setStationLevel(station.getStationLevel());
		}
		PageHelper.startPage(campaignVO.getPageNum(), campaignVO.getPageSize());
		List<CampaignPage> campaignPageList = campaignDao.findAudit(campaignVO);
		for (CampaignPage campaignPage : campaignPageList) {
			String start = DateUtil.date2String(campaignPage.getBeginDate(), DateUtil.DatePattern.YYYYMMDD.getValue());
			start = start.replaceAll("-", "");
			campaignPage.setStartDate(Integer.valueOf(start));
			String end = DateUtil.date2String(campaignPage.getFinishDate(), DateUtil.DatePattern.YYYYMMDD.getValue());
			end = end.replaceAll("-", "");
			campaignPage.setEndDate(Integer.valueOf(end));

		}
		PageInfo<CampaignPage> pageInfo = new PageInfo<CampaignPage>(campaignPageList);
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * app端我的活动列表
	 *
	 * @param campaignVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo appCampaignlistPage(CampaignVO campaignVO) {
		Volunteer vi = volUtil.getVolunteerInfo();
		campaignVO.setStationId(vi.getStationId());
		if (campaignVO.getCampaignStatus() != null) {
			Date nowdate = new Date();
			campaignVO.setNowDate(nowdate);
		}
		//处理时间筛选条件
		if (campaignVO.getStartDate() != null && campaignVO.getEndDate() != null) {
			//合成前端的参数 复制给begindate 和 finishdate
			Date t1 = campaignUtil.convertIntegerDateToBeginDate(campaignVO.getStartDate());
			campaignVO.setBeginDate(t1);
			Date t2 = campaignUtil.convertIntegerDateToEndDate(campaignVO.getEndDate());
			campaignVO.setFinishDate(t2);
		}

		PageHelper.startPage(campaignVO.getPageNum(), campaignVO.getPageSize());
		List<CampaignApp> campaignAppList = campaignDao.appCampaignlistPage(campaignVO);

		if (campaignAppList != null && campaignAppList.size() > 0) {
			for (CampaignApp campaignApp : campaignAppList) {
				String key = RedisKey.VOL_ENROLL.getKey() + campaignApp.getId();
				//从redis中判断key是否存在 如果存在则取key的长度 如果不存在就取数据库的值
				setEnrollNumber(campaignApp, key);

				CampaignVolunteerRecord obj = new CampaignVolunteerRecord();
				obj.setCampaignId(campaignApp.getId());
				obj.setVolunteerId(campaignVO.getVolunteerId());
				List<CampaignVolunteerRecord> volunteerCampaign = campaignVolunteerRecordDao.findVolunteerCampaign(obj);
				if (volunteerCampaign != null && volunteerCampaign.size() == 1) {
					campaignApp.setSignStatus(volunteerCampaign.get(0).getSignStatus());
				}
				//处理活动类型 显示中文
				List<String> campaignTypeList = new ArrayList<>();
				campaignTypeList = typeUtil.getType(campaignApp.getCampaignType());
				campaignApp.setCampaignTypeList(campaignTypeList);

				BaseVo b1 = fileServiceFeign.findById(campaignApp.getCampaignImage());
				String s = JSON.toJSONString(b1.getData());
				FileInfo fileInfo = JSON.parseObject(s, FileInfo.class);
				if (fileInfo == null) {
					throwBusinessException("文件不存在");
				}
				campaignApp.setFileInfo(fileInfo);
				//设置startDate等字段

				Map<String, Object> result = campaignUtil.getStartOrEndTime(campaignApp.getBeginDate(), campaignApp.getFinishDate());
				campaignApp.setStartDate((Integer) result.get("startDate"));
				campaignApp.setEndDate((Integer) result.get("endDate"));
				campaignApp.setStartTime((String) result.get("startTime"));
				campaignApp.setEndTime((String) result.get("endTime"));
				if (!campaignApp.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_CANCELED.getCode())) {
					campaignApp.setCampaignStatus(campaignUtil.setCampaignStatusWithDate(campaignApp.getBeginDate(), campaignApp.getFinishDate()));
				}

			}
		}
		PageInfo<CampaignApp> pageInfo = new PageInfo<CampaignApp>(campaignAppList);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);

		return baseVo;
	}


	/**
	 * app端报名
	 *
	 * @param campaignId
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo appEnroll(Long campaignId) {
		Volunteer vi = volUtil.getVolunteerInfo();

		Campaign temp = campaignDao.queryById(campaignId);
		if (temp == null) {
			throw new BusinessException(ResultCode.WARNING_CAMPAIGN_NOT_EXIST.getInfo(), ResultCode.WARNING_CAMPAIGN_NOT_EXIST.getCode());
		}
		//判断活动已取消
		if (temp.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_CANCELED.getCode())){
			throw new BusinessException(ResultCode.WARNING_CAMPAIGN_CANCELED.getInfo(), ResultCode.WARNING_CAMPAIGN_CANCELED.getCode());
		}
		volRedisUtil.checkCampaignVols(RedisKey.VOL_ENROLL.getKey() + campaignId, temp.getCampaignNumber(), vi.getId().toString());

		// 判断当前时间是否过期
		Date date = temp.getBeginDate();
		Date now = new Date();
		if (now.after(date)) {
			throw new BusinessException(ResultCode.WARNING_CAMPAIGN_IN_PROGRESS_NOT_ENROLL.getInfo(), ResultCode.WARNING_CAMPAIGN_IN_PROGRESS_NOT_ENROLL.getCode());
		}
		Long gap = (date.getTime() - now.getTime()) / 1000;

		String key = "campaign:" + campaignId + ":volunteerId:" + vi.getId();
		Integer count = (Integer) cacheUtil.get(key);
		if (count == null) {
			count = 0;
		}
		count = count + 1;

		cacheUtil.set(key, count, gap);

		Long stationId = vi.getStationId();

		CampaignVolunteerRecord obj = new CampaignVolunteerRecord();
		obj.setVolunteerId(vi.getId());
		obj.setCampaignId(campaignId);
		obj.setStationId(stationId);
		List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(obj);
		if (byParam != null && byParam.size() > 0 && byParam.size() == 1) {
			CampaignVolunteerRecord x = new CampaignVolunteerRecord();
			x.setId(byParam.get(0).getId());
			x.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
			Integer version = byParam.get(0).getVersion();
			if (version == null) {
				version = 0;
			}
			x.setVersion(version);
			campaignVolunteerRecordDao.update(x);
		} else {
			CampaignVolunteerRecord campaignVolunteerRecord = new CampaignVolunteerRecord();
			campaignVolunteerRecord.setVolunteerId(vi.getId());
			campaignVolunteerRecord.setCampaignId(campaignId);
			campaignVolunteerRecord.setStationId(stationId);
			campaignVolunteerRecord.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_NO);
			campaignVolunteerRecord.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
			campaignVolunteerRecord.setCreateTime(new Date());
			campaignVolunteerRecord.setVersion(0);
			campaignVolunteerRecordDao.add(campaignVolunteerRecord);
		}

		int maxnum = temp.getCampaignNumber();
		int enrollnum = campaignVolunteerRecordDao.countByCampaignId(campaignId);
		if (enrollnum >= maxnum) {
			//更新活动满员状态
			Campaign campaign = new Campaign();
			campaign.setId(temp.getId());
			campaign.setCampaignFull(Const.CAMPAIGN_FULL_YES);
			Integer version = temp.getVersion();
			if (version == null) {
				version = 0;
			}
			campaign.setVersion(version);
			campaignDao.update(campaign);
		}
		return new BaseVo();
	}

	/**
	 * app端取消报名
	 *
	 * @param campaignId
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo appUnEnroll(Long campaignId) {
		Volunteer vi = volUtil.getVolunteerInfo();

		Campaign temp = campaignDao.queryById(campaignId);
		if (temp == null) {
			throw new BusinessException(ResultCode.WARNING_CAMPAIGN_NOT_EXIST.getInfo(), ResultCode.WARNING_CAMPAIGN_NOT_EXIST.getCode());
		}

		// 判断当前时间是否过期
		Date date = temp.getBeginDate();
		Date now = new Date();

		if (now.after(date)) {
			throw new BusinessException(ResultCode.WARNING_CAMPAIGN_IN_PROGRESS_NOT_UNENROLL.getInfo(), ResultCode.WARNING_CAMPAIGN_IN_PROGRESS_NOT_ENROLL.getCode());
		}
		Long gap = (date.getTime() - now.getTime()) / 1000;

		String key = "campaign:" + campaignId + ":volunteerId:" + vi.getId();
		Integer count = (Integer) cacheUtil.get(key);
		if (count != null) {
			if (count > Const.CAMPAIGN_UNENROLL_TIMES) {
				throw new BusinessException(ResultCode.WARNING_OVER_THREE_TIMES.getInfo(), ResultCode.WARNING_OVER_THREE_TIMES.getCode());
			}
		}

		cacheUtil.set(key, count, gap);

		Long stationId = vi.getStationId();
		CampaignVolunteerRecord obj = new CampaignVolunteerRecord();
		obj.setVolunteerId(vi.getId());
		obj.setCampaignId(campaignId);
		obj.setStationId(stationId);
		Long recordId = null;
		List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(obj);
		if (byParam != null) {
			if (byParam.size() == 1) {
				recordId = byParam.get(0).getId();
			} else {
				throwBusinessException("活动记录参数异常");
			}
		} else {
			throwBusinessException("未报名");
		}
		if (recordId != null) {
			CampaignVolunteerRecord x = byParam.get(0);
			CampaignVolunteerRecord campaignVolunteerRecord = new CampaignVolunteerRecord();
			campaignVolunteerRecord.setId(recordId);
			campaignVolunteerRecord.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_NO);
			campaignVolunteerRecord.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_INACTIVE);
			Integer version = x.getVersion();
			if (version == null) {
				version = 0;
			}
			campaignVolunteerRecord.setVersion(version);
			campaignVolunteerRecordDao.update(campaignVolunteerRecord);
		}

		int maxnum = temp.getCampaignNumber();
		int enrollnum = campaignVolunteerRecordDao.countByCampaignId(campaignId);
		if (enrollnum < maxnum) {
			//更新活动满员状态
			Campaign campaign = new Campaign();
			campaign.setId(temp.getId());
			campaign.setCampaignFull(Const.CAMPAIGN_FULL_NO);
			Integer version = temp.getVersion();
			if (version == null) {
				version = 0;
			}
			campaign.setVersion(version);
			campaignDao.update(campaign);
		}
		//删除redis中 这个key下的报名人
		cacheUtil.setRemove(RedisKey.VOL_ENROLL.getKey() + campaignId, vi.getId());

		if (count.equals(Const.CAMPAIGN_UNENROLL_TIMES)) {
			return new BaseVo(ResultCode.WARNING_THREE_TIMES.getCode(), ResultCode.WARNING_THREE_TIMES.getInfo());
		}
		return new BaseVo();
	}

	/**
	 * app端我的活动
	 *
	 * @param campaignVO
	 * @return
	 */
	@Override
	public BaseVo appMyCampaignlistPage(CampaignVO campaignVO) {
		Volunteer vi = volUtil.getVolunteerInfo();
		Long userId = vi.getId();
		campaignVO.setVolunteerId(userId);
		Date nowdate = new Date();
		campaignVO.setNowDate(nowdate);
		PageHelper.startPage(campaignVO.getPageNum(), campaignVO.getPageSize());
		List<CampaignApp> campaignAppList = campaignDao.appMyCampaignlistPage(campaignVO);
		if (CollectionUtils.isNotEmpty(campaignAppList)) {
			for (CampaignApp campaignApp : campaignAppList) {
				String key = RedisKey.VOL_ENROLL.getKey() + campaignApp.getId();
				//从redis中判断key是否存在 如果存在则取key的长度 如果不存在就取数据库的值
				setEnrollNumber(campaignApp, key);
				//处理活动类型
				List<String> campaignTypeList = typeUtil.getType(campaignApp.getCampaignType());
				campaignApp.setCampaignTypeList(campaignTypeList);

				if (campaignApp.getCampaignImage() != null) {
					// 查询图片
					BaseVo b1 = fileServiceFeign.findById(campaignApp.getCampaignImage());
					String s = JSON.toJSONString(b1.getData());
					FileInfo fileInfo = JSON.parseObject(s, FileInfo.class);
					if (fileInfo == null) {
						throwBusinessException("文件不存在");
					}
					campaignApp.setFileInfo(fileInfo);
				}
				//设置startDate等字段
				Map<String, Object> result = campaignUtil.getStartOrEndTime(campaignApp.getBeginDate(), campaignApp.getFinishDate());
				campaignApp.setStartDate((Integer) result.get("startDate"));
				campaignApp.setEndDate((Integer) result.get("endDate"));
				campaignApp.setStartTime((String) result.get("startTime"));
				campaignApp.setEndTime((String) result.get("endTime"));
				if (!campaignApp.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_CANCELED.getCode())) {
					campaignApp.setCampaignStatus(campaignUtil.setCampaignStatusWithDate(campaignApp.getBeginDate(), campaignApp.getFinishDate()));
				}
			}
		}
		PageInfo<CampaignApp> pageInfo = new PageInfo<CampaignApp>(campaignAppList);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 通用方法 给campaignApp赋值报名人数
	 *
	 * @param campaignApp
	 * @param key
	 */
	private void setEnrollNumber(CampaignApp campaignApp, String key) {
		Boolean flag = cacheUtil.hasKey(key);
		if (flag) {
			Long l = cacheUtil.sGetSetSize(key);
			//因为key中值有个默认-1的值 所有要-1
			campaignApp.setEnrollNumber(l.intValue() - 1);
		} else {
			int num = campaignVolunteerRecordDao.countByCampaignId(campaignApp.getId());
			campaignApp.setEnrollNumber(num);
		}
	}

	/**
	 * app端查询活动记录
	 *
	 * @param campaignId
	 * @return
	 */
	@Override
	public BaseVo appLog(Long campaignId) {
		Volunteer vi = volUtil.getVolunteerInfo();

		CampaignVolunteerRecord obj = new CampaignVolunteerRecord();
		obj.setCampaignId(campaignId);
		obj.setVolunteerId(vi.getId());
		obj.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
		List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(obj);

		BaseVo baseVo = new BaseVo();
		if (CollectionUtils.isNotEmpty(byParam)) {
			CampaignVolunteerRecord campaignVolunteerRecord = byParam.get(0);
			baseVo.setData(campaignVolunteerRecord);
		}
		return baseVo;
	}

	/**
	 * app端查询报名人数
	 *
	 * @param campaignId
	 * @return
	 */
	@Override
	public BaseVo appCheckSign(Long campaignId) {
		Campaign obj = campaignDao.queryById(campaignId);
		if (obj == null) {
			throwBusinessException("该活动不存在");
		}
		CampaignVolunteerRecord temp = new CampaignVolunteerRecord();
		temp.setCampaignId(campaignId);
		temp.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
		List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(temp);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(byParam);
		return baseVo;
	}

	/**
	 * 更新捐款信息
	 *
	 * @param campaignVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo updateDonateMoney(CampaignVO campaignVO) {

		CampaignVolunteerRecord campaignVolunteerRecord = new CampaignVolunteerRecord();
		campaignVolunteerRecord.setId(campaignVO.getId());
		campaignVolunteerRecord.setDonateMoney(campaignVO.getCampaignDonateMoney());
		campaignVolunteerRecordDao.update(campaignVolunteerRecord);
		return new BaseVo();
	}

	/**
	 * 查询为生成excel专用
	 *
	 * @param campaign
	 * @return
	 */
	@Override
	public List<CampaignPage> listPageForExcel(Campaign campaign) {
		Volunteer vi = volUtil.getVolunteerInfo();

		// 取出下级机构和自己
		List<Long> stationIds = new ArrayList<>();
		List<Station> tree = stationService.childTree(vi.getStationId());
		for (Station station : tree) {
			stationIds.add(station.getId());
		}
		campaign.setStationList(stationIds);
		List<CampaignPage> campaignPageList = campaignDao.findByConditionPageForExcel(campaign);
		return campaignPageList;
	}

	private void exportExcelInfo(List<CampaignRecordExcelInfo> excelInfo, Boolean flag, HttpServletResponse response) {
		Workbook wb = null;
		try {
			wb = ExcelExportUtil.exportExcel(new ExportParams(null, "捐款金额错误sheet"), CampaignRecordExcelInfo.class, excelInfo);
			//获取到你这个Excel的长和宽
			Sheet sheet = wb.getSheetAt(0);
			Row row = sheet.getRow(0);
			int rowNum = sheet.getLastRowNum();
			int colNum = row.getPhysicalNumberOfCells();

			//创建字体对象，注意这不是awt包下的，是poi给我们封装了一个
			Font font = wb.createFont();
			font.setBold(true);
			short in = HSSFColor.RED.index;
			font.setColor(in);
			font.setFontHeightInPoints((short) 12);
			if (!flag) {
				//设置最后一列 错误原因的样式
				HSSFCellStyle colstyle = (HSSFCellStyle) wb.createCellStyle();
				colstyle.setAlignment(HorizontalAlignment.CENTER);
				colstyle.setFont(font);
				Cell col = row.getCell(5);
				col.setCellStyle(colstyle);
			}

			for (int i = 1; i <= rowNum; i++) {
				row = sheet.getRow(i);
				int j = 0;
				while (j < colNum) {
					//这里我们就获得了Cell对象，对他进行操作就可以了
					Cell cell = row.getCell((short) j);
					String value = row.getCell((short) j).toString();
					value = value.trim();
					if (value.startsWith("错误")) {
						HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();
						style.setAlignment(HorizontalAlignment.CENTER);
						style.setFont(font);
						cell.setCellStyle(style);
					}
					j++;
				}
			}
			response.reset();
			String fileName = new String("捐款金额回执".getBytes("UTF-8"), "UTF-8") + ".xls";
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setHeader("content-type", "application/octet-stream");
			wb.write(response.getOutputStream());
			wb.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 导入excel
	 *
	 * @param file
	 * @return
	 */
	@Override
	@Transactional
	public void importDonateMoneyExcel(MultipartFile file, Long campaignId, HttpServletResponse response) throws IOException {
		String regex = "^[0-9]+(\\.[0-9]{1,2})?$";
		String idcardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";

		Campaign campaign = campaignDao.queryById(campaignId);
		if (campaign == null) {
			throwBusinessException("活动不存在");
		}
		CampaignVolunteerRecord campaignVolunteerRecord = new CampaignVolunteerRecord();
		campaignVolunteerRecord.setCampaignId(campaignId);
		campaignVolunteerRecord.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_YES);
		//查询出来签到记录
		List<CampaignVolunteerRecord> recordParams = campaignVolunteerRecordDao.findByParam(campaignVolunteerRecord);
		if (recordParams == null || recordParams.size() <= 0) {
			throwBusinessException("该活动没有签到人员");
		}

		List<CampaignRecordExcel> importExcels = ExcelUtil.importExcel(file, 0, 1, CampaignRecordExcel.class);
		//excel导入参数集合
		List<CampaignRecordExcel> campaignRecordExcels = new ArrayList<>();
		//先过滤掉空的参数 和 没填全的参数
		for (CampaignRecordExcel importExcel : importExcels) {
			if (StringUtil.isNotEmpty(importExcel.getCardId()) &&
					StringUtil.isNotEmpty(importExcel.getVolunteerName()) &&
					importExcel.getDonateMoney() != null) {
				campaignRecordExcels.add(importExcel);
			}
		}

		//存身份证
		List<String> cardIds = new ArrayList<>();
		String orderlist = "";
		if (CollectionUtils.isNotEmpty(campaignRecordExcels)) {
			if (recordParams.size() != campaignRecordExcels.size()) {
				throwBusinessException("签到人员数量与文件记录数量不一致");
			}
			//校验excel的数据是否与签到记录吻合
			List<CampaignRecordExcel> paramRecords = new ArrayList<>();
			for (CampaignVolunteerRecord campaignVolunteerRecord1 : recordParams) {
				CampaignRecordExcel temp = new CampaignRecordExcel();
				temp.setCardId(campaignVolunteerRecord1.getIdcard());
				temp.setVolunteerName(campaignVolunteerRecord1.getRealName());
				paramRecords.add(temp);
			}
			//比较传入参数和签到记录是否一致
			for (int i = 0; i < campaignRecordExcels.size(); i++) {
				//excel导入的
				CampaignRecordExcel t1 = campaignRecordExcels.get(i);
				//签到记录的
				CampaignRecordExcel t2 = paramRecords.get(i);
				if (!t1.equals(t2)) {
					throwBusinessException("导入参数的身份证和姓名和签到参数不一致");
				}
			}
			List<CampaignRecordExcelInfo> excelInfos = new ArrayList<>();
			int index = 0;
			Boolean flag = true;
			//再对参数进行格式校验
			for (CampaignRecordExcel excel : campaignRecordExcels) {
				CampaignRecordExcelInfo campaignRecordExcelInfo = new CampaignRecordExcelInfo();
				BeanUtils.copyProperties(excel, campaignRecordExcelInfo);
				if (StringUtil.isEmpty(excel.getCardId())) {
					campaignRecordExcelInfo.setResult(Const.IMPORT_MONEY_FAIL);
					campaignRecordExcelInfo.setReason(Const.IMPORT_MONEY_FAIL_REASON_IDCARD_NULL);
					index = index - 1;
				} else if (!excel.getCardId().matches(idcardRegex)) {
					campaignRecordExcelInfo.setResult(Const.IMPORT_MONEY_FAIL);
					campaignRecordExcelInfo.setReason(Const.IMPORT_MONEY_FAIL_REASON_IDCARD_FORMAT_WRONG);
					index = index - 1;
				} else if (excel.getDonateMoney() == null) {
					campaignRecordExcelInfo.setResult(Const.IMPORT_MONEY_FAIL);
					campaignRecordExcelInfo.setReason(Const.IMPORT_MONEY_FAIL_REASON_MONEY_NULL);
					index = index - 1;
				} else if (!excel.getDonateMoney().toString().matches(regex)) {
					campaignRecordExcelInfo.setResult(Const.IMPORT_MONEY_FAIL);
					campaignRecordExcelInfo.setReason(Const.IMPORT_MONEY_FAIL_REASON_MONEY_FORMAT_WRONG);
					index = index - 1;
				} else {
					campaignRecordExcelInfo.setResult(Const.IMPORT_MONEY_SUCCESS);
					campaignRecordExcelInfo.setReason("");
					index = index + 1;
				}
				excelInfos.add(campaignRecordExcelInfo);
			}
			//如果index的值小于有效记录数 就意味着有失败的记录
			if (index < campaignRecordExcels.size()) {
				flag = false;
			} else {
				flag = true;
			}
			//如果错了 输出流
			if (!flag) {
				exportExcelInfo(excelInfos, flag, response);
			} else {
				for (CampaignRecordExcel campaignRecordExcel : campaignRecordExcels) {
					//查询志愿者id
					if (StringUtil.isNotEmpty(campaignRecordExcel.getCardId())) {
						cardIds.add(campaignRecordExcel.getCardId());
						orderlist = orderlist + campaignRecordExcel.getCardId() + ",";
					}
				}
				orderlist.substring(orderlist.length() - 1, orderlist.length());
				//用来存储更新语句集合
				List<CampaignVolunteerRecord> recordList = new ArrayList<>();
				//根据身份证号批量查询志愿者id
				List<Volunteer> volunteerList = volunteerDao.batchSelectByCardIds(cardIds, orderlist);
				if (CollectionUtils.isNotEmpty(volunteerList)) {
					for (int i = 0; i < volunteerList.size(); i++) {
						Volunteer obj = volunteerList.get(i);
						for (int j = 0; j < campaignRecordExcels.size(); j++) {
							CampaignRecordExcel campaignRecordExcel = campaignRecordExcels.get(j);
							if (obj.getCardId().equals(campaignRecordExcel.getCardId())) {
								CampaignVolunteerRecord temp = new CampaignVolunteerRecord();
								temp.setVolunteerId(obj.getId());
								temp.setCampaignId(campaignId);
								temp.setDonateMoney(campaignRecordExcel.getDonateMoney());
								recordList.add(temp);
							}
						}
					}
					//批量查询主键id
					if (CollectionUtils.isNotEmpty(recordList)) {
						for (int i = 0; i < recordList.size(); i++) {
							CampaignVolunteerRecord tt = new CampaignVolunteerRecord();
							tt.setVolunteerId(recordList.get(i).getVolunteerId());
							tt.setCampaignId(campaignId);
							List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(tt);
							if (byParam != null && byParam.size() == 1) {
								recordList.get(i).setId(byParam.get(0).getId());
								recordList.get(i).setVersion(byParam.get(0).getVersion());
							} else {
								recordList.remove(i);
							}
						}
						if (recordList == null || recordList.size() <= 0) {
							throwBusinessException("导入结果异常");
						} else {
							// 批量更新记录表
							campaignVolunteerRecordDao.batchUpdateRecordMoney(recordList);
							//输出成功流
							exportExcelInfo(excelInfos, flag, response);
						}
					}
				} else {
					throwBusinessException("身份证数据异常");
				}
			}
		} else {
			throwBusinessException("模板数据异常");
		}
	}

	/**
	 * 查询该活动下的报名人
	 *
	 * @param campaignId
	 * @return
	 */
	@Override
	public List<Long> queryEnrollId(Long campaignId) {
		//查询出参加该活动的人
		List<Long> volIds = new ArrayList<>();
		CampaignVolunteerRecord temp = new CampaignVolunteerRecord();
		temp.setCampaignId(campaignId);
		temp.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
		List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(temp);
		if (byParam != null && byParam.size() > 0) {
			for (CampaignVolunteerRecord campaignVolunteerRecord : byParam) {
				volIds.add(campaignVolunteerRecord.getVolunteerId());
			}
			List<String> stringList = volunteerDao.batchSelectByIds(volIds);
			if (CollectionUtils.isNotEmpty(stringList)) {
				List<Long> longList = sysServiceFeign.batchSelectByCardId(stringList);
				if (longList != null && longList.size() > 0) {
					return longList;
				}
			}
		}

		return null;
	}

	/**
	 * 检查该活动是否取消
	 *
	 * @param campaignId
	 * @return
	 */
	@Override
	public Boolean checkCampaignStatusById(Long campaignId) {
		Campaign campaign = campaignDao.queryById(campaignId);
		if (campaign == null) {
			return false;
		}

		if (campaign.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_CANCELED)) {
			return false;
		}

		return true;
	}

	/**
	 * 下载模板
	 */
	@Override
	public void downloadTemplate(HttpServletResponse response) throws IOException {
		try {
			String fileName = new String("捐款金额模板".getBytes("UTF-8"), "UTF-8") + ".xls";
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setHeader("content-type", "application/octet-stream");

			List<CampaignRecordExcel> campaignRecordExcels = new ArrayList<>();
			CampaignRecordExcel campaignRecordExcel = new CampaignRecordExcel();
			campaignRecordExcels.add(campaignRecordExcel);

			ExcelUtil.exportExcel(campaignRecordExcels, null, "捐款金额模板", CampaignRecordExcel.class, fileName, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
