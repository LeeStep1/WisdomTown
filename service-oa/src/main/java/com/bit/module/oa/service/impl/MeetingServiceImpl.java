package com.bit.module.oa.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;

import com.bit.module.oa.bean.*;
import com.bit.module.oa.constant.redis.RedisConstant;
import com.bit.module.oa.dao.*;
import com.bit.module.oa.enums.LogOperateEnum;
import com.bit.module.oa.enums.LogTypeEnum;
import com.bit.module.oa.enums.MeetingRoomStatusEnum;
import com.bit.module.oa.enums.MeetingStatusEnum;
import com.bit.module.oa.service.MeetingService;
import com.bit.module.oa.utils.ApplyNoGenerator;
import com.bit.module.oa.utils.PushUtil;
import com.bit.module.oa.utils.RedisDelayHandler;
import com.bit.module.oa.vo.meeting.*;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Meeting的Service实现类
 *
 * @author codeGenerator
 */
@Service("meetingService")
@Transactional
public class MeetingServiceImpl extends BaseService implements MeetingService {

    private static final Logger logger = LoggerFactory.getLogger(MeetingServiceImpl.class);

    @Autowired
    private MeetingDao meetingDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private MeetingRoomDao meetingRoomDao;

    @Autowired
    private MeetingExecutorDao meetingExecutorDao;

    @Autowired
    private MeetingSummaryDao  meetingSummaryDao;

    @Autowired
    private RedisDelayHandler redisDelayHandler;

    @Autowired
    private PushUtil pushUtil;


    // 一次批量插入大小
    private static final Integer DATA_SIZE = 500;

    /**
     * 根据条件查询Meeting
     *
     * @param meetingVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(MeetingVO meetingVO) {
        PageHelper.startPage(meetingVO.getPageNum(), meetingVO.getPageSize());
        meetingVO.setOrder("desc");
        meetingVO.setOrderBy("id");
        // 只能看到自己的会议
        meetingVO.setUserId(getCurrentUserInfo().getId());
        List<MeetingPageVO> list = meetingDao.findByConditionPage(meetingVO);
        PageInfo<MeetingPageVO> pageInfo = new PageInfo<>(list);
        return new BaseVo(pageInfo);
    }

    @Override
    public BaseVo findAuditResultByConditionPage(MeetingVO meetingVO) {
        PageHelper.startPage(meetingVO.getPageNum(), meetingVO.getPageSize());
        meetingVO.setOrder("desc");
        meetingVO.setOrderBy("id");
        // 选择审批会议状态
        meetingVO.setStatusList(selectMeetingStatus(meetingVO.getStatus()));

        // 只能看到自己能审批的会议
        meetingVO.setApproverId(getCurrentUserInfo().getId());
        List<MeetingPageVO> list = meetingDao.findAuditPage(meetingVO);
        PageInfo<MeetingPageVO> pageInfo = new PageInfo<>(list);
        return new BaseVo(pageInfo);
    }

    @Override
    public BaseVo findByExecutorPage(MeetingExecutorVO meetingExecutor) {
        PageHelper.startPage(meetingExecutor.getPageNum(), meetingExecutor.getPageSize());
        meetingExecutor.setOrder("desc");
        meetingExecutor.setOrderBy("id");

        List<MeetingExecutorPageVO> list = meetingExecutorDao.findByConditionPage(meetingExecutor);
        PageInfo<MeetingExecutorPageVO> pageInfo = new PageInfo<>(list);
        return new BaseVo(pageInfo);
    }

    /**
     * 通过主键查询单个Meeting
     *
     * @param id
     * @return
     */
    @Override
    public Meeting findById(Long id) {
        return meetingDao.findById(id);
    }

    /**
     * 保存Meeting
     *
     * @param meeting
     */
    @Override
    public void add(Meeting meeting) {
        // 仅检查会议室是否已启用
        existMeetingRoom(meeting);
        Date addAt = new Date();
        String no = generateApplyNo();
        meeting.setNo(no);
        meeting.setStatus(MeetingStatusEnum.UNPUBLISHED.getKey());
        UserInfo userInfo = getCurrentUserInfo();
        meeting.setPublisherId(userInfo.getId());
        meeting.setPublisherName(userInfo.getRealName());
        meeting.setCreateAt(addAt);
        meeting.setUpdateAt(addAt);
        meetingDao.insert(meeting);

    }

