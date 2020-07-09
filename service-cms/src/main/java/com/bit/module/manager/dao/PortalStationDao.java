package com.bit.module.manager.dao;

import com.bit.module.manager.bean.PortalStation;
import com.bit.module.manager.vo.PortalStationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PortalStation管理的Dao
 * @author liuyancheng
 *
 */
public interface PortalStationDao {
	/**
	 * 根据条件查询PortalStation
	 * @param portalStationVO
	 * @return
	 */
	public List<PortalStation> findByConditionPage(PortalStationVO portalStationVO);
	/**
	 * 查询所有PortalStation
	 * @return
	 */
	public List<PortalStation> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个PortalStation
	 * @param id	 	 
	 * @return
	 */
	public PortalStation findById(@Param(value = "id") Long id);
	/**
	 * 保存PortalStation
	 * @param portalStation
	 */
	public void add(PortalStation portalStation);
	/**
	 * 更新PortalStation
	 * @param portalStation
	 */
	public void update(PortalStation portalStation);
	/**
	 * 删除PortalStation
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据参数查询
	 * @param portalStationVO
	 * @return
	 */
	List<PortalStation> findByParam(PortalStationVO portalStationVO);

	/**
	 * 根据姓名查询
	 * @param portalStation
	 * @return
	 */
	PortalStation findByName(PortalStation portalStation);
}
