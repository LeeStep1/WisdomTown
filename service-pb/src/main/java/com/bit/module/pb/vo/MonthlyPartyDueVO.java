package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * MonthlyPartyDue
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MonthlyPartyDueVO extends BasePageVo{

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


