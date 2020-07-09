package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.RoleRelResource;
import com.bit.module.system.vo.ResourcesRoleVo;
import com.bit.module.system.vo.RoleRelResourceVO;

import java.util.List;
/**
 * RoleRelResource的Service
 * @author liqi
 */
public interface RoleRelResourceService {
	/**
	 * 根据条件查询RoleRelResource
	 * @param roleRelResourceVO
	 * @return
	 */
	BaseVo findByConditionPage(RoleRelResourceVO roleRelResourceVO);

	/**
	 * 查询所有RoleRelResource
	 * @param sorter 排序字符串
	 * @return
	 */
	List<RoleRelResource> findAll(String sorter);

	/**
	 * 通过主键查询单个RoleRelResource
	 * @param id
	 * @return
	 */
	RoleRelResource findById(Long id);

	/**
	 * 保存RoleRelResource
	 * @param roleRelResource
	 */
	void add(RoleRelResource roleRelResource);

	/**
	 * 更新RoleRelResource
	 * @param roleRelResource
	 */
	void update(RoleRelResource roleRelResource);

	/**
	 * 删除RoleRelResource
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 批量删除RoleRelResource
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 批量保存RoleRelResource
	 * @param list
	 */
	void batchAdd(List<RoleRelResource> list);

	/**
	 * 角色设置资源
	 * @param resourcesRoleVo
	 * @return
	 */
    BaseVo setResources(ResourcesRoleVo resourcesRoleVo);
}
