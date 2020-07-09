package com.bit.module.manager.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.bit.module.manager.bean.PortalStation;
import com.bit.module.manager.vo.PortalStationVO;
import com.bit.base.vo.BaseVo;
/**
 * PortalStation的Service
 * @author liuyancheng
 */
public interface PortalStationService {
	/**
	 * 根据条件查询PortalStation
	 * @param portalStationVO
	 * @return
	 */
	BaseVo findByConditionPage(PortalStationVO portalStationVO);
	/**
	 * 查询所有PortalStation
	 * @param sorter 排序字符串
	 * @return
	 */
	List<PortalStation> findAll(String sorter);
	/**
	 * 通过主键查询单个PortalStation
	 * @param id
	 * @return
	 */
	PortalStation findById(Long id);

	/**
	 * 保存PortalStation
	 * @param portalStation
	 */
	BaseVo add(PortalStation portalStation);
	/**
	 * 更新PortalStation
	 * @param portalStation
	 */
	BaseVo update(PortalStation portalStation);
	/**
	 * 删除PortalStation
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 查询树
	 * @param portalStation
	 * @return
	 */
    BaseVo queryTree();

	/**
	 * 查询id
	 * @return
	 */
	BaseVo queryId();
}
