package com.bit.module.system.vo;

import lombok.Data;

/**
 * 回显用-我的菜单
 * @author liuyancheng
 * @create 2019-02-18 15:28
 */
@Data
public class UserResourceQueryVO {
    /**
     * t_sys_user_resource 表主键id
     */
    private Long id;
    /**
     * userId
     */
    private Long userId;
    /**
     * 资源表主键id
     */
    private Long resourceId;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 资源链接
     */
    private String url;
}
