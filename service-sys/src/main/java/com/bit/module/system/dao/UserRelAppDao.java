package com.bit.module.system.dao;

import com.bit.module.system.bean.UserRelApp;
import com.bit.module.system.vo.UserRelAppVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserRelApp管理的Dao
 * @author liqi
 *
 */
public interface UserRelAppDao {

	/**
	 * 根据条件查询UserRelApp
	 * @param userRelAppVO
	 * @return
	 */
	public List<UserRelApp> findByConditionPage(UserRelAppVO userRelAppVO);

	/**
	 * 查询所有UserRelApp
	 * @return
	 */
	public List<UserRelApp> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个UserRelApp
	 * @param id	 	 
	 * @return
	 */
	public UserRelApp findById(@Param(value = "id") Long id);

	/**
	 * 保存UserRelApp
	 * @param userRelApp
	 */
	public void add(UserRelApp userRelApp);

	/**
	 * 更新UserRelApp
	 * @param userRelApp
	 */
	public void update(UserRelApp userRelApp);

	/**
	 * 删除UserRelApp
	 * @param ids
	 */
	public void batchDelete(@Param(value = "ids") List<Long> ids);

	/**
	 * 删除UserRelApp
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 批量保存
	 * @param list
	 */
	void batchAdd(@Param(value = "list") List<UserRelApp> list);

	/**
	 * 根据用户id删除中间表（删除应用和用户的关系）
	 * @param userId
	 */
	void delByUserId(@Param(value = "userId") Long userId);

	/**
	 * 根据用户id查询
	 * @param userId
	 */
    List<UserRelApp> findByUserId(@Param(value = "userId") Long userId);

	/**
	 * 多参数查询
	 * @param userRelApp
	 * @return
	 */
	List<UserRelApp> findByParam(UserRelApp userRelApp);
}
