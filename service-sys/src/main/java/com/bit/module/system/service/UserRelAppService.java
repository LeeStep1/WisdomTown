package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.UserRelApp;
import com.bit.module.system.vo.UserRelAppVO;

import java.util.List;
/**
 * UserRelApp的Service
 * @author liqi
 */
public interface UserRelAppService {
	/**
	 * 根据条件查询UserRelApp
	 * @param userRelAppVO
	 * @return
	 */
	BaseVo findByConditionPage(UserRelAppVO userRelAppVO);

	/**
	 * 查询所有UserRelApp
	 * @param sorter 排序字符串
	 * @return
	 */
	List<UserRelApp> findAll(String sorter);

	/**
	 * 通过主键查询单个UserRelApp
	 * @param id
	 * @return
	 */
	UserRelApp findById(Long id);

	/**
	 * 保存UserRelApp
	 * @param UserRelApp
	 */
	void add(UserRelApp UserRelApp);

	/**
	 * 更新UserRelApp
	 * @param UserRelApp
	 */
	void update(UserRelApp UserRelApp);

	/**
	 * 删除UserRelApp
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 批量删除UserRelApp
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 批量保存
	 * @param list
	 */
	void batchAdd(List<UserRelApp> list);
}
