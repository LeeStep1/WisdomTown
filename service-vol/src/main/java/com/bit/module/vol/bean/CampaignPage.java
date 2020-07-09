package com.bit.module.vol.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-04 14:16
 */
@Data
public class CampaignPage {
    /**
     * id
     */
    private Long id;
    /**
     * 活动主题
     */
    private String campaignTheme;
    /**
     * 活动类型
     */
    private String campaignType;
    /**
     * 活动开始日期
     */
    private Integer startDate;
    /**
     * 活动结束日期
     */
    private Integer endDate;
    /**
     * 活动开始小时分钟
     */
    private String startTime;
    /**
     * 活动结束小时分钟
     */
    private String endTime;
    /**
     * 活动服务时长
     */
    private BigDecimal campaignHour;
    /**
     * 活动总服务时长
     */
    private BigDecimal campaignAllHour;
    /**
     * 活动捐献金额
     */
    private BigDecimal campaignDonateMoney;
    /**
     * 状态 0-未开始 1-进行中 2-已结束 3-已取消 4-默认值
     */
    private Integer campaignStatus;
    /**
     * 活动范围 0-站外 1-站内
     */
    private Integer campaignScale;
    /**
     * 站点id
     */
    private Long stationId;
    /**
     * 站点名称
     */
    private String stationName;
    /**
     * 活动人数
     */
    private Integer campaignNumber;
    /**
     * 负责人
     */
    private String chargeMan;
    /**
     * 负责人手机号
     */
    private String chargeManMobile;
    /**
     * 状态 0-已通过 1-草稿 2-待审核 3-已拒绝 4-默认值
     */
    private Integer campaignAudit;
    /**
     * 报名人数
     */
    private Integer enrollNumber;
    /**
     * 签到人数
     */
    private Integer signNumber;
    /**
     * 活动地址
     */
    private String campaignPlace;
    /**
     * 锁
     */
    private Integer version;
    /**
     * 开始日期
     */
    private Date beginDate;
    /**
     * 结束日期
     */
    private Date finishDate;
    /**
     * 当前日期
     */
    private Date nowDate;
}
