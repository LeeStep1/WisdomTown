package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.RepairApplyProgress;

import java.util.List;

public interface RepairApplyProgressDao {
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	int deleteById(Long id);

	/**
	 * 新增
	 * @param record
	 * @return
	 */
    int add(RepairApplyProgress record);

	/**
	 * 返显或单查
	 * @param id
	 * @return
	 */
    RepairApplyProgress getRepairApplyProgressById(Long id);

	/**
	 * 更新
	 * @param repairApplyProgress
	 * @return
	 */
    int update(RepairApplyProgress repairApplyProgress);

	/**
	 * 多参数查询
	 * @param repairApplyProgress
	 * @return
	 */
	List<RepairApplyProgress> findByParam(RepairApplyProgress repairApplyProgress);

}