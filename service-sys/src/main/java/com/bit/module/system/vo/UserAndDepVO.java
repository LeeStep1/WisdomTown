package com.bit.module.system.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuyancheng
 * @create 2019-01-23 16:49
 */
@Data
public class UserAndDepVO {
    /**
     * 用户主键id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户真实姓名
     */
    private String realName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 部门名称
     */
    private String depName;
    /**
     * 部门id集合
     */
    private List<Long> depIds;
}
