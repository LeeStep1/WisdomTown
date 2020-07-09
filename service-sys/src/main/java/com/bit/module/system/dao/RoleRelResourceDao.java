package com.bit.module.system.dao;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.RoleRelResource;
import com.bit.module.system.vo.RoleRelResourceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * RoleRelResource管理的Dao
 * @author liqi
 *
 */
public interface RoleRelResourceDao {

	/**
	 * 根据条件查询RoleRelResource
	 * @param roleRelResourceVO
	 * @return
	 */
	public List<RoleRelResource> findByConditionPage(RoleRelResourceVO roleRelResourceVO);

	/**
	 * 查询所有RoleRelResource
	 * @return
	 */
	public List<RoleRelResource> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个RoleRelResource
	 * @param id	 	 
	 * @return
	 */
	public RoleRelResource findById(@Param(value = "id") Long id);

	/**
	 * 保存RoleRelResource
	 * @param roleRelResource
	 */
	public void add(RoleRelResource roleRelResource);

	/**
	 * 更新RoleRelResource
	 * @param roleRelResource
	 */
	public void update(RoleRelResource roleRelResource);

	/**
	 * 批量删除RoleRelResource
	 * @param ids
	 */
	public void batchDelete(@Param(value = "ids") List<Long> ids);

	/**
	 * 删除RoleRelResource
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 批量添加
	 * @param list
	 */
    void batchAdd(@Param(value = "list") List<RoleRelResource> list);

	/**
	 * 根据角色id删除中间表
	 * @param roleId
	 */
	void delByRoleId(@Param(value = "roleId")Long roleId);

	/**
	 * 设置资源权限回显
	 * @param roleId
	 * @return
	 */
	List<Long> findResourcesByRoleId(@Param(value = "roleId")Long roleId);

	/**
	 * 删除资源和角色的关系 根据资源id
	 * @param resourceId
	 */
    void delByResourceId(@Param(value = "resourceId")Long resourceId);

	/**
	 * 根据多个资源ids 删除资源角色关系
	 * @param resourceIds
	 */
	void batchDeleteByResourceIds(@Param(value = "ids")List<Long> resourceIds);
}
