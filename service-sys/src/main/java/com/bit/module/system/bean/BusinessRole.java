package com.bit.module.system.bean;

import lombok.Data;

import java.util.List;

@Data
public class BusinessRole {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 工作流id
     */
    private Integer flowId;
    /**
     * 工作流名称
     */
    private String flowName;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 应用id
     */
    private Long appId;
    /**
     * 工作流和角色关系
     */
    List<BusinessRelRole> businessRelRoleList;
}
