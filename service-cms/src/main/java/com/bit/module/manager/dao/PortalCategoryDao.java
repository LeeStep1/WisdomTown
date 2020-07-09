package com.bit.module.manager.dao;

import com.bit.module.manager.bean.PortalCategory;
import com.bit.module.manager.vo.PortalCategoryResultVO;
import com.bit.module.manager.vo.PortalCategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PortalCategory管理的Dao
 * @author liuyancheng
 *
 */
public interface PortalCategoryDao {
	/**
	 * 根据条件查询PortalCategory
	 * @param portalCategoryVO
	 * @return
	 */
	public List<PortalCategoryResultVO> findByConditionPage(PortalCategoryVO portalCategoryVO);
	/**
	 * 根据条件查询PortalCategory
	 * @param stationId
	 * @return
	 */
	public List<PortalCategoryResultVO> findByCategoryListByStationId(@Param("stationId") Long stationId,@Param("idLength")Integer idLength);

	/**
	 * 查询所有PortalCategory
	 * @return
	 */
	public List<PortalCategory> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个PortalCategory
	 * @param id	 	 
	 * @return
	 */
	public PortalCategory findById(@Param(value = "id") Long id);
	/**
	 * 保存PortalCategory
	 * @param portalCategory
	 */
	public void add(PortalCategory portalCategory);
	/**
	 * 更新PortalCategory
	 * @param portalCategory
	 */
	public void update(PortalCategory portalCategory);
	/**
	 * 删除PortalCategory
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据参数查询
	 * @param portalCategoryVO
	 * @return
	 */
	List<PortalCategory> findByParam(PortalCategoryVO portalCategoryVO);

    /**
     * 个人服务类型 根据id 模糊查询下级记录
     * @param portalCategory
     * @return
     */
	List<PortalCategory> serviceTypeListPage(PortalCategory portalCategory);
    /**
     * 个人服务 or 法人服务 批量更新服务排序
     * @param list
     * @return
     */
	void batchUpdateRank(@Param(value = "list") List<PortalCategory> list);

	/**
	 * 根据名称查找
	 * @param portalCategory
	 * @return
	 */
	PortalCategory findByName(PortalCategory portalCategory);
}
