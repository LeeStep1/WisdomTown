package com.bit.module.cbo.bean;

import lombok.Data;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/17 17:02
 **/
@Data
public class PmcStaffRelCommunity {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 物业员工id
	 */
	private Long staffId;

	/**
	 * 社区ID
	 */
	private Long orgId;
	/**
	 * 小区ID
	 */
	private Long communityId;
}
