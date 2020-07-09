package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-18 11:08
 */
@Data
public class SignInfo {
    /**
     * id
     */
    private Long id;
    /**
     * 会议主题
     */
    private String theme;
    /**
     * 会议地点
     */
    private String place;
    /**
     * 会议时间
     */
    private Date startTime;
    /**
     * 会议持续时间 按小时计算
     */
    private Integer lastTime;
    /**
     * 会议提前签到时间
     */
    private Integer aheadTime;
    /**
     * 参会人员id
     */
    private Long userId;
    /**
     * 党组织id
     */
    private Long pbId;
    /**
     * 接入端
     */
    private Long appId;
    /**
     * 模块名
     */
    private String module;
    /**
     * 迟到原因
     */
    private String lateReason;
    /**
     * 签到时间
     */
    private Date signTime;
    /**
     * 党组织名称
     */
    private String pbName;

}
