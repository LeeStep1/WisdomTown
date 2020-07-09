package com.bit.module.cbo.vo;

import lombok.Data;

/**
 * @Description
 * @Author liyang
 * @Date 2019/7/22 17:02
 **/
@Data
public class PmcStaffRelCommunityVO {
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
	 * 社区名称
	 */
	private String orgName;

	/**
	 * 小区ID
	 */
	private Long communityId;

	/**
	 * 小区ID集合String
	 */
	private String communityIds;

	/**
	 * 小区名称
	 */
	private String communityName;


}
