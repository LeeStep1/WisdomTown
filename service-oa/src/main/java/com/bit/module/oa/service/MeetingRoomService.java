package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.MeetingRoom;
import com.bit.module.oa.vo.meeting.MeetingPageVO;
import com.bit.module.oa.vo.meeting.RoomUsing;
import com.bit.module.oa.vo.meetingRoom.MeetingRoomVO;

import java.util.List;

/**
 * MeetingRoom的Service
 * @author codeGenerator
 */
public interface MeetingRoomService {
	/**
	 * 根据条件查询MeetingRoom
	 * @param meetingRoomVO
	 * @return
	 */
	BaseVo findByConditionPage(MeetingRoomVO meetingRoomVO);
	/**
	 * 查询所有MeetingRoom
	 *
	 * @return
	 */
	List<MeetingRoom> findAll();
	/**
	 * 通过主键查询单个MeetingRoom
	 * @param id
	 * @return
	 */
	MeetingRoom findById(Long id);
	/**
	 * 保存MeetingRoom
	 * @param meetingRoom
	 */
	void add(MeetingRoom meetingRoom);
	/**
	 * 更新MeetingRoom
	 * @param meetingRoom
	 */
	void update(MeetingRoom meetingRoom);
	/**
	 * 删除MeetingRoom
	 * @param id
	 */
	void delete(Long id);

	/**
	 *
	 * @param id
	 * @param status
	 */
    void convertStatus(Long id, Integer status);

	/**
	 * 查询会议室占用
	 * @return
	 * @param meeting
	 */
	List<RoomUsing> findUsingByCondition(MeetingPageVO meeting);

}
