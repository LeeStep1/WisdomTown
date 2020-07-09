package com.bit.module.system.service;

import java.util.List;

import com.bit.module.system.bean.UserRelOaDep;
import com.bit.module.system.vo.UserRelOaDepVO;
import com.bit.base.vo.BaseVo;
/**
 * UserRelOaDep的Service
 * @author codeGenerator
 */
public interface UserRelOaDepService {
	/**
	 * 根据条件查询UserRelOaDep
	 * @param userRelOaDepVO
	 * @return
	 */
	BaseVo findByConditionPage(UserRelOaDepVO userRelOaDepVO);
	/**
	 * 查询所有UserRelOaDep
	 * @param sorter 排序字符串
	 * @return
	 */
	List<UserRelOaDep> findAll(String sorter);
	/**
	 * 通过主键查询单个UserRelOaDep
	 * @param id
	 * @return
	 */
	UserRelOaDep findById(Long id);
	/**
	 * 保存UserRelOaDep
	 * @param userRelOaDep
	 */
	void add(UserRelOaDep userRelOaDep);
	/**
	 * 更新UserRelOaDep
	 * @param userRelOaDep
	 */
	void update(UserRelOaDep userRelOaDep);
	/**
	 * 删除UserRelOaDep
	 * @param id
	 */
	void delete(Long id);
}
