package com.bit.soft.configserver.bean;

import lombok.Data;

/**
 * 配置中心用户
 * @author Liy
 */
@Data
public class User {

    /**
     * 登录用户名
     */
    private String userName;

    /**
     * 登录密码
     */
    private String password;
}
