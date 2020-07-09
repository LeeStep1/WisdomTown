package com.bit.module.vol.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuyancheng
 * @create 2019-03-28 14:30
 */
@Data
public class BenevolenceShopProductExcelVO {
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
    /**
     * 主键id字符串，前端传，格式例子：1,2,3
     */
    private String ids;
    /**
     * 主键id集合，用来做批量操作
     */
    private List<Long> idList;
    /**
     * sql排序字段
     */
    private String orderBy;
    /**
     * sql排序 asc desc
     */
    private String order;
}
