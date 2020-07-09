package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Meeting;
import com.bit.module.oa.bean.MeetingRoom;
import com.bit.module.oa.enums.MeetingRoomStatusEnum;
import com.bit.module.oa.service.MeetingRoomService;
import com.bit.module.oa.vo.meeting.MeetingPageVO;
import com.bit.module.oa.vo.meeting.RoomUsing;
import com.bit.module.oa.vo.meetingRoom.MeetingRoomVO;
import com.bit.utils.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MeetingRoom的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/meetingRoom")
public class MeetingRoomController {
	private static final Logger logger = LoggerFactory.getLogger(MeetingRoomController.class);
	@Autowired
	private MeetingRoomService meetingRoomService;
	

    /**
     * 分页查询MeetingRoom列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody MeetingRoomVO meetingRoomVO) {
    	//分页对象，前台传递的包含查询的参数

        return meetingRoomService.findByConditionPage(meetingRoomVO);
    }

    /**
     * 根据主键ID查询MeetingRoom
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        MeetingRoom meetingRoom = meetingRoomService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(meetingRoom);
		return baseVo;
    }
    
    /**
     * 新增MeetingRoom
     *
     * @param meetingRoom MeetingRoom实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody MeetingRoom meetingRoom) {
        CheckUtil.notNull(meetingRoom.getName());
        CheckUtil.notNull(meetingRoom.getFloor());
        CheckUtil.notNull(meetingRoom.getLocation());
        CheckUtil.notNull(meetingRoom.getCapacity());

        meetingRoomService.add(meetingRoom);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改MeetingRoom
     *
     * @param meetingRoom MeetingRoom实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody MeetingRoom meetingRoom) {
        CheckUtil.notNull(meetingRoom.getId());
        CheckUtil.notNull(meetingRoom.getName());
        CheckUtil.notNull(meetingRoom.getFloor());
        CheckUtil.notNull(meetingRoom.getLocation());
        CheckUtil.notNull(meetingRoom.getCapacity());
		meetingRoomService.update(meetingRoom);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 启用会议室
     * @param id
     * @return
     */
    @PostMapping("/{id}/enable")
    public BaseVo enableMeetingRoom(@PathVariable("id") Long id) {
        meetingRoomService.convertStatus(id, MeetingRoomStatusEnum.ENABLED.getKey());
        return new BaseVo();
    }

    /**
     * 禁用会议室
     * @param id
     * @return
     */
    @PostMapping("/{id}/disable")
    public BaseVo disableMeetingRoom(@PathVariable("id") Long id) {
        meetingRoomService.convertStatus(id, MeetingRoomStatusEnum.DISABLE.getKey());
        return new BaseVo();
    }

    /**
     * 会议室下拉列表
     * @return
     */
    @GetMapping("/list")
    public BaseVo findAll() {
        List<MeetingRoom> meetingRoom = meetingRoomService.findAll();
        return new BaseVo<>(meetingRoom);
    }

    /**
     * 会议室删除
     * @param id
     * @return
     */
    @GetMapping("/{id}/delete")
    public BaseVo delete(@PathVariable("id") Long id) {
        meetingRoomService.delete(id);
        return new BaseVo();

    }

    /**
     * 会议室使用列表
     * @param meeting
     * @return
     */
    @PostMapping("/using")
    public BaseVo using(@RequestBody MeetingPageVO meeting) {
        List<RoomUsing> usings = meetingRoomService.findUsingByCondition(meeting);
        return new BaseVo(usings);
    }
}
