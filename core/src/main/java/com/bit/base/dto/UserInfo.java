package com.bit.base.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description:当前用户信息
 * @Author: mifei
 * @Date: 2018-10-18
 **/
@Data
public class UserInfo<T> {

	/**
	 * 用户名称
	 */
	private String username;
	/**
	 * 用户真实名
	 */
	private String realName;
	/**
	 * 令牌
	 */
	private String token;

	/**
	 * 用户的id
	 */
	private Long id;

	/**
	 * 所属应用id
	 */
	private List<Integer> appIds;
	/**
	 * 角色ids
	 */
	private List<Long> roleIds;
	/**
	 * 志愿者信息
	 */
	private VolunteerInfo volunteerInfo;

	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 身份证号
	 */
	private String idcard;

	/**
	 * 党建组织机构id
	 */
	private String pbOrgId;
	/**
	 * 接入端id
	 */
	private Integer tid;

	/**
	 * 身份列表
	 */
	private List<T> identitys;
	/**
	 * 当前身份
	 */
	private Long currentIdentity;
	/**
	 * 党组织机构名称
	 */
	private String pbOrgName;

	/**
	 * 社区应用使用
	 */
	private CboInfo cboInfo;
	/**
	 * 安检
	 */
	private Security security;
	/**
	 * 环保
	 */
	private Environment environment;
}
