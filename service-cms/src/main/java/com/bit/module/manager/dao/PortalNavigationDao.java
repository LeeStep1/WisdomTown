package com.bit.module.manager.dao;

import com.bit.module.manager.bean.PortalCategory;
import com.bit.module.manager.bean.PortalNavigation;
import com.bit.module.manager.vo.PortalNavigationParamsVO;
import com.bit.module.manager.vo.PortalNavigationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PortalNavigation管理的Dao
 * @author liuyancheng
 *
 */
public interface PortalNavigationDao {
	/**
	 * 根据条件查询PortalNavigation
	 * @param portalNavigationVO
	 * @return
	 */
	public List<PortalNavigation> findByConditionPage(PortalNavigationVO portalNavigationVO);

	/**
	 * 批量查询导航明细
	 * @param navigationIdList
	 * @return
	 */
	public List<PortalNavigation> findNavigationDetailsSql(@Param("navigationIdList") List<Long> navigationIdList);


	/**
	 * 查询所有PortalNavigation
	 * @return
	 */
	public List<PortalNavigation> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个PortalNavigation
	 * @param id	 	 
	 * @return
	 */
	public PortalNavigation findById(@Param(value = "id") Long id);
	/**
	 * 保存PortalNavigation
	 * @param portalNavigation
	 */
	public void add(PortalNavigation portalNavigation);
	/**
	 * 更新PortalNavigation
	 * @param portalNavigation
	 */
	public void update(PortalNavigation portalNavigation);
	/**
	 * 删除PortalNavigation
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据参数查询
	 * @param portalNavigationParamsVO
	 * @return
	 */
	List<PortalNavigation> findByParam(PortalNavigationParamsVO portalNavigationParamsVO);

	/**
	 * 根据id批量查询
	 * @param ids
	 * @return
	 */
	List<PortalNavigation> batchById(@Param("ids")List<Long> ids);

	/**
	 * 查询最大ID
	 * @param tableName
	 * @return
	 */
	Long getMaxId(@Param("tableName") String tableName,@Param("stationId") Long stationId);

	/**
	 * 获取导航树(内容发布时使用，不包含特殊)
	 * @author liyang
	 * @date 2019-06-03
	 * @param navigationIdList : 导航ID集合
	 * @return : BaseVo
	 */
	List<PortalCategory> getNavigationTreeExSpecialSql(@Param("navigationIdList") List<Long> navigationIdList, @Param("categoryType") Integer categoryType);

	/**
	 * 根据姓名查询
	 * @param portalNavigation
	 * @return
	 */
	PortalNavigation findByName(PortalNavigation portalNavigation);

}
