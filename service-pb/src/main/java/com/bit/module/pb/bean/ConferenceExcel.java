package com.bit.module.pb.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-21 13:11
 */
@Data
public class ConferenceExcel {
    /**
     * 序号
     */
    @Excel(name = "序号")
    private Integer id;
    /**
     * 党支部name
     */
    @Excel(name = "党组织名称")
    private String name;

    /**
     * 会议主题
     */
    @Excel(name = "会议主题")
    private String theme;
    /**
     * 会议时间
     */
    @Excel(name = "会议时间")
    private String startTime;
    /**
     * 会议地点
     */
    @Excel(name = "会议地点")
    private String place;

    /**
     * 会议类型 1 - 党员大会 2- 支部委员会 3- 党小组会 4 -党课
     */
    @Excel(name = "会议类型")
    private String conferenceType;
    /**
     * 发布时间
     */
    @Excel(name = "发布时间",format="yyyy-MM-dd HH:mm:ss")
    private Date releaseTime;
    /**
     * 到课率
     */
    @Excel(name = "到课率")
    private String signRate;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;
}
