package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.CheckIn;
import com.bit.module.oa.vo.checkIn.CheckInVO;
import com.sun.tools.javac.comp.Check;

import java.util.List;

/**
 * CheckIn的Service
 * @author codeGenerator
 */
public interface CheckInService {
	/**
	 * 根据条件查询CheckIn
	 * @param checkInVO
	 * @return
	 */
	BaseVo findByConditionPage(CheckInVO checkInVO);
	/**
	 * 通过主键查询单个CheckIn
	 * @param id
	 * @return
	 */
	CheckIn findById(Long id);

	/**
	 * 批量保存CheckIn
	 * @param checkIns
	 */
	void batchAdd(List<CheckIn> checkIns);
	/**
	 * 保存CheckIn
	 * @param checkIn
	 */
	void add(CheckIn checkIn);
	/**
	 * 更新CheckIn
	 * @param checkIn
	 */
	void update(CheckIn checkIn);
	/**
	 * 删除CheckIn
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除CheckIn
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 *
	 * @param checkIn
	 */
	void signIn(CheckIn checkIn);

	List<CheckIn> findByInspectId(Long InspectId);
}
