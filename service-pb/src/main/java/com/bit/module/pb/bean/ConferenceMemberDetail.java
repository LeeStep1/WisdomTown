package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-17 11:26
 */
@Data
public class ConferenceMemberDetail {
    /**
     * 会议id
     */
    private Long conferenceId;
    /**
     * 党员姓名
     */
    private String name;
    /**
     * 学习时间
     */
    private Date signTime;
    /**
     * 签到情况 0-未签到 1-已签到
     */
    private Integer signSituation;
    /**
     * 签到状况 0-正常 1-迟到
     */
    private Integer signCondition;
    /**
     * 迟到原因
     */
    private String lateReason;
    /**
     * 迟到人员手机号
     */
    private String mobile;
    /**
     * 党员身份证号
     */
    private String idCard;
}
