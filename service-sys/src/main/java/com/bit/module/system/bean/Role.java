package com.bit.module.system.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Role
 * @author zhangjie
 * @date 2018-12-27
 */
@Data
public class Role implements Serializable {

    /**
     * id
     */	
	private Long id;

    /**
     * 名称
     */	
	private String roleName;

    /**
     * 角色编码
     */
	private String roleCode;

    /**
     * 是否设置0没有设置1设置
     */
	private Integer alreadySet;

    /**
     * 描述
     */
    private String remark;

    /**
     * 子系统id
     */
    private Integer appId;

    /**
     * createTime
     */	
	private Date createTime;

    /**
     * createUserId
     */	
	private Long createUserId;

    /**
     * updateTime
     */	
	private Date updateTime;

    /**
     * updateUserId
     */	
	private Long updateUserId;


}


