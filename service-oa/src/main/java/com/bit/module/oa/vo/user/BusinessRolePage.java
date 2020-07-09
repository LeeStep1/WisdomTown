package com.bit.module.oa.vo.user;

import lombok.Data;

import java.util.List;

@Data
public class BusinessRolePage {
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
