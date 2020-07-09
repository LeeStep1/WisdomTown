package com.bit.module.vol.vo;

import java.util.Date;
import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * BenevolenceShopAudit
 * @author liuyancheng
 */
@Data
public class BenevolenceShopAuditVO extends BasePageVo{

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
     * 审核状态：0待审核，1已通过，2：已退回
     */	
	private Integer auditState;
    /**
     * 经营内容
     */	
	private String operationScope;
    /**
     * 锁
     */
    private Integer version;

	//columns END

}


