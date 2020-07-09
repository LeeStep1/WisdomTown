package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Inspect;
import com.bit.module.oa.vo.inspect.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/15 14:23
 */
public interface InspectDao {
    /**
     * 根据条件查询Inspect
     * @param inspectVO
     * @return
     */
    List<InspectExecutorVO> findByConditionPage(InspectVO inspectVO);
    /**
     * 通过主键查询单个Inspect
     * @param id
     * @return
     */
    InspectExecutorVO findById(@Param(value="id")Long id);
    /**
     * 保存Inspect
     * @param inspect
     */
    void add(Inspect inspect);
    /**
     * 更新Inspect
     * @param inspect
     */
    void update(Inspect inspect);
    /**
     * 删除Inspect
     * @param id
     */
    void delete(@Param(value="id")Long id);

    String findNoByPrefix(@Param("prefix") Integer prefix);

    void updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("version")Integer version);

    void publish(@Param("id") Long id, @Param("status") Integer status,
                 @Param("publisherId") Long publisherId, @Param("version")Integer version);

    void updateLastReportAt(Inspect toCheck);

    List<InspectTaskExport> findTaskInspect(Inspect inspect);

    List<InspectSituationExport> findSituationInspect(InspectExportQO inspect);

    List<InspectExecutorVO> findByConditionPageForApp(@Param("inspectVO") InspectVO inspectVO);
}
