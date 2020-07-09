package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Report;
import com.bit.module.oa.vo.report.ReportVO;

import java.util.List;

/**
 * Report的Service
 * @author codeGenerator
 */
public interface ReportService {
	/**
	 * 根据条件查询Report
	 * @param reportVO
	 * @return
	 */
	BaseVo findByConditionPage(ReportVO reportVO);
	/**
	 * 通过主键查询单个Report
	 * @param id
	 * @return
	 */
	Report findById(Long id);

	/**
	 * 批量保存Report
	 * @param reports
	 */
	void batchAdd(List<Report> reports);
	/**
	 * 保存Report
	 * @param report
	 */
	void add(Report report);
	/**
	 * 更新Report
	 * @param report
	 */
	void confirm(Report report);
	/**
	 * 删除Report
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除Report
	 * @param ids
	 */
	void batchDelete(List<Long> ids);
}
