package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Zone;
import com.bit.module.oa.vo.zone.SimpleZoneVO;
import com.bit.module.oa.vo.zone.ZoneQO;
import com.bit.module.oa.vo.zone.ZoneVO;

import java.util.List;

/**
 * Zone的Service
 * @author codeGenerator
 */
public interface ZoneService {
	/**
	 * 根据条件查询Zone
	 * @param zoneVO
	 * @return
	 */
	BaseVo findByConditionPage(ZoneVO zoneVO);
	/**
	 * 查询所有Zone
	 * @param sorter 排序字符串
	 * @return
	 */
	List<SimpleZoneVO> findAll(String sorter);
	/**
	 * 通过主键查询单个Zone
	 * @param id
	 * @return
	 */
	Zone findById(Long id);
	/**
	 * 保存Zone
	 * @param zone
	 */
	void add(Zone zone);
	/**
	 * 更新Zone
	 * @param zone
	 */
	void update(Zone zone);
	/**
	 * 删除Zone
	 * @param id
	 */
	void delete(Long id);

	/**
	 *
	 * @param id
	 * @param status
	 */
    void convertStatus(Long id, Integer status);

	/**
	 * 查询ZoneSpot结构的数据
	 * @param zoneSpotVO
	 */
	BaseVo findZoneSpotVO(ZoneQO zoneSpotVO);
}
