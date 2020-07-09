package com.bit.module.pb.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/2 15:55
 */
@Data
@ExcelTarget("personalPartyDueExportVO")
public class PersonalPartyDueExportVO implements Serializable {
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Integer id;

    @Excel(name = "姓名", width = 15, orderNum = "2")
    private String memberName;

    @Excel(name = "身份证号", width = 20, orderNum = "3")
    private String idCard;

    @Excel(name = "所属党支部", width = 27, orderNum = "4")
    private String orgName;

    private Integer base;

    @Excel(name = "核算基数", width = 15, orderNum = "5")
    private Double exportBase;

    private Integer amount;

    @Excel(name = "应交金额", width = 15, orderNum = "6")
    private Double exportAmount;

    private Integer paidAmount;

    @Excel(name = "实交金额", width = 15, orderNum = "7")
    private Double exportPaidAmount;

    @Excel(name = "备注", width = 15, orderNum = "8")
    private String remark;

    public void determineExportParam() {
        if (null != this.getAmount() && this.getAmount() != 0) {
            this.setExportAmount((double) this.getAmount() / 100);
        }

        if (null != this.getPaidAmount() && this.getPaidAmount() != 0) {
            this.setExportPaidAmount((double) this.getPaidAmount() / 100);
        }

        if (null != this.getBase() && this.getBase() != 0) {
            this.setExportBase((double) this.getBase() / 100);
        }
    }
}
