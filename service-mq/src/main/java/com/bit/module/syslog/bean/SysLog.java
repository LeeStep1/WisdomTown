package com.bit.module.syslog.bean;

import lombok.Data;

import java.util.Date;

/**
* @Description: 系统业务日志
* @author: mifei
* @create: 2019-02-21
**/
@Data
public class SysLog {

    /**
     * id
     */
    private Long id;
    /**
     * 应用id
     */
    private Long appId;
    /**
     * 标题
     */
    private String title;

    /**
     * 类目
     */
    private Integer categoryId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 组织id
     */
    private Long orgId;



}
