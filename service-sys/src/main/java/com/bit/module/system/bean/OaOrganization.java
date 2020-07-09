package com.bit.module.system.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * OaOrganization
 * @author generator
 */
@Data
public class OaOrganization implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 代码
     */	
	private String pcode;
    /**
     * 名称
     */	
	private String name;
    /**
     * 顺序
     */	
	private Integer sort;

	//columns END

}


