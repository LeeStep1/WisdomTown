package com.bit.module.vol.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenduo
 * @create 2019-03-07 14:16
 */
@Data
public class CampaignExcel {

    /**
     * num
     */
    @Excel(name = "序号")
    private Integer num;
    /**
     * 活动主题
     */
    @Excel(name = "活动主题")
    private String campaignTheme;
    /**
     * 活动时间
     */
    @Excel(name = "活动时间")
    private String campaignTime;


    /**
     * 活动服务时长
     */
    @Excel(name = "服务总时长")
    private BigDecimal serviceHour;
    /**
     * 活动捐献金额
     */
    @Excel(name = "捐献总金额")
    private BigDecimal donateMoney;
    /**
     * 状态
     */
    @Excel(name = "状态")
    private String campaignStatus;
    /**
     * 活动类型
     */
    @Excel(name = "活动类型")
    private String campaignType;
    /**
     * 活动负责人
     */
    @Excel(name = "活动负责人")
    private String chargeMan;
    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String chargeManMobile;
    /**
     * 活动人数
     */
    @Excel(name = "活动人数")
    private Integer campaignNumber;
    /**
     * 活动地点
     */
    @Excel(name = "活动地点")
    private String campaignPlace;

    /**
     * 活动范围 0-站内 1-站外
     */
    @Excel(name = "活动发布范围")
    private String campaignScale;
    /**
     * 服务站名称
     */
    @Excel(name = "服务站名称")
    private String stationName;

}
