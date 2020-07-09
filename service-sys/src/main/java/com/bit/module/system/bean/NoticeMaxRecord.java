package com.bit.module.system.bean;

import lombok.Data;

/**
 * @author chenduo
 * @create 2019-02-15 13:54
 */
@Data
public class NoticeMaxRecord {

    /**
     * id
     */
    private Long id;
    /**
     * 最大id
     */
    private Long maxNoticeId;

    /**
     * 用户id
     */
    private Long userId;
}
