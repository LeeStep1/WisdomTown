package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-17 13:44
 */
@Data
public class StudyRecord {

    /**
     * id
     */
    private Long id;
    /**
     * 学习计划id
     */
    private Long studyId;
    /**
     * 人员id
     */
    private Long userId;
    /**
     * 学习情况 0-已学习  1-未学习
     */
    private Integer studySituation;
    /**
     * 学习时间
     */
    private Integer studyTime;
    /**
     * 学习人员类型 0-必学人员 1-自学人员
     */
    private Integer userType;
    /**
     * 公开时间
     */
    private Date releaseTime;
    /**
     * 主题
     */
    private String theme;
    /**
     * 党组织名称
     */
    private String name;
}
