package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;

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
     * 服务站类型
     */
    private Integer stationType;
    /**
     * 服务站人数
     */
    private Integer stationNumber;
    /**
     * 活动次数
     */
    private Integer stationCampaignNumber;
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
     * 状态 0-启用 1-停用
     */
    private Integer stationStatus;
    /**
     * 第一负责人
     */
    private String firstChargeMan;
    /**
     * 第一负责人手机号
     */
    private String firstChargeManMobile;
}
