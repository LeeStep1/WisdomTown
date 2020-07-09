package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Schedule;
import com.bit.module.oa.bean.Meeting;
import com.bit.module.oa.bean.MeetingExecutor;
import com.bit.module.oa.bean.MeetingSummary;
import com.bit.module.oa.vo.meeting.MeetingExecutorPageVO;
import com.bit.module.oa.vo.meeting.MeetingExecutorVO;
import com.bit.module.oa.vo.meeting.MeetingExportVO;
import com.bit.module.oa.vo.meeting.MeetingVO;

import java.util.List;
import java.util.Set;

/**
 * Meeting的Service
 * @author codeGenerator
 */
public interface MeetingService {
	/**
	 * 根据条件查询Meeting
	 * @param meetingVO
	 * @return
	 */
	BaseVo findByConditionPage(MeetingVO meetingVO);

	/**
	 * 根据条件查询审核页面
	 * @param meetingVO
	 * @return
	 */
	BaseVo findAuditResultByConditionPage(MeetingVO meetingVO);

	/**
	 * 根据条件搜索会议签到列表
	 * @param meetingExecutor
	 * @return
	 */
	BaseVo findByExecutorPage(MeetingExecutorVO meetingExecutor);

	/**
	 * 通过主键查询单个Meeting
	 * @param id
	 * @return
	 */
	Meeting findById(Long id);
	/**
	 * 保存Meeting
	 * @param meeting
	 */
	void add(Meeting meeting);
	/**
	 * 更新Meeting
	 * @param meeting
	 */
	void update(Meeting meeting);
	/**
	 * 删除Meeting
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 发布会议单
	 * @param meeting
	 */
    void publish(Meeting meeting);

	/**
	 * 取消会议
	 * @param meeting
	 */
    void reject(MeetingVO meeting);

	/**
	 * 审核会议
	 * @param meeting
	 */
	void audit(Meeting meeting);

	/**
	 * 取消会议
	 * @param meeting
	 */
	void cancel(Meeting meeting);

	/**
	 * 开始会议
     * @param id
     */
	void start(Long id);

	/**
	 * 结束会议
	 * @param id
	 */
	void end(Long id);

	/**
	 * 签到
	 * @param meetingExecutor
	 */
	void checkIn(MeetingExecutor meetingExecutor);

	List<MeetingSummary> findSummaryByMeetingId(Long meetingId);

	void addSummary(MeetingSummary meetingSummary);

	Set<Schedule> findScheduleMeeting(Schedule schedule);

	void updateCheckInOffline(Meeting meeting);

	List<MeetingExportVO> exportByCondition(Meeting meeting);

	List<MeetingExportVO> exportAuditByCondition(Meeting meeting);

    MeetingExecutorPageVO findCheckInExecutorByMeetingId(Long meetingId);

	/**
	 * 定时驳回
	 * @param id
	 */
	void invalid(Long id);

	/**
	 * 更新会议纪要
	 * @param meetingSummary
	 */
    void updateSummary(MeetingSummary meetingSummary);
}
