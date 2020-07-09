package com.bit.module.system.dao;

import com.bit.module.system.bean.Identity;
import com.bit.module.system.bean.UserRelApp;
import com.bit.module.system.bean.UserRelIdentity;
import com.bit.module.system.vo.UserRelIdentityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserRelIdentity管理的Dao
 * @author liqi
 *
 */
public interface UserRelIdentityDao {

	/**
	 * 根据条件查询UserRelIdentity
	 * @param userRelIdentityVO
	 * @return
	 */
	public List<UserRelIdentity> findByConditionPage(UserRelIdentityVO userRelIdentityVO);

	/**
	 * 查询所有UserRelIdentity
	 * @return
	 */
	public List<UserRelIdentity> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个UserRelIdentity
	 * @param id	 	 
	 * @return
	 */
	public UserRelIdentity findById(@Param(value = "id") Long id);

	/**
	 * 保存UserRelIdentity
	 * @param userRelIdentity
	 */
	public void add(UserRelIdentity userRelIdentity);

	/**
	 * 更新UserRelIdentity
	 * @param userRelIdentity
	 */
	public void update(UserRelIdentity userRelIdentity);

	/**
	 * 删除UserRelIdentity
	 * @param ids
	 */
	public void batchDelete(@Param(value = "ids") List<Long> ids);

	/**
	 * 删除UserRelIdentity
	 * @param id
	 * @param userId
	 * @param identityId
	 */
	public void delete(@Param(value = "id") Long id,@Param(value = "userId") Long userId,@Param(value = "identityId") Long identityId);

	/**
	 * 批量保存
	 * @param userRelIdentities
	 */
	void batchAdd(@Param(value = "list")List<UserRelIdentity> userRelIdentities);

	/**
	 * 根据用户id 删除
	 * @param userId
	 */
    void delByUserId(@Param(value = "userId") Long userId);

	/**
	 * 根据用户id查询
	 * @param userId
	 * @return
	 */
    List<UserRelIdentity> findByUserId(@Param(value = "userId")  Long userId);

	/**
	 * 根据身份id 统计查询
	 * @param identityId
	 * @return
	 */
	int findCountByIdentityId(@Param(value = "identityId") Long identityId);
}
