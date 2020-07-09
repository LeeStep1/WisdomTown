package com.bit.module.pb.dao;

import com.bit.module.pb.bean.Study;
import com.bit.module.pb.bean.StudyRecord;
import com.bit.module.pb.vo.StudyRecordVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-17 13:43
 */
@Repository
public interface StudyRecordDao {

    /**
     * 学习计划添加
     * @param studyRecord
     */
    void add(StudyRecord studyRecord);
    /**
     * 学习计划更新
     * @param studyRecord
     */
    void update(StudyRecord studyRecord);
    /**
     * 学习计划删除
     * @param id)
     */
    void delete(@Param(value = "id") Long id);
    /**
     * 学习计划批量新增
     * @param list
     */
    void batchAdd(@Param(value = "list") List<StudyRecord> list);
    /**
     * 学习计划批量删除
     * @param list
     */
    void batchDelete(@Param(value = "ids") List<Long> ids);
    /**
     * 学习计划根据参数查询
     * @param studyRecord
     */
    List<StudyRecord> queryByParam(StudyRecord studyRecord);

    /**
     * 分页查询学习计划
     * @param studyRecordVO
     * @return
     */
    List<StudyRecord> listPage(StudyRecordVO studyRecordVO);


}
