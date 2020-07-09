package com.bit.module.oa.vo.company;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/17 9:56
 */
@Data
@ExcelTarget("companyExportVO")
public class CompanyExportVO implements Serializable {
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Long id;
    /**
     * 名称
     */
    @Excel(name = "企业名称", width = 15, orderNum = "2")
    private String name;
    /**
     * 行业
     */
    @Excel(name = "所属类型", width = 15, orderNum = "3")
    private String industry;
    /**
     * 类型
     */
    @Excel(name = "企业类型", width = 15, orderNum = "4")
    private String type;
    /**
     * 统一社会信用代码
     */
    @Excel(name = "统一社会信用代码", width = 20, orderNum = "5")
    private String uniformSocialCreditCode;
    /**
     * 法定代表人
     */
    @Excel(name = "法定代表人", width = 15, orderNum = "6")
    private String legalRepresentative;
    /**
     * 登记机关
     */
    @Excel(name = "登记机关", width = 15, orderNum = "7")
    private String registerOffice;
    /**
     * 经营状态 1开业 2存续 3停业 4清算
     */
    @Excel(name = "经营状态", width = 15, orderNum = "8")
    private String condition;
    /**
     * 服务性质
     */
    @Excel(name = "服务性质", width = 15, orderNum = "9")
    private String nature;
    /**
     * 服务时间
     */
    @Excel(name = "服务时间", width = 15, orderNum = "10")
    private String serviceTime;
    /**
     * 地址
     */
    @Excel(name = "企业地址", width = 15, orderNum = "11")
    private String address;
    /**
     * 联系人
     */
    @Excel(name = "企业联系人", width = 15, orderNum = "12")
    private String contract;
    /**
     * 联系人电话
     */
    @Excel(name = "联系人电话", width = 15, orderNum = "13")
    private String contractPhone;
    /**
     * 经办人
     */
    @Excel(name = "政府经办人", width = 15, orderNum = "14")
    private String operatorName;
    /**
     * 经办人电话
     */
    @Excel(name = "经办人电话", width = 15, orderNum = "15")
    private String operatorMobile;
}
