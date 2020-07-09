package com.bit.module.pb.dao;

import com.bit.module.pb.bean.ConferenceRecord;
import com.bit.module.pb.vo.ConferenceRecordVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-18 13:43
 */
@Repository
public interface ConferenceRecordDao {

    /**
     * 添加会议记录
     * @param conferenceRecord
     */
    void add(ConferenceRecord conferenceRecord);

    /**
     * 更新会议记录
     * @param conferenceRecord
     */
    void update(ConferenceRecord conferenceRecord);

    /**
     * 按参数查询会议记录
     * @param conferenceRecord
     * @return
     */
    List<ConferenceRecord> queryByParam(ConferenceRecord conferenceRecord);

    /**
     * 分页查询
     * @param conferenceRecordVO
     * @return
     */
    List<ConferenceRecord> listPage(ConferenceRecordVO conferenceRecordVO);

    /**
     * 批量添加
     */
    void batchAdd(@Param(value = "conferenceRecords") List<ConferenceRecord> conferenceRecords);

    /**
     * 根据会议id删除记录
     * @param conferenceId
     */
    void delByConferenceId(@Param(value = "conferenceId") Long conferenceId);

}
