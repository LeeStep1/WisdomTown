package com.bit.module.vol.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * 爱心商家导出模板
 * @author liuyancheng
 * @create 2019-03-26 11:00
 */
@Data
@ExcelTarget("BenevolenceShopTemplate")
public class BenevolenceShopTemplate {

    /**
     * 主键id
     */
    @Excel(name = "序号",orderNum = "0")
    private Long id;
    /**
     * 爱心商家名称
     */
    @Excel(name = "爱心商家名称",orderNum = "1")
    private String name;
    /**
     * 爱心商家地址
     */
    @Excel(name = "爱心商家地址", orderNum = "8")
    private String address;
    /**
     * 爱心商家联系人
     */
    @Excel(name = "联系人",orderNum = "2")
    private String contacts;
    /**
     * 爱心商家联系电话
     */
    @Excel(name = "手机号",orderNum = "3")
    private String contactsMobile;
    /**
     * 兑换总积分
     */
    @Excel(name = "兑换总积分",orderNum = "5")
    private Integer exchangeIntegralAmount;
    /**
     * 商品种类总数
     */
    @Excel(name = "商品种类",orderNum = "4")
    private Integer merchandizeTypes;
    /**
     * 折扣(字典)
     */
    @Excel(name = "折扣",orderNum = "6")
    private String discount;
    /**
     * 状态 0-停用 1-启用
     */
    @Excel(name = "状态",replace = {"停用_0", "正常_1"}, orderNum = "7")
    private Integer enable;
}
