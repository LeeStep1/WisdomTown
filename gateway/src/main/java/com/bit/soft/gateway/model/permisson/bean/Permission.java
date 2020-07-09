package com.bit.soft.gateway.model.permisson.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 权限实体类
 */
@Data
public class Permission implements Serializable {

    /**
     * id
     */	
	private Long id;
    /**
     * 模块
     */	
	private String module;
    private String type;
    /**
     * 接口url
     */
    private String url;
    private String memo;

}


