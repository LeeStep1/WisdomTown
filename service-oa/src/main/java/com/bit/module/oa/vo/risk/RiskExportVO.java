package com.bit.module.oa.vo.risk;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.bit.module.oa.enums.RiskStatusEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/17 9:56
 */
@Data
@ExcelTarget("riskExportVO")
public class RiskExportVO implements Serializable {
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Long id;
    /**
     * 单号
     */
    @Excel(name = "单号", width = 25, orderNum = "2")
    private String no;
    /**
     * 主题
     */
    @Excel(name = "主题", width = 15, orderNum = "3")
    private String name;
    /**
     * 上报人姓名
     */
    @Excel(name = "上报人姓名", width = 15, orderNum = "4")
    private String reporterName;
    /**
     * 上报部门名称
     */
    @Excel(name = "上报部门名称", width = 20, orderNum = "5")
    private String reportDepName;
    /**
     * 上报时间
     */
    @Excel(name = "上报时间", width = 15, orderNum = "6")
    private String reportTime;
    /**
     * 上报地点
     */
    @Excel(name = "上报地点", width = 15, orderNum = "7")
    private String reportLocation;
    /**
     * 状态（0：未反馈; 1：已反馈）
     */
    @Excel(name = "状态", width = 15, orderNum = "8")
    private String status;

    public String getStatus() {
        return RiskStatusEnum.getDescriptionByStringKey(status);
    }
}
