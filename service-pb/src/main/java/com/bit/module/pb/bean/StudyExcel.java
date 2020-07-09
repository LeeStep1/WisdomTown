package com.bit.module.pb.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-21 15:21
 */
@Data
public class StudyExcel {
    /**
     * 序号
     */
    @Excel(name = "序号")
    private Integer id;
    /**
     * 会议主题
     */
    @Excel(name = "会议主题")
    private String theme;
    /**
     * 党支部name
     */
    @Excel(name = "党组织名称")
    private String pborgName;
    /**
     * 会议内容
     */
    @Excel(name = "会议内容")
    private String content;
    /**
     * 发布时间
     */
    @Excel(name = "发布时间",format="yyyy-MM-dd HH:mm:ss")
    private Date releaseTime;
    /**
     * 发布状态  0-草稿  1-已发布
     */
    @Excel(name = "状态")
    private String isRelease;
}
