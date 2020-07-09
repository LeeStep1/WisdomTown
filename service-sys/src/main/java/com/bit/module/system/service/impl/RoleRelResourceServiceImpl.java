package com.bit.module.system.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.SysConst;
import com.bit.module.system.bean.Role;
import com.bit.module.system.bean.RoleRelResource;
import com.bit.module.system.dao.RoleDao;
import com.bit.module.system.dao.RoleRelResourceDao;
import com.bit.module.system.service.RoleRelResourceService;
import com.bit.module.system.vo.ResourcesRoleVo;
import com.bit.module.system.vo.RoleRelResourceVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * RoleRelResource的Service实现类
 * @author codeGenerator
 *
 */
@Service("roleRelResourceService")
public class RoleRelResourceServiceImpl extends BaseService implements RoleRelResourceService {
	
	private static final Logger logger = LoggerFactory.getLogger(RoleRelResourceServiceImpl.class);
	
	@Autowired
	private RoleRelResourceDao roleRelResourceDao;

    @Autowired
    private RoleDao roleDao;
	/**1
	 * 根据条件查询RoleRelResource
	 * @param roleRelResourceVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(RoleRelResourceVO roleRelResourceVO){
		PageHelper.startPage(roleRelResourceVO.getPageNum(), roleRelResourceVO.getPageSize());
		List<RoleRelResource> list = roleRelResourceDao.findByConditionPage(roleRelResourceVO);
		PageInfo<RoleRelResource> pageInfo = new PageInfo<RoleRelResource>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 查询所有RoleRelResource
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<RoleRelResource> findAll(String sorter){
		return roleRelResourceDao.findAll(sorter);
	}

	/**
	 * 通过主键查询单个RoleRelResource
	 * @param id
	 * @return
	 */
	@Override
	public RoleRelResource findById(Long id){
		return roleRelResourceDao.findById(id);
	}

	/**
	 * 保存RoleRelResource
	 * @param roleRelResource
	 */
	@Override
	@Transactional
	public void add(RoleRelResource roleRelResource){
		roleRelResourceDao.add(roleRelResource);
	}

	/**
	 * 更新RoleRelResource
	 * @param roleRelResource
	 */
	@Override
	@Transactional
	public void update(RoleRelResource roleRelResource){
		roleRelResourceDao.update(roleRelResource);
	}

	/**
	 * 批量删除RoleRelResource
	 * @param ids
	 */
	@Override
	@Transactional
	public void batchDelete(List<Long> ids){
		roleRelResourceDao.batchDelete(ids);
	}

	/**
	 * 批量保存
	 * @param list
	 */
	@Override
	@Transactional
	public void batchAdd(List<RoleRelResource> list) {
		roleRelResourceDao.batchAdd(list);
	}

    /**
     * 角色设置资源权限
     * @param resourcesRoleVo
     * @return
     */
	@Override
    @Transactional
	public BaseVo setResources(ResourcesRoleVo resourcesRoleVo) {
        Long roleId = resourcesRoleVo.getRoleId();
        roleRelResourceDao.delByRoleId(roleId);
        List<Long> resourceIds = resourcesRoleVo.getResourceIds();
        List<RoleRelResource> roleRelResources = new ArrayList<>();
        for (Long resourceId : resourceIds) {
            RoleRelResource roleRelResource = new RoleRelResource();
            roleRelResource.setResourceId(resourceId);
            roleRelResource.setRoleId(roleId);
            roleRelResources.add(roleRelResource);
        }
        roleRelResourceDao.batchAdd(roleRelResources);
        Role role = new Role();
        role.setId(roleId);
        role.setAlreadySet(SysConst.ALREADYSET);
        roleDao.update(role);
		BaseVo baseVo = new BaseVo();
		return baseVo;
	}

	/**
	 * 删除RoleRelResource
	 * @param id
	 */
	@Override
	@Transactional
	public void delete(Long id){
		roleRelResourceDao.delete(id);
	}

}
