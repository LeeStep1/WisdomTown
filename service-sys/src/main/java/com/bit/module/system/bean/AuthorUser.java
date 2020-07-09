package com.bit.module.system.bean;

import lombok.Data;

/**
 * 用户授权
 * @author Liy
 */
@Data
public class AuthorUser {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户身份Id
     */
    private Long identityId;

    /**
     * 接入appId
     */
    private Long appId;

}
