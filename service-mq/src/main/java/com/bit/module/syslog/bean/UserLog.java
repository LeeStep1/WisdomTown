package com.bit.module.syslog.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author liuyancheng
 * @create 2019-02-21 14:05
 */
@Data
public class UserLog extends SysLog{
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 登录ip
     */
    private String ip;
    /**
     * 操作页面
     */
    private String page;
    /**
     * 操作内容
     */
    private String content;
}
