package com.bit.module.cbo.bean;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/6 15:11
 **/
@Data
public class ResidentSuggestion {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 标题
	 */
	@NotEmpty(message = "标题不能为空")
	private String title;
	/**
	 * 意见内容
	 */
	@NotEmpty(message = "意见内容不能为空")
	private String content;
	/**
	 * 是否已回复,1回复，0未回复
	 */
	private Integer status;
	/**
	 * 组织id
	 */
	private Long orgId;
	/**
	 * 组织名称
	 */
	private String orgName;
	/**
	 * 小区id
	 */
	private Long communityId;
	/**
	 * 小区名称
	 */
	private String communityName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建的居民id
	 */
	private Long createResidentId;
	/**
	 * 创建的居民名称
	 */
	private String createResidentName;
	/**
	 * 居民联系电话
	 */
	private String residentMobile;
	/**
	 * 反馈信息
	 */
	private String feedBackMsg;
	/**
	 * 回馈的社区工作人员id
	 */
	private Long feedBackUserId;
	/**
	 * 反馈时间
	 */
	private Date feedBackTime;
	/**
	 * 锁
	 */
	private Integer version;
}
