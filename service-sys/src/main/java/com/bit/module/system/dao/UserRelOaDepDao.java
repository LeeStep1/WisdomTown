package com.bit.module.system.dao;

import java.util.List;

import com.bit.module.system.bean.OrgAndName;
import com.bit.module.system.bean.User;
import com.bit.module.system.bean.UserRelOaDep;
import com.bit.module.system.vo.UserRelOaDepVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * UserRelOaDep管理的Dao
 * @author 
 *
 */
@Repository
public interface UserRelOaDepDao {
	/**
	 * 根据条件查询UserRelOaDep
	 * @param userRelOaDepVO
	 * @return
	 */
	public List<UserRelOaDep> findByConditionPage(UserRelOaDepVO userRelOaDepVO);
	/**
	 * 查询所有UserRelOaDep
	 * @return
	 */
	public List<UserRelOaDep> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个UserRelOaDep
	 * @param id	 	 
	 * @return
	 */
	public UserRelOaDep findById(@Param(value = "id") Long id);
	/**
	 * 保存UserRelOaDep
	 * @param userRelOaDep
	 */
	public void add(UserRelOaDep userRelOaDep);
	/**
	 * 更新UserRelOaDep
	 * @param userRelOaDep
	 */
	public void update(UserRelOaDep userRelOaDep);
	/**
	 * 删除UserRelOaDep
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 批量保存
	 * @param list
	 */
    void batchAdd(@Param(value = "list") List<UserRelOaDep> list);

	/**
	 * 根据用户id删除
	 * @param userId
	 */
	void delByUserId(@Param(value = "userId")Long userId);

    /**
     * 根据组织id  姓名 查询政务用户
     * @param orgAndName
     * @return
     */
	List<User> getOaUserByName(OrgAndName orgAndName);

	/**
	 * 根据参数查询所有
	 * @param userRelOaDep
	 * @return
	 */
	List<UserRelOaDep> findAllByParam(UserRelOaDep userRelOaDep);
}
