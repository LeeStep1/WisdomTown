package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Report;
import com.bit.module.oa.vo.report.ReportVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/19 14:29
 */
@Repository
public interface ReportDao {
    /**
     * 根据条件查询Report
     * @param reportVO
     * @return
     */
    List<Report> findByConditionPage(ReportVO reportVO);
    /**
     * 通过主键查询单个Report
     * @param id
     * @return
     */
    Report findById(@Param(value="id") Long id);
    /**
     * 批量保存Report
     * @param reports
     */
    void batchAdd(@Param(value="reports") List<Report> reports);
    /**
     * 保存Report
     * @param report
     */
    void add(Report report);
    /**
     * 批量更新Report
     * @param reports
     */
    void batchUpdate(List<Report> reports);
    /**
     * 更新Report
     * @param report
     */
    void updateConfirmStatus(Report report);
    /**
     * 删除Report
     * @param ids
     */
    void batchDelete(List<Long> ids);
    /**
     * 删除Report
     * @param id
     */
    void delete(@Param(value="id")Long id);

    /**
     * 查找最后一个上报记录
     * @param inspectId
     * @return
     */
    String findLastReportByInspectId(@Param("inspectId") Long inspectId);
}
