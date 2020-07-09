package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Driver;
import com.bit.module.oa.vo.driver.DriverExportVO;
import com.bit.module.oa.vo.driver.DriverVO;
import com.bit.module.oa.vo.driver.SimpleDriverVO;

import java.util.List;
/**
 * Driver的Service
 * @author codeGenerator
 */
public interface DriverService {
	/**
	 * 根据条件查询Driver
	 * @param driverVO
	 * @return
	 */
	BaseVo findByConditionPage(DriverVO driverVO);
	/**
	 * 查询所有Driver
	 * @param name 驾驶员姓名
	 * @return
	 */
	List<SimpleDriverVO> findAll(String name);
	/**
	 * 通过主键查询单个Driver
	 * @param id
	 * @return
	 */
	Driver findById(Long id);

	/**
	 * 批量保存Driver
	 * @param drivers
	 */
	void batchAdd(List<Driver> drivers);
	/**
	 * 保存Driver
	 * @param driver
	 */
	void add(Driver driver);
	/**
	 * 批量更新Driver
	 * @param drivers
	 */
	void batchUpdate(List<Driver> drivers);
	/**
	 * 更新Driver
	 * @param driver
	 */
	void update(Driver driver);
	/**
	 * 删除Driver
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除Driver
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 修改驾驶员状态
	 * @param id
	 * @param status
	 */
	void convertStatus(Long id, Integer status);

	/**
	 * 查询手机号是否重复(true : 存在; false : 不存在 )
	 * @param mobile
	 * @return
	 */
    Boolean checkDriverMobile(String mobile);

	/**
	 * 驾驶员导出
	 * @return
     * @param name
	 */
    List<DriverExportVO> findExportDriver(String name);
}
