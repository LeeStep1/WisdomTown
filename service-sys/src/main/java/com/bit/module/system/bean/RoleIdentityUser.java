package com.bit.module.system.bean;

import lombok.Data;

@Data
public class RoleIdentityUser {
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 身份id
     */
    private Long identityId;
}
