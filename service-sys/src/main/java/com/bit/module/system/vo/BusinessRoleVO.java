package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

@Data
public class BusinessRoleVO extends BasePageVo{
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
}
