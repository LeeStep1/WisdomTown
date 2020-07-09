package com.bit.module.system.bean;

import lombok.Data;

/**
 * UserRelIdentity
 * @author liqi
 */
@Data
public class UserRelIdentity {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 用户id
     */	
	private Long userId;
    /**
     * 角色id
     */	
	private Long identityId;

	//columns END

}


