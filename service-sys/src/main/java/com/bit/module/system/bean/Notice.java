package com.bit.module.system.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-02-14 10:13
 */
@Data
public class Notice {

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
     * 记录类型 1-消息 2-待办 3-通知 4-公告 5-已办
     */
    private Integer noticeType;
    /**
     * 文件id 存储字符串
     */
    private String fileId;

    /**
     * 用户ids
     */
    private List<Long> userIds;
    /**
     * 通知公告与文件关联列表
     */
//    private List<NoticeRelFile> noticeRelFileList;

    /**
     * 文件列表
     */
    private List<FileInfo> fileInfoList;
    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 发布方组织id
     */
    private Long publishOrgId;
    /**
     * 接入端
     */
    private String tid;
    /**
     * 接收人的id集合字符串
     */
    private String userIdList;
    /**
     * 发布方组织名称
     */
    private String publishOrgName;
    /**
     * 发布方组织id 字符串
     */
    private String publishOrgStr;



}