    /**
     * 更新Meeting
     *
     * @param meeting
     */
    @Override
    public void update(Meeting meeting) {
        Meeting toCheck = meetingDao.findById(meeting.getId());
        if (toCheck == null) {
            logger.error("会议单不存在, {}", meeting);
            throw new BusinessException("会议单不存在");
        }
        // 只有未发布和已驳回状态下可以修改
        if (!MeetingStatusEnum.UNPUBLISHED.getKey().equals(toCheck.getStatus())
                && !MeetingStatusEnum.REJECT.getKey().equals(toCheck.getStatus())) {
            logger.error("会议 : {} " + MeetingStatusEnum.getDescriptionByKey(toCheck.getStatus()), toCheck.getId());
            throw new BusinessException("会议" + MeetingStatusEnum.getDescriptionByKey(toCheck.getStatus()));
        }
        meeting.setStatus(MeetingStatusEnum.UNPUBLISHED.getKey());
        meeting.setUpdateAt(new Date());
        // 如果会议室被修改再次检查会议室是否已启用
        if (!toCheck.getRoomId().equals(meeting.getRoomId())) {
            existMeetingRoom(meeting);
        }
        meeting.setVersion(toCheck.getVersion());
        meetingDao.updateByPrimaryKey(meeting);
    }

    /**
     * 删除Meeting
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        checkMeetingCurrentStatus(id, MeetingStatusEnum.UNPUBLISHED);
        meetingDao.deleteByPrimaryKey(id);
    }

    @Override
    public void publish(Meeting meeting) {
        // 如果尚未保存过的就新增
        if (meeting.getId() == null) {
            meeting.checkAdd();
            this.add(meeting);
        // 如果已经保存的检查是否有修改项，有就更新，没有直接发布
        } else {
            if (updateParamIfNotNull(meeting)) {
                this.update(meeting);
            }
        }
        Date publishAt = new Date();
        Meeting toPublish = meetingDao.findById(meeting.getId());
        // 检查必要参数是否为空, 时间校验
        checkMeetingPublishParam(toPublish, publishAt);
        Meeting toUpdate = new Meeting();
        toUpdate.setId(toPublish.getId());
        toUpdate.setUpdateAt(publishAt);
        toUpdate.setPublishAt(publishAt);
        toUpdate.setStatus(MeetingStatusEnum.TO_AUDIT.getKey());
        toUpdate.setVersion(toPublish.getVersion());
        int result = meetingDao.updateByPrimaryKey(toUpdate);

        if (result <= 0) {
            logger.error("更新失败, {}", meeting);
            throw new BusinessException("更新失败");
        }

        Log log = Log.getLogByOperateAndType(meeting.getId(), getCurrentUserInfo(), LogOperateEnum.MEETING_PUBLISH,
                LogTypeEnum.MEETING);
        logDao.add(log);

        // 在会议开始前10分钟内还未进行审批的会议，默认驳回

        Date delayAt = new Date(toPublish.getStartTime().toInstant().toEpochMilli() - 10 * 60 * 1000);
        redisDelayHandler.setDelayZSet(RedisConstant.MEETING_REJECT_ZSET, toPublish.getId(), delayAt);

        // 发送反馈隐患消息通知
        pushUtil.sendMessage(toPublish.getId(), MessageTemplateEnum.MEETING_APPLY, TargetTypeEnum.USER,
                new Long[]{toPublish.getApproverId()},
                new String[]{getCurrentUserInfo().getRealName(), MessageTemplateEnum.MEETING_APPLY.getInfo()},
                getCurrentUserInfo().getRealName(), toPublish.getVersion());
    }

    private void checkMeetingPublishParam(Meeting meeting, Date publishAt) {
        // 检查必要参数是否为空
        if (meeting.getNeedCheckIn() == null || meeting.getRemindSet() == null || meeting.getApproverId() == null
                || meeting.getHostId() == null || CollectionUtils.isEmpty(meeting.getParticipants())
                || meeting.getDepId() == null) {
            logger.error("会议必要参数不能为空, {}", meeting);
            throw new BusinessException("会议必要参数不能为空");
        }
        meeting.checkMeetingTime(publishAt);
        // 会议室占用检查
        if (meetingDao.countOccupyMeeting(meeting.getRoomId(), meeting.getStartTime(), meeting.getEndTime()) > 0) {
            logger.error("当前时间会议室已被占用 : {}", meeting);
            throw new BusinessException("当前时间会议室已被占用");
        }
    }


    private boolean updateParamIfNotNull(Meeting meeting) {
        return StringUtils.isNotEmpty(meeting.getTitle()) || meeting.getStartTime() != null
                || meeting.getEndTime() != null || CollectionUtils.isNotEmpty(meeting.getParticipants())
                || StringUtils.isNotEmpty(meeting.getContent()) || StringUtils.isNotEmpty(meeting.getAttactIds())
                || meeting.getHostId() != null || StringUtils.isNotEmpty(meeting.getHostName())
                || meeting.getApproverId() != null || StringUtils.isNotEmpty(meeting.getApproverName())
                || meeting.getReporterId() != null || StringUtils.isNotEmpty(meeting.getReporterName());
    }

    @Override
    public void reject(MeetingVO meeting) {
        Meeting rejected = checkMeetingCurrentStatus(meeting.getId(), MeetingStatusEnum.TO_AUDIT);
        UserInfo userInfo = getCurrentUserInfo();
        // 检查审核权限
        checkAuditMeetingAuth(userInfo, rejected);
        // 更新会议状态
        updateMeetingStatus(rejected, MeetingStatusEnum.REJECT);

        Log log = Log.getLogByOperateAndTypeAndContent(rejected.getId(), userInfo, LogOperateEnum.MEETING_REJECT,
                LogTypeEnum.MEETING, meeting.getRejectReason());
        logDao.add(log);

        // 发送通过会议审核消息通知
        pushUtil.sendMessage(rejected.getId(), MessageTemplateEnum.MEETING_REJECT, TargetTypeEnum.USER,
                new Long[]{rejected.getPublisherId()},
                new String[]{MessageTemplateEnum.MEETING_REJECT.getInfo(), meeting.getRejectReason()},
                getCurrentUserInfo().getRealName());
    }

    @Override
    public void audit(Meeting meeting) {
        Meeting audited = checkMeetingCurrentStatus(meeting.getId(), MeetingStatusEnum.TO_AUDIT);
        Date now = new Date();
        UserInfo userInfo = getCurrentUserInfo();
        // 检查审核权限
        checkAuditMeetingAuth(userInfo, audited);
        audited.checkMeetingTime(now);
        updateMeetingStatus(audited, MeetingStatusEnum.READY);

        Log log = Log.getLogByOperateAndType(audited.getId(), userInfo, LogOperateEnum.MEETING_AUDIT, LogTypeEnum.MEETING);
        logDao.add(log);

        // 发送开始会议的redis队列
        redisDelayHandler.setDelayZSet(RedisConstant.MEETING_START_ZSET, audited.getId(), audited.getStartTime());

        // 发送通过会议审核消息通知
        pushUtil.sendMessage(audited.getId(), MessageTemplateEnum.MEETING_AUDIT, TargetTypeEnum.USER,
                new Long[]{audited.getPublisherId()},
                new String[]{MessageTemplateEnum.MEETING_AUDIT.getInfo()},
                getCurrentUserInfo().getRealName());

        // 若不需要签到则不预先生成签到信息
        if (!audited.getNeedCheckIn()) {
            return;
        }

        // 生成批量会议签到信息
        List<SimpleUser> users = audited.parseParticipants();
        List<MeetingExecutor> executorList = users.stream().map(user -> {
            MeetingExecutor meetingExecutor = new MeetingExecutor();
            meetingExecutor.setMeetingId(audited.getId());
            meetingExecutor.setParticipantId(user.getId());
            meetingExecutor.setParticipantName(user.getName());
            meetingExecutor.setCreateAt(now);
            return meetingExecutor;
        }).collect(Collectors.toList());

        int size = executorList.size() / DATA_SIZE + 1;

        for (int i = 0; i < size; i++) {
            List<MeetingExecutor> partOfUpdate = executorList.stream().limit(DATA_SIZE).collect(Collectors.toList());
            meetingExecutorDao.batchInsert(partOfUpdate);
            executorList.subList(0, executorList.size() >= DATA_SIZE ? DATA_SIZE : executorList.size()).clear();
        }
    }

    @Override
    public void cancel(Meeting meeting) {
        Meeting canceled = checkMeetingCurrentStatus(meeting.getId(), MeetingStatusEnum.READY);
        if (!getCurrentUserInfo().getId().equals(canceled.getPublisherId())) {
            logger.error("当前用户不是任务发布人，无法取消该会议 : {}", meeting);
            throw new BusinessException("当前用户不是任务发布人，无法取消该会议");
        }
        long startTimeInterval = Duration.between(Instant.now(), canceled.getStartTime().toInstant()).toMinutes();
        // 距离开会低于20分钟不能取消
        if (startTimeInterval < 20) {
            logger.error("会议开始前20分钟不能取消会议, {}", meeting);
            throw new BusinessException("会议开始前20分钟不能取消会议");
        }
        updateMeetingStatus(canceled, MeetingStatusEnum.CANCEL);
        // 取消会议同时删除需要签到的信息
        meetingExecutorDao.deleteByMeetingId(canceled.getId());

        Log log = Log.getLogByOperateAndType(canceled.getId(), getCurrentUserInfo(),
                LogOperateEnum.MEETING_CANCEL, LogTypeEnum.MEETING);
        logDao.add(log);
    }

    @Override
    public void start(Long id) {
        Meeting toCheck = meetingDao.findById(id);
        if (toCheck == null) {
            logger.error("会议单不存在, id -> {}", id);
            // 直接忽略
            return;
        }
        if (!MeetingStatusEnum.READY.getKey().equals(toCheck.getStatus())) {
            logger.error("会议单状态不正确, {}", toCheck);
            // 直接忽略
            return;
        }
        logger.info("开始会议 : {}", toCheck);
        updateMeetingStatus(toCheck, MeetingStatusEnum.EXECUTE);
        // 发送结束会议的redis队列
        redisDelayHandler.setDelayZSet(RedisConstant.MEETING_END_ZSET, toCheck.getId(), toCheck.getEndTime());
    }

    @Override
    public void end(Long id) {
        Meeting toCheck = meetingDao.findById(id);
        if (toCheck == null) {
            logger.error("会议单不存在, {}", id);
            // 直接忽略
            return;
        }
        if (!MeetingStatusEnum.EXECUTE.getKey().equals(toCheck.getStatus())) {
            logger.error("会议单状态不正确, {}", toCheck);
            // 直接忽略
            return;
        }
        logger.info("结束会议 : {}", toCheck);
        updateMeetingStatus(toCheck, MeetingStatusEnum.END);
    }

    @Override
    public void checkIn(MeetingExecutor meetingExecutor) {
        MeetingPageVO meeting = meetingDao.findById(meetingExecutor.getMeetingId());
        MeetingExecutorPageVO record = meetingExecutorDao
                .findCheckInExecutorByMeetingId(meetingExecutor.getMeetingId(), getCurrentUserInfo().getId());
        if (record == null) {
            logger.error("您不是会议相关人员，无法签到, {}", meetingExecutor);
            throw new BusinessException("您不是会议相关人员，无法签到");
        }
        if (record.getCheckInAt() != null) {
            logger.error("该用户已签到, {}", meetingExecutor);
            throw new BusinessException("您已签到");
        }
        Date checkInTime = new Date();
        boolean isLate = checkInTime.after(meeting.getStartTime());
        if (!isLate && Duration.between(checkInTime.toInstant(), meeting.getStartTime().toInstant()).toMinutes() > 10) {
            logger.error("还未到签到时间, {}", meetingExecutor);
            throw new BusinessException("还未到签到时间");
        }
        if (meeting.getEndTime().before(checkInTime)) {
            logger.error("会议已结束, {}", meetingExecutor);
            throw new BusinessException("会议已结束");
        }
        meetingExecutor.setCreateAt(checkInTime);
        meetingExecutor.setCheckInAt(checkInTime);
        meetingExecutor.setLate(isLate);
        meetingExecutorDao.upsertMeetingExecutor(meetingExecutor);
    }

    @Override
    public List<MeetingSummary> findSummaryByMeetingId(Long meetingId) {
        return meetingSummaryDao.selectByMeetingId(meetingId);
    }

    @Override
    public void addSummary(MeetingSummary meetingSummary) {
        MeetingPageVO meeting = checkUserSummaryPermission(meetingSummary);
        meetingSummary.setReporterId(meeting.getReporterId());
        meetingSummary.setReporterName(meeting.getReporterName());
        meetingSummary.setMeetingTitle(meeting.getTitle());
        meetingSummary.setCreateAt(new Date());
        meetingSummaryDao.insert(meetingSummary);
    }

    @Override
    public void updateSummary(MeetingSummary meetingSummary) {
        MeetingSummary toQuery = meetingSummaryDao.selectByPrimaryKey(meetingSummary.getId());
        meetingSummary.setMeetingId(toQuery.getMeetingId());
        checkUserSummaryPermission(meetingSummary);
        meetingSummaryDao.updateByPrimaryKey(meetingSummary);
    }

    /**
     * 检查用户上传会议纪要的权限
     * @param meetingSummary
     * @return
     */
    private MeetingPageVO checkUserSummaryPermission(MeetingSummary meetingSummary) {
        MeetingPageVO meeting = meetingDao.findById(meetingSummary.getMeetingId());
        if (meeting == null) {
            logger.error("会议不存在, {}", meetingSummary);
            throw new BusinessException("会议不存在");
        }

        if (!getCurrentUserInfo().getId().equals(meeting.getReporterId())) {
            logger.error("非会议纪要员不能上传会议纪要记录, {}", meetingSummary);
            throw new BusinessException("非会议纪要员不能上传会议纪要记录");
        }
        return meeting;
    }

