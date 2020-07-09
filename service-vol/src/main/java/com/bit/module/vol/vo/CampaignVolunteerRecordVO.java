package com.bit.module.vol.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-04 16:22
 */
@Data
public class CampaignVolunteerRecordVO extends BasePageVo{

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
     * 活动时长
     */
    private BigDecimal campaignHour;
    /**
     * 捐款金额
     */
    private BigDecimal donateMoney;
    /**
     * 签到状态 0-已签到 1-未签到
     */
    private Integer signStatus;
    /**
     * 签到时间
     */
    private Date signTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 站点id
     */
    private Long stationId;
    /**
     * 记录状态 0-停用 1-启用
     */
    private Integer recordStatus;
    /**
     * 版本号
     */
    private Integer version;

}
