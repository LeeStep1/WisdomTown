package com.bit.soft.push.payload;

import lombok.Data;

/**
 * 存入mongo数据库的message
 *  通知，公告，待办，消息，存入MongoDB中对应的数据结构
 * @author mifei
 * @create 2019-04-03 9:31
 **/
@Data
public class UserMessage {
    /**
     * mongo主键id
     */
    private String _id;
    /**
     * 对应的业务表id
     */
    private Long businessId;
    /**
     * 消息类型 1消息 2待办 3通知 4公告 5已办
     */
    private Integer msgType;

    /**
     * 消息类型名称
     */
    private String msgTypeName;
    /**
     * 类目 移动端识别码code
     */
    private String categoryCode;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 类目的ID 新增
     */
    private String categoryId;
    /**
     * 消息
     */
    private String content;
    /**
     * 接入端
     */
    private String tid;
    /**
     * 应用id
     */
    private Integer appid;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 消息状态 0未读 1已读
     */
    private Integer status;
    /**
     * 接收时间
     */
    private String recTime;
    /**
     * 作者
     */
    private String creater;
    /**
     * 锁
     */
    private Integer version;

    /**
     * 一级菜单
     */
    private Integer levelOneMenu;

}
