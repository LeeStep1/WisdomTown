package com.bit.module.system.dao;

import com.bit.module.system.bean.OaDepartment;
import com.bit.module.system.bean.User;
import com.bit.module.system.bean.UserRelCboDep;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRelCboDepDao {

	/**
	 * 根据用户id删除
	 * @param userId
	 */
	void delByUserId(@Param(value = "userId")Long userId);

	/**
	 * 批量新增
	 */
	void batchAdd(@Param(value = "list") List<UserRelCboDep> list);
	/**
	 * 根据用户userId 查询
	 * @param userId
	 * @return
	 */
	OaDepartment findByUserId(@Param(value = "userId") Long userId);

	/**
	 * 根据社区id查询社区管理员
	 * @param orgId
	 * @return
	 */
	List<User> getUserByCboDep(@Param(value = "orgId")Long orgId);

	/**
	 * 多参数查询
	 * @param userRelCboDep
	 * @return
	 */
	List<UserRelCboDep> findByParam(UserRelCboDep userRelCboDep);



	/**
	 * 根据社区id查询社区管理员
	 * @param orgIds
	 * @return List<Long>
	 */
	List<Long> getUserByCboDeps(@Param(value = "orgIds")List<Long> orgIds);
}
