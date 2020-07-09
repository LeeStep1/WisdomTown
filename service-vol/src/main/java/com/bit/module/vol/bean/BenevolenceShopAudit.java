package com.bit.module.vol.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * BenevolenceShopAudit
 * @author generator
 */
@Data
public class BenevolenceShopAudit {

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


