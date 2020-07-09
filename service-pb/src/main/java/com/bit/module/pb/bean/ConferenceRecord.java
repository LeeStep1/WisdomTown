package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-17 11:21
 */
@Data
public class ConferenceRecord {

    /**
     * id
     */
    private Long id;
    /**
     * 会议id
     */
    private Long conferenceId;
    /**
     * 参会人员id
     */
    private Long userId;
    /**
     * 签到情况 0-已签到 1-未签到
     */
    private Integer signSituation;
    /**
     * 签到时间
     */
    private Date signTime;
    /**
     * 签到状况 0-正常 1-迟到
     */
    private Integer signCondition;
    /**
     * 迟到原因
     */
    private String lateReason;
    /**
     * 党组织名称
     */
    private String name;
    /**
     * 会议开始时间
     */
    private Date startTime;
    /**
     * 会议主题
     */
    private String theme;
}
