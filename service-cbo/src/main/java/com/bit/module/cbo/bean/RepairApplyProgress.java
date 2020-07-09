package com.bit.module.cbo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class RepairApplyProgress implements Serializable {

    private static final long serialVersionUID = -8022788181770910073L;
	/**
	 * id
	 */
	private Long id;
	/**
	 * 维修申请表的id
	 */
    private Long repairApplyId;
	/**
	 * 操作人类型 1社区 2.物业 3居民 0社区办
	 */
    private Integer userType;
	/**
	 * 操作人ID
	 */
    private Long userId;
	/**
	 * 操作人名称
	 */
    private String userName;
	/**
	 * 操作类型 1-居民提交 2-居民取消报修 3-物业受理 4-物业移交居委会  5-物业拒绝 6-物业完结 7-居委会受理 8-居委会退回物业 9-居委会拒绝 10-居委会完结
	 */
    private Integer operationType;
	/**
	 * 操作名称
	 */
    private String operationName;
	/**
	 * 数据操作时间
	 */
    private Date operationTime;
	/**
	 * 原因  退回、拒绝、移交操作时填写
	 */
    private String reason;

}