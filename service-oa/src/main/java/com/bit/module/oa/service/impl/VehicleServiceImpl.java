package com.bit.module.oa.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Vehicle;
import com.bit.module.oa.dao.VehicleDao;
import com.bit.module.oa.enums.VehicleIdleStatusEnum;
import com.bit.module.oa.enums.VehicleStatusEnum;
import com.bit.module.oa.service.VehicleService;
import com.bit.module.oa.vo.vehicle.SimpleVehicleVO;
import com.bit.module.oa.vo.vehicle.VehicleExportVO;
import com.bit.module.oa.vo.vehicle.VehicleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Vehicle的Service实现类
 * @author codeGenerator
 *
 */
@Service("vehicleService")
@Transactional
public class VehicleServiceImpl extends BaseService implements VehicleService{

	private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

	@Autowired
	private VehicleDao vehicleDao;

	/**
	 * 根据条件查询Vehicle
	 * @param vehicleVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(VehicleVO vehicleVO){
		PageHelper.startPage(vehicleVO.getPageNum(), vehicleVO.getPageSize());
		List<Vehicle> list = vehicleDao.findByConditionPage(vehicleVO);
		PageInfo<Vehicle> pageInfo = new PageInfo<Vehicle>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	@Override
	public List<VehicleExportVO> findExportVehicle(String plateNo, String vehicleType, String power, String plateType) {
		List<Vehicle> list = vehicleDao.findByCondition(plateNo, vehicleType, power, plateType);
		return list.stream().map(source -> {
			VehicleExportVO target = new VehicleExportVO();
			BeanUtils.copyProperties(source, target);
			return target;
		}).collect(Collectors.toList());
	}

	/**
	 * 查询所有Vehicle
	 * @param plateNo 车牌号
	 * @return
	 */
	@Override
	public List<SimpleVehicleVO> findAll(String plateNo){
		return vehicleDao.findAll(plateNo, "id");
	}
	/**
	 * 通过主键查询单个Vehicle
	 * @param id
	 * @return
	 */
	@Override
	public Vehicle findById(Long id){
		return vehicleDao.findById(id);
	}
	/**
	 * 查询单个Vehicle
	 * @param plateNo
	 * @return
	 */
	@Override
	public Vehicle findByPlateNo(String plateNo){
		return vehicleDao.findByPlateNo(plateNo);
	}

	/**
	 * 批量保存Vehicle
	 * @param vehicles
	 */
	@Override
	public void batchAdd(List<Vehicle> vehicles){
		vehicleDao.batchAdd(vehicles);
	}
	/**
	 * 保存Vehicle
	 * @param vehicle
	 */
	@Override
	public void add(Vehicle vehicle) {
		Integer byPlateNo = vehicleDao.countByPlateNo(vehicle.getPlateNo());
		if (byPlateNo > 0) {
			logger.error("车辆已存在 : {}", vehicle);
			throw new BusinessException("该车辆已存在");
		}
	    vehicle.setStatus(VehicleStatusEnum.DISABLE.getKey());
	    vehicle.setIdle(VehicleIdleStatusEnum.IDLE.getKey());
		vehicleDao.add(vehicle);
	}
	/**
	 * 批量更新Vehicle
	 * @param vehicles
	 */
	@Override
	public void batchUpdate(List<Vehicle> vehicles){
		vehicleDao.batchUpdate(vehicles);
	}
	/**
	 * 更新Vehicle
	 * @param vehicle
	 */
	@Override
	public void update(Vehicle vehicle){
		Vehicle toUpdate = vehicleDao.findById(vehicle.getId());
		if (toUpdate == null) {
			logger.error("车辆不存在, {}", vehicle);
			throw new BusinessException("车辆不存在");
		}
		vehicleDao.update(vehicle);
	}
	/**
	 * 删除Vehicle
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		vehicleDao.batchDelete(ids);
	}

    @Override
    public void convertStatus(Long id, Integer status) {
		Vehicle toConvertStatus = vehicleDao.findById(id);
		if (toConvertStatus == null) {
			logger.error("车辆{}不存在", id);
			throw new BusinessException("车辆不存在");
		}
		if (toConvertStatus.getStatus().equals(status)) {
			logger.error("车辆的状态已经是{}", status);
			throw new BusinessException("车辆状态不匹配");
		}
        vehicleDao.updateStatus(id, status);
    }

	@Override
	public Boolean checkPlateNo(String plateNo) {
		return vehicleDao.countByPlateNo(plateNo) > 0;
	}

	/**
	 * 删除Vehicle
	 * @param id
	 */
	@Override
	public void delete(Long id){
		vehicleDao.delete(id);
	}
}
