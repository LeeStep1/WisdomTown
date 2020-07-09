package com.bit.module.manager.dao;

import com.bit.module.manager.bean.PortalUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User管理的Dao
 * @author 
 *
 */
@Repository
public interface PortalUserDao {

	/**
	 * 根据条件查询User
	 * @param portalUser
	 * @return
	 */
	List<PortalUser> findByConditionPage(PortalUser portalUser);

	/**
	 * 查询所有User
	 * @return
	 */
	List<PortalUser> findAll();

	/**
	 * 通过主键查询单个User
	 * @param id
	 * @return
	 */
	PortalUser findById(@Param(value = "id") Long id);

	/**
	 * 保存User新方法
	 * @param portalUser
	 */
	void addNew(PortalUser portalUser);

	/**
	 * 更新User
	 * @param portalUser
	 */
	void update(PortalUser portalUser);

	/**
	 * 删除User
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);


	/**
	 * 根据用户名查询用户信息
	 * @param username
	 * @return
	 */
	PortalUser findByUsername(@Param(value = "username") String username);


}
