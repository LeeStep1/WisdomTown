package com.bit.module.cbo.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Description 通讯录
 * @Author chenduo
 * @Date 2019/8/5 14:16
 **/
@Data
public class PhoneBook {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 社区组织id
	 */
	@NotNull(message = "社区id不能为空")
	private Long orgId;
	/**
	 * 小区id
	 */
	@NotNull(message = "小区id不能为空")
	private Long communityId;
	/**
	 * 电话1
	 */
	@NotBlank(message = "电话不能为空")
	private String telOne;
	/**
	 * 电话2
	 */
	@NotBlank(message = "电话不能为空")
	private String telTwo;
	/**
	 * 类型：1社区，2物业
	 */
	@NotNull(message = "类型不能为空")
	private Integer type;
}
