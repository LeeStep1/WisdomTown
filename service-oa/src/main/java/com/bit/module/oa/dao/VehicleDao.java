package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Vehicle;
import com.bit.module.oa.vo.vehicle.SimpleVehicleVO;
import com.bit.module.oa.vo.vehicle.VehicleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Vehicle管理的Dao
 * @author 
 *
 */
@Repository
public interface VehicleDao {
	/**
	 * 根据条件查询Vehicle
	 * @param vehicleVO
	 * @return
	 */
	List<Vehicle> findByConditionPage(VehicleVO vehicleVO);
	/**
	 * 查询所有Vehicle
	 * @return
	 */
	List<SimpleVehicleVO> findAll(@Param("plateNo") String plateNo,
								  @Param(value="sorter")String sorter);
	/**
	 * 通过主键查询单个Vehicle
	 * @param id
	 * @return
	 */
	Vehicle findById(@Param(value="id")Long id);

	/**
	 * 批量查询
	 * @param vehicleIds
	 */
	List<SimpleVehicleVO> findByIds(List<Long> vehicleIds);
	/**
	 * 查询单个Vehicle
	 * @param plateNo
	 * @return
	 */
	Integer countByPlateNo(String plateNo);
	/**
	 * 批量保存Vehicle
	 * @param vehicles
	 */
	void batchAdd(List<Vehicle> vehicles);
	/**
	 * 保存Vehicle
	 * @param vehicle
	 */
	void add(Vehicle vehicle);
	/**
	 * 批量更新Vehicle
	 * @param vehicles
	 */
	void batchUpdate(List<Vehicle> vehicles);
	/**
	 * 更新Vehicle
	 * @param vehicle
	 */
	void update(Vehicle vehicle);
	/**
	 * 删除Vehicle
	 * @param vehicles
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除Vehicle
	 * @param id
	 */
	void delete(@Param(value="id")Long id);

    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

	Vehicle findByPlateNo(String plateNo);

	/**
	 * 释放车辆占用状态
	 * @param id
	 */
    void releaseVehicleByApplicationId(Long id);

    void occupyVehicleById(List<Long> ids);

    List<Vehicle> findByCondition(@Param("plateNo") String plateNo, @Param("vehicleType") String vehicleType, @Param("power") String power,
                                  @Param("plateType") String plateType);
}
