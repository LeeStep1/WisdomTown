package com.bit.module.system.dao;

import com.bit.module.system.bean.UserRelRole;
import com.bit.module.system.vo.UserRelRoleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserRelRole管理的Dao
 * @author 
 *
 */
@Repository
public interface UserRelRoleDao {

	/**
	 * 根据条件查询UserRelRole
	 * @param userRelRoleVO
	 * @return
	 */
	List<UserRelRole> findByConditionPage(UserRelRoleVO userRelRoleVO);

	/**
	 * 查询所有UserRelRole
	 * @return
	 */
	List<UserRelRole> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个UserRelRole
	 * @param id	 	 
	 * @return
	 */
	UserRelRole findById(@Param(value = "id") Long id);

	/**
	 * 保存UserRelRole
	 * @param userRelRole
	 */
	void add(UserRelRole userRelRole);

	/**
	 * 更新UserRelRole
	 * @param userRelRole
	 */
	void update(UserRelRole userRelRole);

	/**
	 * 删除UserRelRole
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

}
