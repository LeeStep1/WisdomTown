package com.bit.soft.push.payload;

import lombok.Data;

import java.util.List;

/**
 * ms工程处理的消息实体类
 * @author mifei
 * @create 2019-02-16 19:37
 */
@Data
public class MsMessage {

    /**
     * 对应的业务表id
     */
    private Long businessId;
    /**
     * 用户id
     */
    private Long[] userId;
    /**
     * 接入端
     */
    private String tid;
    /**
     *  1为使用tag, 给所有用户推送
     */
    private Integer tagType;
    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 消息类型名称
     */
    private String msgTypeName;

    /**
     * 消息
     */
    private String content;
    /**
     * 类目code
     */
    private String categoryCode;

    /**
     * 类目名称
     */
    private String categoryName;
    /**
     * 标题(极光)
     */
    private String title;

    /**
     * AppId
     */
    private Long appId;

    /**
     * 发送时间
     */
    private String time;

    /**
     * web跳转地址
     */
    private String webUrl;

    /**
     * APP跳转地址
     */
    private String appUrl;

    /**
     * 是否需要跳转 0、需要  1、不需要
     */
    private Integer jumpFlag;

   /**新增参数tagList**/
   /**
    * 标签推送时会有标签列表
    * */
    private List<String>tagList;

    /**
     * 类目code
     */
    private String categoryId;
}
