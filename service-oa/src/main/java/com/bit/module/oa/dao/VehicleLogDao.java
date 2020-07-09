package com.bit.module.oa.dao;

import com.bit.module.oa.bean.VehicleLog;
import com.bit.module.oa.vo.vehicleLog.VehicleLogDetailVO;
import com.bit.module.oa.vo.vehicleLog.VehicleLogQO;
import com.bit.module.oa.vo.vehicleLog.VehicleLogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * VehicleLog管理的Dao
 * @author 
 *
 */
@Repository
public interface VehicleLogDao {
	/**
	 * 根据条件查询VehicleLog
	 * @param vehicleLogVO
	 * @return
	 */
	List<VehicleLog> findByConditionPage(VehicleLogVO vehicleLogVO);

	/**
	 *
	 * @param vehicleLogVO
	 * @return
	 */
	List<VehicleLogDetailVO> findDetailByConditionPage(VehicleLogVO vehicleLogVO);

	/**
	 * 列表
	 * @param vehicleLogQO
	 * @return
	 */
	List<VehicleLogDetailVO> findDetailByCondition(VehicleLogQO vehicleLogQO);
	/**
	 * 查询所有VehicleLog
	 * @return
	 */
	List<VehicleLog> findAll(@Param(value="sorter")String sorter);
	/**
	 * 通过主键查询单个VehicleLog
	 * @param id
	 * @return
	 */
	VehicleLog findById(@Param(value="id")Long id);
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
	 * @param vehicleLogs
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除VehicleLog
	 * @param id
	 */
	void delete(@Param(value="id")Long id);

    Integer countOccupyLog(@Param("vehicleIds") List<Long> vehicleIds,
						   @Param("startTime") Date startTime,
						   @Param("endTime") Date endTime);
}
