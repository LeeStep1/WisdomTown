package com.bit.module.syslog.bean;

import lombok.Data;

import java.util.List;

/**
 * 模板表
 * @description:
 * @author: Liyang
 * @date: 2019-04-04
 **/
@Data
public class MessageTemplate {

    /**
     * 主键
     */
    private Long id;

    /**
     * 模板内容
     */
    private String context;

    /**
     * 类目code
     */
    private String category;

    /**
     * 消息类型 1、消息提醒  2、待办提醒  3、通知提醒  4、公告提醒
     */
    private Integer msgType;

    /**
     * 跳转URL
     */
    private String url;

    /**
     * appid
     */
    private Integer appId;

    /**
     * 用户类型 0、所有用户  1、内部用户  2、外部用户
     */
    private Integer userType;

    /**
     * 组织ID集合
     */
    private List<Long> orgIds;

    /**
     * appUrl
     */
    private String appUrl;

    /**
     * 是否需要跳转 0、需要  1、不需要
     */
    private Integer jumpFlag;

    /**
     * 一级菜单
     */
    private Integer levelOneMenu;


    //新增加字段  category_id

    /**
     * 一级菜单
     */
    private Long categoryId;
}
