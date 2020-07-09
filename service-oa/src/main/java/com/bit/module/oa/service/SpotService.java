package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Spot;
import com.bit.module.oa.vo.spot.SpotDetailVO;
import com.bit.module.oa.vo.spot.SpotVO;

import java.util.List;

/**
 * Spot的Service
 * @author codeGenerator
 */
public interface SpotService {
	/**
	 * 根据条件查询Spot
	 * @param spotVO
	 * @return
	 */
	BaseVo findByConditionPage(SpotVO spotVO);
	/**
	 * 查询所有Spot
	 * @param sorter 排序字符串
	 * @return
	 */
	List<Spot> findAll(String sorter);
	/**
	 * 通过主键查询单个Spot
	 * @param id
	 * @return
	 */
	SpotDetailVO findById(Long id);
	/**
	 * 保存Spot
	 * @param spot
	 */
	void add(Spot spot);
	/**
	 * 更新Spot
	 * @param spot
	 */
	void update(Spot spot);

	/**
	 *
	 * @param id
	 * @param status
	 */
    void convertStatus(Long id, Integer status);

	/**
	 * 删除签到点
	 * @param id
	 */
	void delete(Long id);
}
