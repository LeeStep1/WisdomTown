package com.bit.module.pb.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * MonthlyPartyDue
 * @author generator
 */
@Data
public class MonthlyPartyDue implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 组织id
     */	
	private String orgId;
    /**
     * 年份
     */	
	private Integer year;
    /**
     * 月份
     */	
	private Integer month;
    /**
     * 合计金额，单位分
     */	
	private Integer amount;
    /**
     * 插入时间
     */	
	private Date insertTime;

	//columns END

}


