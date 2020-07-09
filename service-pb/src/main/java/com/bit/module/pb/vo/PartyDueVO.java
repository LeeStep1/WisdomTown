package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * PartyDue
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PartyDueVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 年份
     */
	private Integer year;
    /**
     * 月份
     */
	private Integer month;
    /**
     * 党员id
     */
	private Integer memberId;
    /**
     * 党员身份证
     */
    private String idCard;
    /**
     * 党员姓名
     */
    private String memberName;
    /**
     * 党员所在组织id
     */	
	private String orgId;
    /**
     * 党员所在组织名称
     */	
	private String orgName;
    /**
     * 核准基数，单位为分
     */	
	private Integer base;
    /**
     * 应交金额，单位为分
     */	
	private Integer amount;
    /**
     * 实交金额，单位为分
     */	
	private Integer paidAmount;
    /**
     * 备注
     */	
	private String remark;
    /**
     * 插入时间
     */	
	private Date insertTime;

	//columns END

}


