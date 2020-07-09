package com.bit.module.oa.dao;

import com.bit.module.oa.bean.CheckIn;
import com.bit.module.oa.vo.checkIn.CheckInVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CheckIn管理的Dao
 * @author 
 *
 */
@Repository
public interface CheckInDao {
	/**
	 * 根据条件查询CheckIn
	 * @param checkInVO
	 * @return
	 */
	List<CheckIn> findByCondition(CheckInVO checkInVO);
	/**
	 * 通过主键查询单个CheckIn
	 * @param id
	 * @return
	 */
	CheckIn findById(@Param(value = "id") Long id);
	/**
	 * 批量保存CheckIn
	 * @param checkIns
	 */
	void batchAdd(@Param(value = "checkIns") List<CheckIn> checkIns);
	/**
	 * 保存CheckIn
	 * @param checkIn
	 */
	void add(CheckIn checkIn);
	/**
	 * 批量更新CheckIn
	 * @param checkIns
	 */
	void batchUpdate(List<CheckIn> checkIns);
	/**
	 * 更新CheckIn
	 * @param checkIn
	 */
	void update(CheckIn checkIn);
	/**
	 * 删除CheckIn
	 * @param ids
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除CheckIn
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 签到
	 * @param toCheck
	 */
    void updateCheckInStatus(CheckIn toCheck);

	/**
	 *
	 * @param executeId
	 * @return
	 */
	List<CheckIn> findByExecuteId(@Param(value = "executeId") Long executeId);

	List<CheckIn> findByInspectId(@Param("inspectId")Long inspectId);
}
