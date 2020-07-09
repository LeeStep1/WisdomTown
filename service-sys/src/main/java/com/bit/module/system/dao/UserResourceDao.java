package com.bit.module.system.dao;

import java.util.List;

import com.bit.module.system.bean.UserResource;
import com.bit.module.system.vo.UserResourceQueryVO;
import com.bit.module.system.vo.UserResourceVO;
import org.apache.ibatis.annotations.Param;

/**
 * UserResource管理的Dao
 * @author liuyancheng
 *
 */
public interface UserResourceDao {
	/**
	 * 根据条件查询UserResource
	 * @param userResourceVO
	 * @return
	 */
	public List<UserResource> findByConditionPage(UserResourceVO userResourceVO);
	/**
	 * 查询所有UserResource
	 * @return
	 */
	public List<UserResource> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个UserResource
	 * @param id	 	 
	 * @return
	 */
	public UserResource findById(@Param(value = "id") Long id);
	/**
	 * 批量保存UserResource
	 * @param userResourceVO
	 */
	public void batchAdd(UserResourceVO userResourceVO);
	/**
	 * 保存UserResource
	 * @param userResource
	 */
	public void add(UserResource userResource);
	/**
	 * 更新UserResource
	 * @param userResource
	 */
	public void update(UserResource userResource);
	/**
	 * 删除UserResource
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据用户id删除
	 * @param userId
	 */
	void deleteByUserId(@Param(value = "userId") Long userId);

	/**
	 * 根据userId查询条数
	 * @param userId
	 * @return
	 */
	int findByUserIdCount(@Param(value = "userId") Long userId);

	/**
	 * 根据userId查询菜单
	 * @param userId
	 * @return
	 */
	List<UserResourceQueryVO> findByUserId(@Param(value = "userId") Long userId);
}
