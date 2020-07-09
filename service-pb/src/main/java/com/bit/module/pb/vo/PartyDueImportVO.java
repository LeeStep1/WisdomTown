package com.bit.module.pb.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/4 10:24
 */
@Data
public class PartyDueImportVO implements Serializable {
    @Excel(name = "序号")
    private Integer id;

    @Excel(name = "姓名")
    private String memberName;

    @Excel(name = "身份证号")
    private String idCard;

    @Excel(name = "所属党支部")
    private String orgName;

    @Excel(name = "核算基数")
    private Double base;

    @Excel(name = "应交金额")
    private Double amount;

    @Excel(name = "实交金额")
    private Double paidAmount;

    @Excel(name = "备注")
    private String remark;

}
