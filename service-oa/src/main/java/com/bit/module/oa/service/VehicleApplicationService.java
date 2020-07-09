package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.VehicleApplication;
import com.bit.module.oa.vo.vehicleApplication.*;

import java.util.List;

/**
 * VehicleApplication的Service
 * @author codeGenerator
 */
public interface VehicleApplicationService {
	/**
	 * 根据条件查询VehicleApplication
	 * @param vehicleApplicationVO
	 * @return
	 */
	BaseVo findByConditionPage(VehicleApplicationVO vehicleApplicationVO);
	/**
	 * 我的用车页面
	 * @param vehicleApplicationQO
	 * @return
	 */
	BaseVo findMyVehicleApplicationPage(VehicleApplicationQO vehicleApplicationQO);

	/**
	 * 我的用车导出
	 * @param vehicleApplicationQO
	 * @return
	 */
	List<MyVehicleApplicationExportVO> findExportMyVehicleUsing(VehicleApplicationExportQO vehicleApplicationQO);
	/**
	 * 用车台账
	 * @param vehicleApplicationQO
	 * @return
	 */
	BaseVo findLedgerApplicationListPage(VehicleApplicationQO vehicleApplicationQO);

	/**
	 * 派车管理
	 * @param vehicleApplicationQO
	 * @return
	 */
	BaseVo findHandleApplicationListPage(VehicleApplicationQO vehicleApplicationQO);

	/**
	 * 导出派车管理数据
	 * @param vehicleApplicationQO
	 * @return
	 */
	List<HandleVehicleApplicationExportVO> findHandleApplicationExport(VehicleApplicationExportQO vehicleApplicationQO);
	/**
	 * 查询所有VehicleApplication
	 * @param sorter 排序字符串
	 * @return
	 */
	List<VehicleApplication> findAll(String sorter);
	/**
	 * 通过主键查询单个VehicleApplication
	 * @param id
	 * @return
	 */
	VehicleApplication findById(Long id);

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
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除VehicleApplication
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 查询用车详情
	 * @param toQuery
	 * @return
	 */
    VehicleApplicationDetail findVehicleApplicationDetail(VehicleApplication toQuery);

	/**
	 * 拒绝派车
	 * @param vehicleApplication
	 */
	void reject(VehicleApplication vehicleApplication);

	/**
	 * 租赁派车
	 * @param vehicleRentInfo
	 */
	void rent(VehicleRentInfo vehicleRentInfo);

	/**
	 * 派车
	 * @param vehicleAllowInfo
	 */
	void allow(VehicleAllowInfo vehicleAllowInfo);

	/**
	 * 结束派车，并释放车辆占用状态
	 * @param vehicleApplication
	 */
	void end(VehicleApplication vehicleApplication);

	List<HandleVehicleApplicationExportVO> findLedgerApplicationExport(VehicleApplicationExportQO vehicleApplicationQO);

	/**
	 * 车辆申请过期失效
	 * @param id
	 */
    void invalid(Long id);
}
