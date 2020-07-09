package com.bit.module.vol.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-05 9:58
 */
@Data
public class StationVO extends BasePageVo{
    /**
     * id
     */
    private Long id;
    /**
     * 站点代码
     */
    private String stationCode;
    /**
     * 站点名称
     */
    private String stationName;
    /**
     * 第一负责人
     */
    private String firstChargeMan;
    /**
     * 第一负责人手机号
     */
    private String firstChargeManMobile;
    /**
     * 站长姓名
     */
    private String stationLeader;
    /**
     * 站长手机号
     */
    private String stationLeaderMobile;
    /**
     * 副站长姓名
     */
    private String viceStationLeader;
    /**
     * 副站长手机号
     */
    private String viceStationLeaderMobile;
    /**
     * 站点介绍
     */
    private String stationIntroduction;
    /**
     * 服务站类型 0-默认类型 1-镇团委 2-村服务站 3-社区服务站 4-企业服务站
     */
    private Integer stationType;
    /**
     * 服务站人数
     */
    private Integer stationNumber;

    /**
     * 活动次数
     */
    private Integer stationCampaignCount;
    /**
     * 活动时长
     */
    private Integer stationCampaignHour;
    /**
     * 捐款金额
     */
    private BigDecimal stationDonateMoney;
    /**
     * 状态 0-停用 1-启用
     */
    private Integer stationStatus;

    /**
     * 活动主题
     */
    private String campaignTheme;
    /**
     * 层级
     */
    private Integer stationLevel;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 共建单位 0-不是 1-是
     */
    private Integer partnerOrgType;
    /**
     * 服务站地址
     */
    private String stationLocation;
    /**
     * 活动资料
     */
    private String informationFile;
    /**
     * 活动状态  0-未开始 1-进行中 2-已结束 3-已取消 4-默认值
     */
    private Integer campaignStatus;

}
