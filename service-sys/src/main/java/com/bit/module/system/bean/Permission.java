package com.bit.module.system.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

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


