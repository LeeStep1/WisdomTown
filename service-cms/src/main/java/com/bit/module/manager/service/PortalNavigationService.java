package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalNavigation;
import com.bit.module.manager.vo.PortalNavigationVO;

import java.util.List;
/**
 * PortalNavigation的Service
 * @author liuyancheng
 */
public interface PortalNavigationService {
	/**
	 * 根据条件查询PortalNavigation
	 * @param portalNavigationVO
	 * @return
	 */
	BaseVo findByConditionPage(PortalNavigationVO portalNavigationVO);
	/**
	 * 查询所有PortalNavigation
	 * @param sorter 排序字符串
	 * @return
	 */
	List<PortalNavigation> findAll(String sorter);
	/**
	 * 通过主键查询单个PortalNavigation
	 * @param id
	 * @return
	 */
	PortalNavigation findById(Long id);
	/**
	 * 保存PortalNavigation
	 * @param portalNavigation
	 */
	BaseVo add(PortalNavigation portalNavigation);
	/**
	 * 更新PortalNavigation
	 * @param portalNavigation
	 */
	BaseVo update(PortalNavigation portalNavigation);
	/**
	 * 删除PortalNavigation
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 查询id
	 * @param portalNavigation
	 * @return
	 */
    BaseVo queryId(PortalNavigation portalNavigation);

	/**
	 * 根据站点获取导航树
	 * @author liyang
	 * @date 2019-05-14
	 * @param stationId : 站点ID
	 * @return : BaseVo
	 */
	BaseVo getNavigationTreeByStationId(Long stationId);

	/**
	 * 获取导航树(内容发布时使用，不包含特殊)
	 * @author liyang
	 * @date 2019-06-03
	 * @return : BaseVo
	 */
	BaseVo getNavigationTreeExSpecial(Long stationId);

}
