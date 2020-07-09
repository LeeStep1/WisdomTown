package com.bit.module.vol.vo;

import java.util.Date;
import java.util.List;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * BenevolenceShopProduct
 * @author liuyancheng
 */
@Data
public class BenevolenceShopProductVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 爱心商家id
     */	
	private Long shopId;
    /**
     * 商品名称
     */	
	private String productName;
    /**
     * 商品介绍
     */	
	private String productIntroduction;
    /**
     * 积分值
     */	
	private Integer integralValue;
    /**
     * 兑换说明
     */	
	private String exchangeExplain;
    /**
     * 商品的照片
     */	
	private Long productImgId;
    /**
     * 创建时间
     */	
	private Date createTime;
    /**
     * createUserId
     */	
	private Long createUserId;
    /**
     * updateTime
     */	
	private Date updateTime;
    /**
     * updateUserId
     */	
	private Long updateUserId;
    /**
     * 商品状态:0:下架，1上架
     */	
	private Integer productState;
    /**
     * 兑换的总数数量
     */	
	private Integer exchangeNum;
    /**
     * 爱心商家名称
     */
    private String shopName;

    private List<Long> ids;
	//columns END

}


