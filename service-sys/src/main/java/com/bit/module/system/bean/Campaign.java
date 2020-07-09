package com.bit.module.system.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-04 13:56
 */
@Data
public class Campaign {
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
     * 活动负责人
     */
    private String chargeMan;
    /**
     * 活动负责人手机号
     */
    private String chargeManMobile;
    /**
     * 活动地址
     */
    private String campaignPlace;
    /**
     * 活动坐标
     */
    private String campaignCoordinate;
    /**
     * 活动人数
     */
    private Integer campaignNumber;
    /**
     * 活动时长
     */
    private BigDecimal campaignHour;
    /**
     * 活动内容
     */
    private String campaignContent;
    /**
     * 活动捐款数
     */
    private BigDecimal campaignDonateMoney;
    /**
     * 活动图片
     */
    private Long campaignImage;
    /**
     * 活动范围 0-站内 1-站外
     */
    private Integer campaignScale;
    /**
     * 活动站点id
     */
    private Long stationId;
    /**
     * 服务站姓名
     */
    private String stationName;
    /**
     * 活动总结文件
     */
    private Long summaryFile;
    /**
     * 活动资料文件
     */
    private String informationFile;
    /**
     * 状态 0-未开始 1-进行中 2-已结束 3-已取消 4-默认值
     */
    private Integer campaignStatus;
    /**
     * 状态 0-已通过 1-草稿 2-待审核 3-已拒绝 4-默认值
     */
    private Integer campaignAudit;
    /**
     * 拒绝原因
     */
    private String campaignRejectReason;
    /**
     * 取消原因
     */
    private String campaignCancelReason;


    private FileInfo fileInfo;
    /**
     * 已报名人数
     */
    private Integer enrollNumber;
    /**
     * 状态 0-未报名 1-已报名
     */
    private Integer enrollStatus;
    /**
     * 签到日期
     */
    private Date signTime;
    /**
     * 版本号
     */
    private Integer version;

    /**
     * 报名满 0-未满 1-已满
     */
    private Integer campaignFull;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 活动类型集合
     */
    private List<String> campaignTypeList;
    /**
     * 是否收藏 0-否 1-是
     */
    private Integer isFavourite;

    /**
     * 签到人数
     */
    private Integer signNumber;

    private List<Long> stationList;
}
