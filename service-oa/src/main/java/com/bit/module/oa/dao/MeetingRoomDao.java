package com.bit.module.oa.dao;

import com.bit.module.oa.bean.MeetingRoom;
import com.bit.module.oa.vo.meetingRoom.MeetingRoomVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/3/1 11:18
 */
public interface MeetingRoomDao {

    List<MeetingRoom> findByConditionPage(MeetingRoomVO meetingRoomVO);

    int delete(Long id);

    int insert(MeetingRoom record);

    MeetingRoom findById(Long id);

    /**
     * 查询所有MeetingRoom
     * @return
     */
    List<MeetingRoom> findAll(@Param(value="sorter")String sorter);

    int updateByPrimaryKeySelective(MeetingRoom record);

    int updateByPrimaryKey(MeetingRoom record);

    int updateStatusById(@Param("updatedStatus")Integer updatedStatus,@Param("id")Long id);

    int updateBatch(List<MeetingRoom> list);

    int batchInsert(@Param("list") List<MeetingRoom> list);

}