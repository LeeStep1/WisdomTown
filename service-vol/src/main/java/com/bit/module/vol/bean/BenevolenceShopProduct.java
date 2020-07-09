package com.bit.module.vol.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * BenevolenceShopProduct
 * @author generator
 */
@Data
public class BenevolenceShopProduct {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
    /**
     * createUserId
     */	
	private Long createUserId;
    /**
     * updateTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
    /**
     * 临时字段--图片地址
     */
	private String imgPath;
	//columns END

}


