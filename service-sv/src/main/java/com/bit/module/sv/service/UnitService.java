package com.bit.module.sv.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.Unit;
import com.bit.module.sv.vo.UnitVO;

import java.util.List;

/**
 * Unit的Service
 * @author codeGenerator
 */
public interface UnitService {
	/**
	 * 根据条件查询Unit
	 * @param unitVO
	 * @return
	 */
	BaseVo findByConditionPage(UnitVO unitVO);
	/**
	 * 通过主键查询单个Unit
	 * @param id
	 * @return
	 */
	Unit findById(Long id);

	/**
	 * 批量保存Unit
	 * @param units
	 */
	void batchAdd(List<Unit> units);
	/**
	 * 保存Unit
	 * @param unit
	 */
	void add(Unit unit);
	/**
	 * 更新Unit
	 * @param unit
	 */
	void update(Unit unit);
	/**
	 * 删除Unit
	 * @param id
	 */
	void delete(Long id);

	void disableStatus(Long id);

	/**
	 * 根据类型查询unit
	 * @param unit
	 * @return
	 */
    BaseVo findByType(Unit unit);

	/**
	 * 根据id集合查单位
	 * @param ids
	 * @return
	 */
	BaseVo findByIds(List<Long> ids);
}
