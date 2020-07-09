package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.RepairApply;

import java.util.List;

import com.bit.module.cbo.vo.RepairApplyPageVO;
import com.bit.module.cbo.vo.RepairApplyVO;
import org.apache.ibatis.annotations.Param;

public interface RepairApplyDao {
    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 新增
     * @param repairApply
     * @return
     */
    int add(RepairApply repairApply);

    /**
     * 返显或单查
     * @param id
     * @return
     */
    RepairApply getRequireApplyById(Long id);

    /**
     * 更新
     * @param repairApply
     * @return
     */
    int update(RepairApply repairApply);

	/**
	 * app三端查询报修
	 * @param repairApplyPageVO
	 * @return
	 */
	List<RepairApply> appRecordListPage(RepairApplyPageVO repairApplyPageVO);
	/**
	 * web 端故障申报分页查询
	 * @param repairApplyPageVO
	 * @return
	 */
	List<RepairApplyVO> webListPage(RepairApplyPageVO repairApplyPageVO);

}