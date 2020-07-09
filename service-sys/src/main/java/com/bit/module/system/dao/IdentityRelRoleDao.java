package com.bit.module.system.dao;

import com.bit.module.system.bean.IdentityRelRole;
import com.bit.module.system.vo.IdentityRelRoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * IdentityRelRole管理的Dao
 * @author liqi
 *
 */
public interface IdentityRelRoleDao {

	/**
	 * 根据条件查询IdentityRelRole
	 * @param identityRelRoleVO
	 * @return
	 */
	public List<IdentityRelRole> findByConditionPage(IdentityRelRoleVO identityRelRoleVO);

	/**
	 * 查询所有IdentityRelRole
	 * @return
	 */
	public List<IdentityRelRole> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个IdentityRelRole
	 * @param id	 	 
	 * @return
	 */
	public IdentityRelRole findById(@Param(value = "id") Long id);

	/**
	 * 保存IdentityRelRole
	 * @param identityRelRole
	 */
	public void add(IdentityRelRole identityRelRole);

	/**
	 * 更新IdentityRelRole
	 * @param identityRelRole
	 */
	public void update(IdentityRelRole identityRelRole);

	/**
	 * 删除IdentityRelRole
	 * @param ids
	 */
	public void batchDelete(@Param(value = "ids") List<Long> ids);

	/**
	 * 删除IdentityRelRole
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据身份id查询中间表
	 * @param identityId
	 * @return
	 */
    List<IdentityRelRole> findByIdentityId(@Param(value = "identityId")Long identityId);

	/**
	 * 根据身份id删除
	 * @param identityId
	 */
	void delByIndentityId(@Param(value = "identityId")Long identityId);

	/**
	 * 批量保存
	 * @param identityRelRoles
	 */
    void batchAdd(@Param(value = "list")List<IdentityRelRole> identityRelRoles);

	/**
	 * 根据角色id 查询  统计
	 * @param roleId
	 * @return
	 */
	int findCountByRoleId(@Param(value = "roleId") Long roleId);

}
