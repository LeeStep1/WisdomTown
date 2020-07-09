package com.bit.module.oa.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.MeetingRoom;
import com.bit.module.oa.dao.MeetingDao;
import com.bit.module.oa.dao.MeetingRoomDao;
import com.bit.module.oa.enums.MeetingRoomStatusEnum;
import com.bit.module.oa.service.MeetingRoomService;
import com.bit.module.oa.vo.meeting.MeetingPageVO;
import com.bit.module.oa.vo.meeting.RoomUsing;
import com.bit.module.oa.vo.meetingRoom.MeetingRoomVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * MeetingRoom的Service实现类
 *
 * @author codeGenerator
 */
@Service("meetingRoomService")
@Transactional
public class MeetingRoomServiceImpl extends BaseService implements MeetingRoomService {

    private static final Logger logger = LoggerFactory.getLogger(MeetingRoomServiceImpl.class);

    @Autowired
    private MeetingRoomDao meetingRoomDao;

    @Autowired
    private MeetingDao meetingDao;

    /**
     * 根据条件查询MeetingRoom
     *
     * @param meetingRoomVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(MeetingRoomVO meetingRoomVO) {
        PageHelper.startPage(meetingRoomVO.getPageNum(), meetingRoomVO.getPageSize());
        meetingRoomVO.setOrderBy("id");
        meetingRoomVO.setOrder("desc");
        List<MeetingRoom> list = meetingRoomDao.findByConditionPage(meetingRoomVO);
        PageInfo<MeetingRoom> pageInfo = new PageInfo<>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查询所有签到点的基本信息
     *
     * @return
     */
    @Override
    public List<MeetingRoom> findAll() {
        return meetingRoomDao.findAll("id");
    }

    /**
     * 通过主键查询单个MeetingRoom
     *
     * @param id
     * @return
     */
    @Override
    public MeetingRoom findById(Long id) {
        return meetingRoomDao.findById(id);
    }

    /**
     * 保存MeetingRoom
     *
     * @param meetingRoom
     */
    @Override
    public void add(MeetingRoom meetingRoom) {
        meetingRoom.setStatus(MeetingRoomStatusEnum.DISABLE.getKey());
        meetingRoomDao.insert(meetingRoom);
    }

    /**
     * 更新MeetingRoom
     *
     * @param meetingRoom
     */
    @Override
    public void update(MeetingRoom meetingRoom) {
        meetingRoomDao.updateByPrimaryKey(meetingRoom);
    }

    @Override
    public void convertStatus(Long id, Integer status) {
        if (MeetingRoomStatusEnum.DISABLE.getKey().equals(status) && meetingDao.countUsingByRoomId(id, new Date()) > 0) {
            logger.info("当前会议室{}处于占用状态，不能停用", id);
            throw new BusinessException("当前会议室处于占用状态");
        }
        meetingRoomDao.updateStatusById(status, id);
    }

    @Override
    public List<RoomUsing> findUsingByCondition(MeetingPageVO meeting) {
        return meetingDao.findUsingByCondition(meeting);
    }

    @Override
    public void delete(Long id) {
        meetingRoomDao.delete(id);
    }
}
