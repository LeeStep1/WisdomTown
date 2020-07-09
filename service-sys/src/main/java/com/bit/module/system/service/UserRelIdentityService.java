package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.UserRelIdentity;
import com.bit.module.system.vo.UserRelIdentityVO;

import java.util.List;
/**
 * UserRelIdentity的Service
 * @author liqi
 */
public interface UserRelIdentityService {
	/**
	 * 根据条件查询UserRelIdentity
	 * @param userRelIdentityVO
	 * @return
	 */
	BaseVo findByConditionPage(UserRelIdentityVO userRelIdentityVO);

	/**
	 * 查询所有UserRelIdentity
	 * @param sorter 排序字符串
	 * @return
	 */
	List<UserRelIdentity> findAll(String sorter);

	/**
	 * 通过主键查询单个UserRelIdentity
	 * @param id
	 * @return
	 */
	UserRelIdentity findById(Long id);


	/**
	 * 保存UserRelIdentity
	 * @param userRelIdentity
	 */
	void add(UserRelIdentity userRelIdentity);

	/**
	 * 更新UserRelIdentity
	 * @param userRelIdentity
	 */
	void update(UserRelIdentity userRelIdentity);

	/**
	 * 删除UserRelIdentity
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 批量删除UserRelIdentity
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

}
