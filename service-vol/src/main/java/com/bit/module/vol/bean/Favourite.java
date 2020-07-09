package com.bit.module.vol.bean;

import lombok.Data;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-08 15:17
 */
@Data
public class Favourite {

    /**
     * id
     */
    private Long id;
    /**
     * 活动id
     */
    private Long campaignId;
    /**
     * 志愿者id
     */
    private Long volunteerId;

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
     * 活动地址
     */
    private String campaignPlace;
    /**
     * 活动人数
     */
    private Integer campaignNumber;
    /**
     * 活动图片
     */
    private Long campaignImage;
    /**
     * 状态 0-草稿  1-未开始 2-进行中 3-已结束 4-已取消
     */
    private Integer campaignStatus;
    /**
     * 报名人数
     */
    private Integer enrollNumber;
    /**
     * 签到状态 0-未签到 1-已签到
     */
    private Integer signStatus;
    /**
     * 活动类型集合
     */
    private List<String> campaignTypeList;
    /**
     * 文件
     */
    private FileInfo fileInfo;
}
