package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Vehicle;
import com.bit.module.oa.vo.vehicle.SimpleVehicleVO;
import com.bit.module.oa.vo.vehicle.VehicleExportVO;
import com.bit.module.oa.vo.vehicle.VehicleVO;

import java.util.List;
/**
 * Vehicle的Service
 * @author codeGenerator
 */
public interface VehicleService {
	/**
	 * 根据条件查询Vehicle
	 * @param vehicleVO
	 * @return
	 */
	BaseVo findByConditionPage(VehicleVO vehicleVO);
	/**
	 * 查询所有Vehicle
	 * @param plateNo 车牌号
	 * @return
	 */
	List<SimpleVehicleVO> findAll(String plateNo);
	/**
	 * 通过主键查询单个Vehicle
	 * @param id
	 * @return
	 */
	Vehicle findById(Long id);

	/**
	 * 查询单个Vehicle
	 * @param plateNo
	 * @return
	 */
	Vehicle findByPlateNo(String plateNo);
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
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除Vehicle
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 转换车辆状态
	 * @param id
	 * @param status
	 */
    void convertStatus(Long id, Integer status);

	/**
	 * 查询车牌号是否重复(true : 存在; false : 不存在 )
	 * @param plateNo
	 * @return
	 */
	Boolean checkPlateNo(String plateNo);

	/**
	 * 导出车辆
	 * @return
     * @param plateNo
     * @param vehicleType
     * @param power
     * @param plateType
     */
	List<VehicleExportVO> findExportVehicle(String plateNo, String vehicleType, String power, String plateType);

}
