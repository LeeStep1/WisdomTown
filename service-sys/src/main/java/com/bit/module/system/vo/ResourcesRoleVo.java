package com.bit.module.system.vo;

import lombok.Data;

import java.util.List;

/**
 * 设置资源角色
 * liqi
 */
@Data
public class ResourcesRoleVo {

    /**
     * 角色id
     */
    private Long RoleId;
    /**
     * 资源id
     */
    private List<Long> resourceIds;
}
