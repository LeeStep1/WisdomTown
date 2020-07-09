package com.bit.module.vol.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.util.Date;

/**
 * 爱心商家商品导出模板
 * @author liuyancheng
 * @create 2019-03-27 8:38
 */
@Data
@ExcelTarget("BenevolenceShopProductTemplate")
public class BenevolenceShopProductTemplate {
    /**
     * id
     */
    @Excel(name = "序号",orderNum = "0")
    private Long id;
    /**
     * 商品名称
     */
    @Excel(name = "商品名称",orderNum = "1")
    private String productName;
    /**
     * 商品介绍
     */
    @Excel(name = "商品简介",orderNum = "6")
    private String productIntroduction;
    /**
     * 积分值
     */
    @Excel(name = "积分值",orderNum = "3")
    private Integer integralValue;
    /**
     * 商品状态:0:下架，1上架
     */
    @Excel(name = "状态",replace = {"下架_0", "上架_1"}, orderNum = "5")
    private Integer productState;
    /**
     * 兑换的总数数量
     */
    @Excel(name = "兑换数量",orderNum = "4")
    private Integer exchangeNum;
    /**
     * 爱心商家名称
     */
    @Excel(name = "所属爱心商家",orderNum = "2")
    private String shopName;
}
