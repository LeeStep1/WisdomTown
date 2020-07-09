package com.bit.module.system.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Resource
 * @author liqi
 */
@Data
public class Resource implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * name
     */	
	private String name;
    /**
     * 0菜单1按钮
     */	
	private Integer resType;
    /**
     * url
     */	
	private String url;
    /**
     * terminalId 终端id
     */	
	private Integer terminalId;
    /**
     * 应用id
     */	
	private Integer appId;
    /**
     * 父id
     */	
	private Long pid;
    /**
     * 1：显示 0：不显示
     */	
	private Integer display;
    /**
     * 接口
     */	
	private String api;
    /**
     * 图标
     */
    private String icon;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 孩子节点
     */
    private List<Resource> childList;

    /**
     * 临时字段--用户id
     */
    private Long userId;
    /**
     * 临时字段--角色id
     */
    private Long roleId;
    /**
     * 临时字段--身份id
     */
    private Long identityId;
    //columns END

}


