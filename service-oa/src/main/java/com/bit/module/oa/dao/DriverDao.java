package com.bit.module.oa.dao;

import java.util.List;

import com.bit.module.oa.vo.driver.SimpleDriverVO;
import org.apache.ibatis.annotations.Param;
import com.bit.module.oa.bean.Driver;
import com.bit.module.oa.vo.driver.DriverVO;
import org.springframework.stereotype.Repository;

/**
 * Driver管理的Dao
 * @author 
 *
 */
@Repository
public interface DriverDao {
	/**
	 * 根据条件查询Driver
	 * @param driverVO
	 * @return
	 */
	List<Driver> findByConditionPage(DriverVO driverVO);
	/**
	 * 查询所有Driver
	 * @return
	 */
	List<SimpleDriverVO> findAll(@Param(value="name")String name,
								 @Param(value="sorter")String sorter);
	/**
	 * 通过主键查询单个Driver
	 * @param id	 	 
	 * @return
	 */
	Driver findById(@Param(value="id")Long id);
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
	 * @param drivers
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除Driver
	 * @param id
	 */
	void delete(@Param(value="id")Long id);

	/**
	 * 修改驾驶员状态
	 * @param id
	 * @param status
	 */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

	/**
	 * 查询是否有重复的手机号
	 * @param mobile
	 * @return
	 */
	Integer countByMobile(String mobile);


	List<SimpleDriverVO> findByIds(List<Long> driverIds);

	List<Driver> findByCondition(@Param("name") String name);

}
