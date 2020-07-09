package com.bit.soft.gateway.model.permisson.service.impl;

import com.bit.base.service.BaseService;
import com.bit.soft.gateway.model.permisson.bean.Permission;
import com.bit.soft.gateway.model.permisson.dao.PermissionDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author mf
 **/
@Service("permissionService")
@CacheConfig(cacheNames = "cache:permission")
public class PermissionServiceImpl extends BaseService  {

	@Autowired
	private PermissionDao permissionDao;


	@Cacheable
	public List<Permission> selectByRoleIds(@Param(value = "list") List<Long> roleIds){
		return permissionDao.selectByRoleIds(roleIds);
	}
}
