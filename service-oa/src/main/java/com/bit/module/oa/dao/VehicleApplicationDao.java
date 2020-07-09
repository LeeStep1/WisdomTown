package com.bit.module.oa.dao;

import java.util.List;

import com.bit.module.oa.vo.vehicleApplication.*;
import org.apache.ibatis.annotations.Param;
import com.bit.module.oa.bean.VehicleApplication;
import org.springframework.stereotype.Repository;

/**
 * VehicleApplication管理的Dao
 * @author 
 *
 */
@Repository
public interface VehicleApplicationDao {
	/**
	 * 根据条件查询VehicleApplication
	 * @param vehicleApplicationVO
	 * @return
	 */
	List<VehicleApplication> findByConditionPage(VehicleApplicationVO vehicleApplicationVO);

	/**
	 * 根据条件查询我的用车
	 * @param vehicleApplicationQO
	 * @return
	 */
	List<MyVehicleApplication> findMyVehicleApplication(VehicleApplicationQO vehicleApplicationQO);

	List<VehicleApplicationDetail> findExportDataByCondition(VehicleApplicationExportQO vehicleApplicationQO);
	/**
	 * 查询所有VehicleApplication
	 * @return
	 */
	List<VehicleApplication> findAll(@Param(value="sorter")String sorter);
	/**
	 * 通过主键查询单个VehicleApplication
	 * @param id	 	 
	 * @return
	 */
	VehicleApplicationDetail findById(@Param(value="id")Long id);
	/**
	 * 批量保存VehicleApplication
	 * @param vehicleApplications
	 */
	void batchAdd(List<VehicleApplication> vehicleApplications);
	/**
	 * 保存VehicleApplication
	 * @param vehicleApplication
	 */
	void apply(VehicleApplication vehicleApplication);
	/**
	 * 更新VehicleApplication
	 * @param vehicleApplication
	 */
	void update(VehicleApplication vehicleApplication);
	/**
	 * 删除VehicleApplication
	 * @param vehicleApplications
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除VehicleApplication
	 * @param id
	 */
	void delete(@Param(value="id")Long id);

	/**
	 * 查询最新的用车单号，用于生成用车单
	 * @param prefix
	 * @return
	 */
    String findByApplyNo(String prefix);

	List<ManagerVehicleApplicationVO> findLedgerVehicleApplication(VehicleApplicationQO vehicleApplicationQO);

	List<ManagerVehicleApplicationVO> findHandleVehicleApplication(VehicleApplicationQO vehicleApplicationQO);

	List<VehicleApplicationDetail> findLedgerExportDataByCondition(VehicleApplicationExportQO vehicleApplicationQO);
}
