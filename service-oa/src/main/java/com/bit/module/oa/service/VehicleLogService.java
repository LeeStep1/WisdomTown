package com.bit.module.oa.service;

import java.util.List;

import com.bit.module.oa.vo.vehicleLog.VehicleLogQO;
import com.bit.module.oa.bean.VehicleLog;
import com.bit.module.oa.vo.vehicleLog.VehicleLogVO;
import com.bit.base.vo.BaseVo;
/**
 * VehicleLog的Service
 * @author codeGenerator
 */
public interface VehicleLogService {
	/**
	 * 根据条件查询VehicleLog
	 * @param vehicleLogVO
	 * @return
	 */
	BaseVo findByConditionPage(VehicleLogVO vehicleLogVO);

	/**
	 * 根据条件查询VehicleLog列表
	 * @param vehicleLogQO
	 * @return
	 */
	BaseVo findByCondition(VehicleLogQO vehicleLogQO);
	/**
	 * 查询所有VehicleLog
	 * @param sorter 排序字符串
	 * @return
	 */
	List<VehicleLog> findAll(String sorter);
	/**
	 * 通过主键查询单个VehicleLog
	 * @param id
	 * @return
	 */
	VehicleLog findById(Long id);

	/**
	 * 批量保存VehicleLog
	 * @param vehicleLogs
	 */
	void batchAdd(List<VehicleLog> vehicleLogs);
	/**
	 * 保存VehicleLog
	 * @param vehicleLog
	 */
	void add(VehicleLog vehicleLog);
	/**
	 * 批量更新VehicleLog
	 * @param vehicleLogs
	 */
	void batchUpdate(List<VehicleLog> vehicleLogs);
	/**
	 * 更新VehicleLog
	 * @param vehicleLog
	 */
	void update(VehicleLog vehicleLog);
	/**
	 * 删除VehicleLog
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除VehicleLog
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

}