    @Override
    public Set<Schedule> findScheduleMeeting(Schedule schedule) {
        schedule.setUserId(getCurrentUserInfo().getId());
        List<Schedule> schedules = meetingDao.findScheduleByCondition(schedule);
        schedules.addAll(meetingExecutorDao.findScheduleByCondition(schedule));
        return schedules.stream().sorted(Comparator.comparing(Schedule::getStartTime)).collect(
                Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public void updateCheckInOffline(Meeting meeting) {
        MeetingPageVO toCheck = meetingDao.findById(meeting.getId());
        meeting.setVersion(toCheck.getVersion());
        meetingDao.updateByCheckInPicUrlsAndCheckInOfflineNum(meeting);
    }

    @Override
    public List<MeetingExportVO> exportByCondition(Meeting meeting) {
        MeetingExportQO qo = new MeetingExportQO(meeting);
        // 如果不是会议管理员只能看到自己的数据
        qo.setUserId(getCurrentUserInfo().getId());
        return meetingDao.findExportInfoByCondition(qo);
    }

    @Override
    public List<MeetingExportVO> exportAuditByCondition(Meeting meeting) {
        MeetingExportQO qo = new MeetingExportQO(meeting);
        // 如果不是会议管理员只能看到自己的数据
        qo.setApproverId(getCurrentUserInfo().getId());
        // 选择审批会议状态
        qo.setStatusList(selectMeetingStatus(qo.getStatus()));
        return meetingDao.findExportAuditByCondition(qo);
    }

    @Override
    public MeetingExecutorPageVO findCheckInExecutorByMeetingId(Long meetingId) {
        MeetingExecutorPageVO record = meetingExecutorDao
                .findCheckInExecutorByMeetingId(meetingId, getCurrentUserInfo().getId());
        if (record == null) {
            logger.error("您不是会议相关人员，无法签到, {}", meetingId);
            throw new BusinessException("您不是会议相关人员，无法签到");
        }
        return record;
    }

    @Override
    public void invalid(Long id) {
        Meeting toInvalid = meetingDao.findById(id);
        if (toInvalid == null) {
            logger.error("会议单不存在, {}", id);
            return;
        }
        if (!MeetingStatusEnum.TO_AUDIT.getKey().equals(toInvalid.getStatus())) {
            logger.error("会议单状态不正确, {}", toInvalid);
            return;
        }
        updateMeetingStatus(toInvalid, MeetingStatusEnum.INVALID);
    }

    private void updateMeetingStatus(Meeting meeting, MeetingStatusEnum toUpdateStatusEnum) {
        Meeting toUpdate = new Meeting();
        toUpdate.setId(meeting.getId());
        toUpdate.setUpdateAt(new Date());
        toUpdate.setStatus(toUpdateStatusEnum.getKey());
        toUpdate.setVersion(meeting.getVersion());
        meetingDao.updateByPrimaryKey(toUpdate);
    }

    /**
     * 生成会议报告单
     * 规则 : 拼音首字母-年月日-当日派单号
     */
    private String generateApplyNo() {
        String lastApplyNo = meetingDao.findNoByPrefix();
        return ApplyNoGenerator.generateApplyNo(lastApplyNo, "HY");
    }

    /**
     * 检查会议是否在对应的状态
     * @param id
     * @param correctStatusEnum
     * @return
     */
    private Meeting checkMeetingCurrentStatus(Long id, MeetingStatusEnum correctStatusEnum) {
        Meeting toCheck = meetingDao.findById(id);
        if (toCheck == null) {
            logger.error("会议单不存在, {}", id);
            throw new BusinessException("会议单不存在");
        }
        if (!correctStatusEnum.getKey().equals(toCheck.getStatus())) {
            logger.error("会议 : {} " + MeetingStatusEnum.getDescriptionByKey(toCheck.getStatus()), id);
            throw new BusinessException("会议" + MeetingStatusEnum.getDescriptionByKey(toCheck.getStatus()));
        }
        return toCheck;
    }

    private void existMeetingRoom(Meeting meeting) {
        MeetingRoom toCheckRoom = meetingRoomDao.findById(meeting.getRoomId());
        if (toCheckRoom == null || MeetingRoomStatusEnum.DISABLE.getKey().equals(toCheckRoom.getStatus())) {
            logger.error("会议室不存在或未启用, {}", meeting);
            throw new BusinessException("会议室不存在或未启用");
        }
    }

    /**
     * 审核会议检查权限
     * @param userInfo
     * @param meeting
     */
    private void checkAuditMeetingAuth(UserInfo userInfo, Meeting meeting) {
        if (!userInfo.getId().equals(meeting.getApproverId())) {
            logger.error("当前用户并非该会议审核人, {}", meeting);
            throw new BusinessException("当前用户并非该会议审核人");
        }
    }

    /**
     * 会议审批选择需要显示的会议状态
     * @param status
     */
    private List<Integer> selectMeetingStatus(Integer status) {
        // 如果没选中状态或状态不属于会议审批页面, 就返回全部审批会议
        switch (MeetingStatusEnum.getByKey(status)) {
            case TO_AUDIT:
                return null;
            case REJECT:
                return null;
            case READY:
                return Arrays.asList(MeetingStatusEnum.TO_AUDIT.getKey(), MeetingStatusEnum.READY.getKey(),
                                MeetingStatusEnum.EXECUTE.getKey(), MeetingStatusEnum.CANCEL.getKey(),
                                MeetingStatusEnum.END.getKey());
            default:
                return Arrays.asList(MeetingStatusEnum.TO_AUDIT.getKey(),
                        MeetingStatusEnum.READY.getKey(), MeetingStatusEnum.REJECT.getKey(),
                        MeetingStatusEnum.EXECUTE.getKey(), MeetingStatusEnum.CANCEL.getKey(),
                        MeetingStatusEnum.END.getKey());
        }
    }
}