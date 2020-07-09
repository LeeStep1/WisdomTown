package com.bit.module.system.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * RoleRelResource
 * @author liqi
 */
@Data
public class RoleRelResource implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 角色id
     */	
	private Long roleId;
    /**
     * 资源id
     */	
	private Long resourceId;

	//columns END

}


