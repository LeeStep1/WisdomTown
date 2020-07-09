package com.bit.module.cbo.vo;

import lombok.Data;

/**
 * @Description 社区基本信息维护查询展示使用
 * @Author chenduo
 * @Date 2019/8/6 8:43
 **/
@Data
public class PhoneBookCommunityCompanyVO {
	/**
	 * 通讯录id
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
	 * 小区名称
	 */
	private String CommunityName;
	/**
	 * 物业公司名称
	 */
	private String pmcCompanyName;
	/**
	 * 社区名称
	 */
	private String orgName;
	/**
	 * 社区id web查询使用
	 */
	private Long comId;
}
