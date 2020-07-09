package com.bit.module.system.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Study实体类
 * @author zhangjie
 * @date 2019-1-11
 */
@Data
public class Study {

    /**
     * id
     */
    private Long id;
    /**
     * 计划主题
     */
    private String theme;
    /**
     * 计划内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建用户id
     */
    private Long createUserId;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新用户id
     */
    private Long updateUserId;
    /**
     * 是否发布  0-草稿  1-已发布
     */
    private Integer isRelease;
    /**
     * 党组织id
     */
    private Long pborgId;
    /**
     * 党组织名称
     */
    private String pborgName;
    /**
     * 发布时间
     */
    private Date releaseTime;

    /**
     * 附件id
     */
    private Long fileId;
    /**
     * 附件集合
     */
    private List<FileInfo> fileInfos;
    /**
     * 党员ids
     */
    private List<Long> memberIds;

    /**
     * 学习情况 0-已学习  1-未学习
     */
    private Integer studySituation;


    /**
     * 创建者用户名
     */
    private String createUserName;
    /**
     * 创建者真实姓名
     */
    private String createRealName;
    /**
     * 更新者用户名
     */
    private String updateUserName;
    /**
     * 更新者真实姓名
     */
    private String updateRealName;
    /**
     * 锁
     */
    private Integer version;

}
