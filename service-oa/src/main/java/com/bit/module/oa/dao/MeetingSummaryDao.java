package com.bit.module.oa.dao;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.bit.module.oa.bean.MeetingSummary;


public interface MeetingSummaryDao {
    int deleteByPrimaryKey(Long id);

    int insert(MeetingSummary record);

    MeetingSummary selectByPrimaryKey(Long id);

    List<MeetingSummary> selectByMeetingId(@Param("meetingId")Long meetingId);

    int updateByPrimaryKey(MeetingSummary record);
}