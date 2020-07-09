package com.bit.module.pb.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * PartyDue
 * @author generator
 */
@Data
public class PartyDue implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 年份
     */
    @NotNull(message = "年份不能为空", groups = ExportPartyDue.class)
	private Integer year;
    /**
     * 月份
     */
    @NotNull(message = "月份不能为空", groups = ExportPartyDue.class)
	private Integer month;
    /**
     * 党员id
     */	
	private Integer memberId;
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

    public interface ExportPartyDue {}
}


