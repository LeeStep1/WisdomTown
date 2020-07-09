package com.bit.module.system.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-02-14 14:20
 */
@Data
public class NoticePage {

    /**
     * id
     */
    private Long id;
    /**
     * 应用来源id
     */
    private Long appId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 状态 0-草稿 1-已发布
     */
    private Integer noticeStatus;
    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 记录类型 0-通知 1-公告
     */
    private Integer noticeType;

    /**
     * 创建人姓名
     */
    private String authorName;
    /**
     * 平台名称
     */
    private String sourceName;
    /**
     * 是否已读 0-已读 1-未读
     */
    private Integer readed;
    /**
     * 类目名称
     */
    private String categoryName;
    /**
     * mongoId
     */
    private String mongoId;
    /**
     * 类目code
     */
    private String categoryCode;
    /**
     * 锁
     */
    private Integer version;
    /**
     * 一级菜单
     */
    private Integer levelOneMenu;
}
