package com.bit.module.vol.vo;

import java.util.Date;
import java.util.List;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * ProductExchangeAudit
 * @author liuyancheng
 */
@Data
public class ProductExchangeAuditVO extends BasePageVo{

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
	private Date createTime;
    /**
     * 更新时间
     */	
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
     * id集合-用于批量操作
     */
	private List<Long> ids;
    /**
     * 锁
     */
    private Integer version;
	//columns END

}


