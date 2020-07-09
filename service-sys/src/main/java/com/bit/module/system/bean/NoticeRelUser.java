package com.bit.module.system.bean;

import lombok.Data;

/**
 * @author chenduo
 * @create 2019-02-14 11:04
 */
@Data
public class NoticeRelUser {

    /**
     * id
     */
    private Long id;
    /**
     * 通知公告id
     */
    private Long noticeId;
    /**
     * 人员id
     */
    private Long userId;
    /**
     * 是否已读 0-已读 1-未读
     */
    private Integer readed;
}
