package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Meeting;
import com.bit.module.oa.bean.Schedule;
import com.bit.module.oa.vo.meeting.MeetingExportVO;
import com.bit.module.oa.vo.meeting.MeetingPageVO;
import com.bit.module.oa.vo.meeting.MeetingVO;
import com.bit.module.oa.vo.meeting.RoomUsing;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/3/1 16:38
 */
public interface MeetingDao {
    List<MeetingPageVO> findByConditionPage(MeetingVO meetingVO);

    List<MeetingExportVO> findExportInfoByCondition(Meeting meeting);

    List<MeetingExportVO> findExportAuditByCondition(Meeting meeting);

    List<MeetingPageVO> findAuditPage(MeetingVO meetingVO);

    int deleteByPrimaryKey(Long id);

    int insert(Meeting record);

    MeetingPageVO findById(Long id);

    int updateByPrimaryKey(Meeting record);

    int updateByCheckInPicUrlsAndCheckInOfflineNum(Meeting meeting);

    int batchInsert(@Param("list") List<Meeting> list);

    String findNoByPrefix();

    List<RoomUsing> findUsingByCondition(MeetingPageVO meeting);

    List<Schedule> findScheduleByCondition(Schedule meeting);

    Integer countOccupyMeeting(@Param("roomId")Long roomId, @Param("startTime")Date startTime, @Param("endTime")Date endTime);

    int countUsingByRoomId(@Param("id")Long id, @Param("now")Date now);
}