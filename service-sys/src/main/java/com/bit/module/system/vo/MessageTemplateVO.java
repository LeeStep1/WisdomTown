package com.bit.module.system.vo;

import com.bit.module.system.bean.MessageTemplateRelTid;
import lombok.Data;

import java.util.List;

/**
 * @Description: 推送模板的类，主要针对t_sys_messagetemplate,t_sys_message_category
 * @Author: liyujun
 * @Date: 2020-04-08
 **/
@Data
public class MessageTemplateVO {

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
     * 类目id
     */
    private Long categoryId;


    /**
     * 类目的名称 对应t_sys_message_category 表
     */
    private String categoryName;

    /**
     * 消息名称
     */
    private String MsgTypeName;

    /**
     * 模板与平台的关系
     */
    private List<MessageTemplateRelTid> messageTemplateRelTids;
}
