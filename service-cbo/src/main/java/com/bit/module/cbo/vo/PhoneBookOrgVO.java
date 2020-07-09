package com.bit.module.cbo.vo;

import lombok.Data;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/7 14:21
 **/
@Data
public class PhoneBookOrgVO {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 社区id
	 */
	private Long orgId;
	/**
	 * 类型：1社区，2物业
	 */
	private Integer type;
	/**
	 * 小区id
	 */
	private Long communityId;
	/**
	 * 电话1
	 */
	private String telOne;
	/**
	 * 电话2
	 */
	private String telTwo;
	/**
	 * 社区名称
	 */
	private String orgName;
}
