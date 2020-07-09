package com.bit.module.system.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * UserRelRole
 * @author generator
 */
@Data
public class UserRelRole implements Serializable {

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


