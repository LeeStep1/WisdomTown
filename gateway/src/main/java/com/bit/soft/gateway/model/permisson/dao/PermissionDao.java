package com.bit.soft.gateway.model.permisson.dao;

import com.bit.soft.gateway.model.permisson.bean.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao
 */
@Repository
public interface PermissionDao {

	@Select( " <script>" +
			" SELECT b.id as id,b.module as module,b.type as type,b.url as url,b.memo as memo " +
			"FROM `t_sys_role_rel_permission` a , `t_sys_permission` b" +
			" WHERE a.permission_id = b.id AND role_id IN" +
			" <foreach collection='list' item='id' index='index' open='(' close=')' separator=','>" +
			" #{id} " +
			" </foreach> "+
			" </script>" )
	List<Permission> selectByRoleIds(@Param(value = "list") List<Long> roleIds);

}
