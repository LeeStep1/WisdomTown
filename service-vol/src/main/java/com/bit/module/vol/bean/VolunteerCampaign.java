package com.bit.module.vol.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-05 17:09
 */
@Data
public class VolunteerCampaign {
    /**
     * 活动记录id
     */
    private Long id;
    /**
     * 活动主题
     */
    private String campaignTheme;
    /**
     * 活动时长
     */
    private BigDecimal serviceHour;
    /**
     * 捐款
     */
    private BigDecimal donateMoney;
    /**
     * 开始日
     */
    private Integer startDate;
    /**
     * 结束日
     */
    private Integer endDate;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 志愿者id
     */
    private Long volunteerId;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 身份证
     */
    private String cardId;
    /**
     * 活动图片
     */
    private FileInfo fileInfo;
    /**
     * 状态 0-未开始 1-进行中 2-已结束 3-已取消 4-默认值
     */
    private Integer campaignStatus;
    /**
     * 活动地址
     */
    private String campaignPlace;
    /**
     * 活动人数
     */
    private Integer campaignNumber;
    /**
     * 报名人数
     */
    private Integer enrollNumber;
    /**
     * 活动类型
     */
    private List<String> campaignTypeList;

}
