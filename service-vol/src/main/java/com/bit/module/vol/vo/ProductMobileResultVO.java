package com.bit.module.vol.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author liuyancheng
 * @create 2019-03-27 13:28
 */
@Data
public class ProductMobileResultVO {

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
    private String productImgPath;
    /**
     * 爱心商家名称
     */
    private String shopName;
    /**
     * 商家地址
     */
    private String shopAddress;

}
