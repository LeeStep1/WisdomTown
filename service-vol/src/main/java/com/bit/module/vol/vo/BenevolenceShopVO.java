package com.bit.module.vol.vo;

import java.util.Date;
import java.util.List;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * BenevolenceShop
 * @author liuyancheng
 */
@Data
public class BenevolenceShopVO extends BasePageVo{

	//columns START

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
     * 主键id集合
     */
    private List<Long> ids;
    /**
     * 图片文件id
     */
    private Long imgId;
    /**
     * 锁
     */
    private Integer version;
	//columns END

}


