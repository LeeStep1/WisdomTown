package com.bit.module.vol.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-04 14:16
 */
@Data
public class CampaignVO extends BasePageVo {
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
     * 活动捐款数
     */
    private BigDecimal campaignDonateMoney;
    /**
     * 活动内容
     */
    private String campaignContent;
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
    private String summaryFile;
    /**
     * 活动资料文件
     */
    private Long informationFile;

    /**
     * 状态 0-草稿  1-未开始 2-进行中 3-已结束 4-已取消
     */
    private Integer campaignStatus;
    /**
     * 状态 状态 0-已通过 1-未通过 2-待审核 3-已拒绝
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
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 报名满 0-未满 1-已满
     */
    private Integer campaignFull;

    private List<Long> stationList;

    private Long volunteerId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 活动id
     */
    private Long campaignId;
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
    /**
     * 站点等级
     */
    private Integer stationLevel;
}
