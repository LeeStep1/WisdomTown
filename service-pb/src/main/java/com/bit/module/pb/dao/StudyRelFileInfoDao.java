package com.bit.module.pb.dao;


import com.bit.module.pb.bean.FileInfo;
import com.bit.module.pb.bean.StudyRelFileInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-16 10:26
 */
@Repository
public interface StudyRelFileInfoDao {

    /**
     * 会议，学习计划 附件的关联表新增
     * @param studyRelFileInfo
     */
    void add(StudyRelFileInfo studyRelFileInfo);
    /**
     * 会议，学习计划 附件的关联表更新
     * @param studyRelFileInfo
     */
    void update(StudyRelFileInfo studyRelFileInfo);
    /**
     * 会议，学习计划 附件的关联表 删除
     * @param id
     */
    void delete(@Param(value = "id") Long id);
    /**
     * 会议，学习计划 附件的关联表 批量添加
     * @param list
     */
    void batchAdd(@Param(value = "list") List<StudyRelFileInfo> list);
    /**
     * 会议，学习计划 附件的关联表 批量删除
     * @param ids
     */
    void batchDel(@Param(value = "ids") List<Long> ids);
    /**
     * 会议，学习计划 附件的关联表 根据主键id查询信息
     * @param id
     */
    StudyRelFileInfo findByid(@Param(value = "id") Long id);
    /**
     * 会议，学习计划 附件的关联表 根据参数查询记录
     * @param studyRelFileInfo
     */
    List<StudyRelFileInfo> findByParam(StudyRelFileInfo studyRelFileInfo);
    /**
     * 会议，学习计划 附件的关联表 根据计划id查询记录
     * @param id
     */
    List<FileInfo> findFileInfoByStudyId(@Param(value = "id") Long id);











}
