package com.bit.module.vol.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuyancheng
 * @create 2019-03-28 14:22
 */
@Data
public class BenevolenceShopExcelVO {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 爱心商家名称
     */
    private String name;
    /**
     * 爱心商家地址
     */
    private String address;
    /**
     * 爱心商家联系人
     */
    private String contacts;
    /**
     * 爱心商家联系电话
     */
    private String contactsMobile;
    /**
     * 兑换总积分
     */
    private Integer exchangeIntegralAmount;
    /**
     * 商品种类总数
     */
    private Integer merchandizeTypes;
    /**
     * 折扣(字典)
     */
    private String discount;
    /**
     * 状态 0-停用 1-启用
     */
    private Integer enable;
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
     * 经营内容
     */
    private String operationScope;
    /**
     * 审核状态：0待审核，1已通过，2：已退回
     */
    private Integer auditState;
    /**
     * 主键id集合，用来做批量操作
     */
    private List<Long> idList;
    /**
     * 主键id字符串，前端传，格式例子：1,2,3
     */
    private String ids;
    /**
     * sql排序字段
     */
    private String orderBy;
    /**
     * sql排序 asc desc
     */
    private String order;
}
