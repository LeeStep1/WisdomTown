package com.bit.module.system.dao;

import java.util.List;

import com.bit.base.vo.BaseVo;
import org.apache.ibatis.annotations.Param;
import com.bit.module.system.bean.Role;
import com.bit.module.system.vo.RoleVO;
import org.springframework.stereotype.Repository;

/**
 * Role管理的Dao
 * @author
 */
public interface RoleDao {
	/**
	 * 根据条件查询Role
	 * @param roleVO
	 * @return
	 */
	List<Role> findByConditionPage(RoleVO roleVO);

	/**
	 * 查询所有Role
	 * @return
	 */
	List<Role> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个Role
	 * @param id	 	 
	 * @return
	 */
	Role findById(@Param(value = "id") Long id);

	/**
	 * 保存Role
	 * @param role
	 */
	void add(Role role);

	/**
	 * 更新Role
	 * @param role
	 */
	void update(Role role);

	/**
	 * 删除Role
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 校验名称不能唯一
	 * @param roleName
	 * @return
	 */
	int checkRoleName(@Param(value = "roleName") String roleName);

	/**
	 * 校验角色编码唯一
	 * @param role
	 * @return
	 */
	int checkRoleCode(Role role);

	/**
	 * 根据应用id 查询角色
	 * @param appId
	 * @return
	 */
    List<Role> findRoleByApp(@Param(value = "appId") Integer appId);

	/**
	 * 根据appId查询rolecode
	 * @param role
	 * @return
	 */
	List<String> selectLikeRoleCode(Role role);

	/**
	 * 根据用户id 查询他所有角色id
	 * @param userId
	 * @return
	 */
	List<Long> findRoleIdByUserId(@Param(value = "userId") Long userId);
}
