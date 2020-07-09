package com.bit.module.vol.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.util.Date;

/**
 * @author liuyancheng
 * @create 2019-03-27 8:51
 */
@Data
@ExcelTarget("ProductExchangeAuditTemplate")
public class ProductExchangeAuditTemplate {
    /**
     * 主键id
     */
    @Excel(name = "序号",orderNum = "0")
    private Long id;
    /**
     * 商家名称
     */
    @Excel(name = "所属商家",orderNum = "2")
    private String shopName;
    /**
     * 商品名称
     */
    @Excel(name = "商品名称",orderNum = "1")
    private String productName;
    /**
     * 申请人
     */
    @Excel(name = "申请人",orderNum = "3")
    private String proposerName;
    /**
     * 申请人电话
     */
    @Excel(name = "手机号",orderNum = "4")
    private String proposerMobile;
    /**
     * 商品积分值（单价）
     */
    @Excel(name = "积分值",orderNum = "5")
    private Integer integralValue;
    /**
     * 兑换数量
     */
    @Excel(name = "兑换数量",orderNum = "6")
    private Integer exchangeNumber;
    /**
     * 兑换总积分
     */
    @Excel(name = "兑换总积分",orderNum = "7")
    private Integer exchangeIntegralAmount;
    /**
     * 创建时间
     */
    @Excel(name = "兑换申请时间",format="yyyy-MM-dd HH:mm:ss",orderNum = "8")
    private Date createTime;
    /**
     * 领取状态:1领取，0未领取
     */
    @Excel(name = "领取状态",replace = {"未领取_0", "已领取_1"}, orderNum = "9")
    private Integer getStatus;
}
