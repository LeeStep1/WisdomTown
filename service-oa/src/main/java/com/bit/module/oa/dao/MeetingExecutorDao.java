package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Schedule;
import com.bit.module.oa.bean.MeetingExecutor;

import java.util.List;

import com.bit.module.oa.vo.meeting.MeetingExecutorPageVO;
import com.bit.module.oa.vo.meeting.MeetingExecutorVO;
import org.apache.ibatis.annotations.Param;

public interface MeetingExecutorDao {
    List<MeetingExecutorPageVO> findByConditionPage(MeetingExecutorVO meetingExecutor);

    int deleteByPrimaryKey(Long id);

    int insert(MeetingExecutor record);

    int upsertMeetingExecutor(MeetingExecutor record);

    MeetingExecutor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MeetingExecutor record);

    int updateByPrimaryKey(MeetingExecutor record);

    int batchInsert(@Param("list") java.util.List<MeetingExecutor> list);

    int deleteByMeetingId(@Param("meetingId")Long meetingId);

    List<Schedule> findScheduleByCondition(Schedule schedule);

    MeetingExecutorPageVO findCheckInExecutorByMeetingId(@Param("meetingId")Long meetingId,
                                                         @Param("userId")Long userId);
}