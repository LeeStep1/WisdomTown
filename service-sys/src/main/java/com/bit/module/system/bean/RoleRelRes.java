package com.bit.module.system.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * RoleRelRes
 * @author generator
 */
@Data
public class RoleRelRes implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 角色ID
     */	
	private Long roleId;
    /**
     * 资源ID
     */	
	private Long resId;

	//columns END

}


