package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Inspect;
import com.bit.module.oa.vo.inspect.*;

import java.util.Date;
import java.util.List;

/**
 * Inspect的Service
 * @author codeGenerator
 */
public interface InspectService {
	/**
	 * 根据条件查询Inspect
	 * @param inspectVO
	 * @return
	 */
	BaseVo findByConditionPage(InspectVO inspectVO);

    BaseVo findByConditionPageForApp(InspectVO inspectVO);
	/**
	 * 通过主键查询单个Inspect
	 * @param id
	 * @return
	 */
	InspectExecutorVO findById(Long id);
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
	void delete(Long id);

	/**
	 * 发布巡检单
	 * @param inspect
	 */
    void publish(Inspect inspect);

    List<InspectTaskExport> findExportTaskInspect(Inspect inspect);

    List<InspectSituationExport> findExportSituationInspect(InspectExportQO inspect);

	/**
	 * 结束巡检
	 * @param id
	 */
	void end(Long id);

    InspectCountVO count(Integer type, Date startAt, Date endAt);
}
