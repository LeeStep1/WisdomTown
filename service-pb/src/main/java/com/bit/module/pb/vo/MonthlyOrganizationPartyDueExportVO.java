package com.bit.module.pb.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.bit.module.pb.bean.MonthlyPartyDue;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/3 14:19
 */
@Data
@ExcelTarget("monthlyOrganizationPartyDueExportVO")
public class MonthlyOrganizationPartyDueExportVO implements Serializable {
    @Excel(name = "序号", width = 20, orderNum = "1")
    private Integer id;

    @Excel(name = "党组织", width = 27, orderNum = "2")
    private String orgName;

    @Excel(name = "一月", width = 7, orderNum = "3")
    private Double january;

    @Excel(name = "二月", width = 7, orderNum = "4")
    private Double february;

    @Excel(name = "三月", width = 7, orderNum = "5")
    private Double march;

    @Excel(name = "四月", width = 7, orderNum = "6")
    private Double april;

    @Excel(name = "五月", width = 7, orderNum = "7")
    private Double may;

    @Excel(name = "六月", width = 7, orderNum = "8")
    private Double june;

    @Excel(name = "七月", width = 7, orderNum = "9")
    private Double july;

    @Excel(name = "八月", width = 7, orderNum = "10")
    private Double august;

    @Excel(name = "九月", width = 7, orderNum = "11")
    private Double september;

    @Excel(name = "十月", width = 7, orderNum = "12")
    private Double october;

    @Excel(name = "十一月", width = 7, orderNum = "13")
    private Double november;

    @Excel(name = "十二月", width = 7, orderNum = "14")
    private Double december;

    public void setMonthData(MonthlyPartyDue monthlyPartyDue) {
        switch (monthlyPartyDue.getMonth()) {
            case 1 :
                this.setJanuary(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                                ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 2 :
                this.setFebruary(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                                 ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 3 :
                this.setMarch(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                              ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 4 :
                this.setApril(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                              ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 5 :
                this.setMay(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                            ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 6 :
                this.setJune(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                             ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 7 :
                this.setJuly(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                             ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 8 :
                this.setAugust(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                               ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 9 :
                this.setSeptember(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                                  ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 10 :
                this.setOctober(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                                ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 11 :
                this.setNovember(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                                 ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
            case 12 :
                this.setDecember(null != monthlyPartyDue.getAmount() && monthlyPartyDue.getAmount() > 0
                                 ? (double) monthlyPartyDue.getAmount() / 100 : null);
                break;
        }
    }
}
