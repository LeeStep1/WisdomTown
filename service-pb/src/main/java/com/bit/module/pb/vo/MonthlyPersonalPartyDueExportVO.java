package com.bit.module.pb.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/3 9:58
 */
@Data
@ExcelTarget("monthlyPersonalPartyDueExportVO")
public class MonthlyPersonalPartyDueExportVO implements Serializable {
    @Excel(name = "序号", width = 10, orderNum = "1")
    private Integer id;

    @Excel(name = "姓名", width = 13, orderNum = "2")
    private String memberName;

    @Excel(name = "所在党支部", width = 27, orderNum = "3")
    private String orgName;

    @Excel(name = "核算基数（元）", width = 12, orderNum = "4")
    private Double base;

    @Excel(name = "缴费金额（元）", width = 12, orderNum = "5")
    private Double amount;

    @Excel(name = "1月", width = 9, orderNum = "6")
    private Double january;

    @Excel(name = "2月", width = 9, orderNum = "7")
    private Double february;

    @Excel(name = "3月", width = 9, orderNum = "8")
    private Double march;

    @Excel(name = "4月", width = 9, orderNum = "9")
    private Double april;

    @Excel(name = "5月", width = 9, orderNum = "10")
    private Double may;

    @Excel(name = "6月", width = 9, orderNum = "11")
    private Double june;

    @Excel(name = "7月", width = 9, orderNum = "12")
    private Double july;

    @Excel(name = "8月", width = 9, orderNum = "13")
    private Double august;

    @Excel(name = "9月", width = 9, orderNum = "14")
    private Double september;

    @Excel(name = "10月", width = 9, orderNum = "15")
    private Double october;

    @Excel(name = "11月", width = 9, orderNum = "16")
    private Double november;

    @Excel(name = "12月", width = 9, orderNum = "17")
    private Double december;

    public void setMonthData(MonthlyPartyDueDetailVO partyDue) {
        switch (partyDue.getMonth()) {
            case 1 :
                // 核算基数和应交金额取一月份的值
                this.setJanuary(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                                ? (double) partyDue.getPaidAmount() / 100 : null);
                this.setBase(null != partyDue.getBase() && partyDue.getBase() > 0
                             ? (double) partyDue.getBase() / 100 : null);
                this.setAmount(null != partyDue.getAmount() && partyDue.getAmount() > 0
                               ? (double) partyDue.getAmount() / 100 : null);
                break;
            case 2 :
                this.setFebruary(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                                 ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
            case 3 :
                this.setMarch(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                              ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
            case 4 :
                this.setApril(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                              ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
            case 5 :
                this.setMay(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                            ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
            case 6 :
                this.setJune(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                             ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
            case 7 :
                this.setJuly(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                             ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
            case 8 :
                this.setAugust(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                               ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
            case 9 :
                this.setSeptember(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                                  ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
            case 10 :
                this.setOctober(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                                ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
            case 11 :
                this.setNovember(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                                 ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
            case 12 :
                this.setDecember(null != partyDue.getPaidAmount() && partyDue.getPaidAmount() > 0
                                 ? (double) partyDue.getPaidAmount() / 100 : null);
                break;
        }
    }
}
