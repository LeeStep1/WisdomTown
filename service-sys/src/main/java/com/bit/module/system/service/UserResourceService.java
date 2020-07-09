package com.bit.module.system.service;

import java.util.List;

import com.bit.module.system.bean.UserResource;
import com.bit.module.system.vo.UserResourceVO;
import com.bit.base.vo.BaseVo;
/**
 * UserResource的Service
 * @author liuyancheng
 */
public interface UserResourceService {
	/**
	 * 根据条件查询UserResource
	 * @param userResourceVO
	 * @return
	 */
	BaseVo findByConditionPage(UserResourceVO userResourceVO);
	/**
	 * 查询所有UserResource
	 * @param sorter 排序字符串
	 * @return
	 */
	List<UserResource> findAll(String sorter);
	/**
	 * 通过主键查询单个UserResource
	 * @param id
	 * @return
	 */
	UserResource findById(Long id);

	/**
	 * 保存UserResource
	 * @param userResourceVO
	 */
	void add(UserResourceVO userResourceVO);
	/**
	 * 更新UserResource
	 * @param userResourceVO
	 */
	void update(UserResourceVO userResourceVO);
	/**
	 * 删除UserResource
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 根据userId查询菜单
	 * @return
	 */
	BaseVo findByUserId();
}
