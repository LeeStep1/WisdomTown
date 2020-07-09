package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.RepairApply;
import com.bit.module.cbo.vo.RepairApplyPageVO;
import com.bit.module.cbo.vo.RepairApplyVO;

public interface RepairApplyService {
	/**
	 * app端故障报修
	 * @param repairApply
	 * @return
	 */
	BaseVo appInsert(RepairApply repairApply);
	/**
	 * 取消报修
	 * @return
	 */
	BaseVo cancel(RepairApplyVO repairApplyVO);
	/**
	 * 返显或单查记录
	 * @param id
	 * @return
	 */
	BaseVo reflectById(Long id);
	/**
	 * app三端查询报修
	 * @param repairApplyPageVO
	 * @return
	 */
	BaseVo appRecordListPage(RepairApplyPageVO repairApplyPageVO);

	/**
	 * 报修记录处理
	 * @param repairApplyVO
	 * @return
	 */
	BaseVo applyHandle(RepairApplyVO repairApplyVO);

	/**
	 * web 端故障申报分页查询
	 * @param repairApplyPageVO
	 * @return
	 */
	BaseVo webListPage(RepairApplyPageVO repairApplyPageVO);

	/**
	 * 根据token 查询小区信息
	 * @return
	 */
	BaseVo getCommunityByToken();
}
