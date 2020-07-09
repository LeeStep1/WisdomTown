package com.bit.module.vol.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * ProductExchangeAudit
 * @author liuyancheng
 */
@Data
public class ProductExchangeAudit {

	//columns START

    /**
     * 主键id
     */	
	private Long id;
    /**
     * 爱心商家id
     */	
	private Long shopId;
    /**
     * 商家名称
     */	
	private String shopName;
    /**
     * 商品id
     */	
	private Long productId;
    /**
     * 商品名称
     */	
	private String productName;
    /**
     * 申请人id
     */	
	private Long proposerId;
    /**
     * 申请人
     */	
	private String proposerName;
    /**
     * 申请人电话
     */	
	private String proposerMobile;
    /**
     * 商品积分值（单价）
     */	
	private Integer integralValue;
    /**
     * 兑换数量
     */	
	private Integer exchangeNumber;
    /**
     * 兑换总积分
     */	
	private Integer exchangeIntegralAmount;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date updateTime;
    /**
     * 更新人id
     */	
	private Long updateUserId;
    /**
     * 兑换状态：0待审核，1已通过，2：已退回
     */	
	private Integer auditStatus;
    /**
     * 领取状态:1领取，0未领取
     */	
	private Integer getStatus;
    /**
     * 退回原因
     */
	private String returnReason;
    /**
     * 图片地址
     */
	private String imgPath;
    /**
     * 商品简介
     */
	private String productIntroduction;
    /**
     * 锁
     */
    private Integer version;
	//columns END

}


