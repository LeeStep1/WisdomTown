package com.bit.module.vol.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenduo
 * @create 2019-03-14 13:27
 */
@Data
public class StationExcel {
    /**
     * 序号
     */
    @Excel(name = "序号")
    private Integer num;
    /**
     * 站点代码
     */
    @Excel(name = "编号")
    private String stationCode;
    /**
     * 站点名称
     */
    @Excel(name = "服务站名称")
    private String stationName;
    /**
     * 服务站类型
     */
    @Excel(name = "服务站类型")
    private String stationType;
    /**
     * 第一负责人
     */
    @Excel(name = "第一负责人")
    private String firstChargeMan;
    /**
     * 第一负责人手机号
     */
    @Excel(name = "手机号")
    private String firstChargeManMobile;

    /**
     * 服务站人数
     */
    @Excel(name = "人数")
    private Integer stationNumber;


    /**
     * 活动次数
     */
    @Excel(name = "活动次数")
    private Integer stationCampaignCount;
    /**
     * 活动时长
     */
    @Excel(name = "活动时长")
    private BigDecimal stationCampaignHour;
    /**
     * 捐款金额
     */
    @Excel(name = "捐款金额")
    private BigDecimal stationDonateMoney;
    /**
     * 状态 0-停用 1-启用
     */
    @Excel(name = "状态")
    private String status;
    /**
     * 站长姓名
     */
    @Excel(name = "站长姓名")
    private String stationLeader;
    /**
     * 站长手机号
     */
    @Excel(name = "站长手机号")
    private String stationLeaderMobile;
    /**
     * 副站长姓名
     */
    @Excel(name = "副站长姓名")
    private String viceStationLeader;
    /**
     * 副站长手机号
     */
    @Excel(name = "副站长手机号")
    private String viceStationLeaderMobile;
    /**
     * 服务站地址
     */
    @Excel(name = "服务站地址")
    private String stationLocation;

}
