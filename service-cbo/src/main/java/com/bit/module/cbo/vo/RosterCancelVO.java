package com.bit.module.cbo.vo;

import lombok.Data;

/**
 * @Description 服务名单注销使用
 * @Author chenduo
 * @Date 2019/9/20 14:12
 **/
@Data
public class RosterCancelVO {
	/**
	 * 服务名单类型id
	 */
	private Integer serviceId;
	/**
	 * 服务名单id
	 */
	private Long recordId;
	/**
	 * 注销备注
	 */
	private String cancellationRemarks;
	/**
	 * 附件id
	 */
	private String attachedIds;
}
