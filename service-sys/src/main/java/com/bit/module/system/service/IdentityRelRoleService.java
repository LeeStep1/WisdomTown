package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.IdentityRelRole;
import com.bit.module.system.vo.IdentityRelRoleVO;

import java.util.List;
/**
 * IdentityRelRole的Service
 * @author liqi
 */
public interface IdentityRelRoleService {
	/**
	 * 根据条件查询IdentityRelRole
	 * @param identityRelRoleVO
	 * @return
	 */
	BaseVo findByConditionPage(IdentityRelRoleVO identityRelRoleVO);

	/**
	 * 查询所有IdentityRelRole
	 * @param sorter 排序字符串
	 * @return
	 */
	List<IdentityRelRole> findAll(String sorter);

	/**
	 * 通过主键查询单个IdentityRelRole
	 * @param id
	 * @return
	 */
	IdentityRelRole findById(Long id);

	/**
	 * 保存IdentityRelRole
	 * @param identityRelRole
	 */
	void add(IdentityRelRole identityRelRole);

	/**
	 * 更新IdentityRelRole
	 * @param identityRelRole
	 */
	void update(IdentityRelRole identityRelRole);

	/**
	 * 删除IdentityRelRole
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 批量删除IdentityRelRole
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

}
