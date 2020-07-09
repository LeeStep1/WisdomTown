package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserRelRole
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRelRoleVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 居民用户的id
     */	
	private Long userId;
    /**
     * 角色id
     */	
	private Long roleId;

	//columns END

}


