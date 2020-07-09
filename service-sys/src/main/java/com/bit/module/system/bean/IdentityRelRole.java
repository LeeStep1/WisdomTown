package com.bit.module.system.bean;

import lombok.Data;

/**身份——角色
 * IdentityRelRole
 * @author liqi
 */
@Data
public class IdentityRelRole {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 身份id
     */	
	private Long identityId;
    /**
     * 角色id
     */	
	private Long roleId;

	//columns END

}


