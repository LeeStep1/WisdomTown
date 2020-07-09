package com.bit.module.system.dao;

import com.bit.module.system.bean.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao
 */
@Repository
public interface PermissionDao {


	List<Permission> selectByRoleIds(@Param(value = "list") List<Long> roleIds);

}
