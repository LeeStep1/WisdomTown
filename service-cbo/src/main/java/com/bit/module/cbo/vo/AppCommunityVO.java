package com.bit.module.cbo.vo;

import lombok.Data;

/**
 * @Description app小区展示用
 * @Author chenduo
 * @Date 2019/7/18 16:47
 **/
@Data
public class AppCommunityVO {

	/**
	 * id
	 */
	private Long id;
	/**
	 * 小区名称
	 */
	private String communityName;
	/**
	 * 社区组织id
	 */
	private Long orgId;
	/**
	 * 社区组织名称
	 */
	private String orgName;

	/**
	 * 已验证 0-未验证 1-已验证
	 */
	private Integer status;
}
