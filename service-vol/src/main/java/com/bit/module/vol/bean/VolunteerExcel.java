package com.bit.module.vol.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-14 13:43
 */
@Data
public class VolunteerExcel {
    /**
     * 序号
     */
    @Excel(name = "序号")
    private Integer num;
    /**
     * 服务站
     */
    @Excel(name = "服务站")
    private String stationName;
    /**
     * 编号
     */
    @Excel(name = "编号")
    private String serialNumber;
    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String realName;
    /**
     * 性别
     */
    @Excel(name = "性别")
    private String sex;
    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    private String cardId;
    /**
     * 手机号
     */
    @Excel(name = "手机号")
    private String mobile;
    /**
     * 活动时长
     */
    @Excel(name = "活动时长")
    private BigDecimal campaignHour;
    /**
     * 活动次数
     */
    @Excel(name = "活动次数")
    private Integer campaignCount;
    /**
     * 捐款金额
     */
    @Excel(name = "捐款金额")
    private BigDecimal donateMoney;
    /**
     * 积分
     */
    @Excel(name = "积分")
    private BigDecimal point;
    /**
     * 志愿等级
     */
    @Excel(name = "志愿等级")
    private Integer serviceLevel;
    /**
     * 民族
     */
    @Excel(name = "民族")
    private String people;
    /**
     * 教育程度
     */
    @Excel(name = "教育程度")
    private String education;
    /**
     * 出生日期
     */
    @Excel(name = "出生日期")
    private String birthday;
    /**
     * 健康状况
     */
    @Excel(name = "健康状况")
    private String health;
    /**
     * 志愿者经验
     */
    @Excel(name = "志愿者经验")
    private String experience;
    /**
     * 政治面貌
     */
    @Excel(name = "政治面貌")
    private String political;
    /**
     * 志愿服务类别
     */
    @Excel(name = "志愿服务类别")
    private String serviceType;
    /**
     * 可服务时间
     */
    @Excel(name = "可服务时间")
    private String serviceTime;
    /**
     * 特长爱好
     */
    @Excel(name = "特长爱好")
    private String hobby;
    /**
     * 籍贯
     */
    @Excel(name = "籍贯")
    private String nativePlace;
    /**
     * 居住地
     */
    @Excel(name = "居住地")
    private String livingPlace;
    /**
     * 工作学习单位
     */
    @Excel(name = "工作学习单位")
    private String company;

}
