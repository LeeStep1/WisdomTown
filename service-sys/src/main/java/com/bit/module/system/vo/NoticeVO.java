package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-02-14 10:44
 */
@Data
public class NoticeVO extends BasePageVo {
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
     * 用户id
     */
    private Long userId;
    /**
     * 记录类型 1-消息 2-待办 3-通知 4-公告 5-已办
     */
    private Integer noticeType;
    /**
     * 字典表模块名称
     */
    private String module;

    /**
     * 类目id
     */
    private Integer categoryId;

    /**
     * 消息状态 0未读 1已读
     */
    private Integer readed;
    /**
     * 接入端
     */
    private Integer tid;
}
