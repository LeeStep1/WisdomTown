package com.bit.module.cbo.bean;

import lombok.Data;

import java.util.Date;

/**
 * @Description 物业公告表
 * @Author chenduo
 * @Date 2019/8/5 9:45
 **/
@Data
public class PmcAnnouncement {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 发布方名称
	 */
	private String authorName;
	/**
	 * 社区id
	 */
	private Long orgId;
	/**
	 * 小区的id
	 */
	private Long communityId;
	/**
	 * 创建物业公告的物业人员的id
	 */
	private Long createPmcUserId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 发布时间
	 */
	private Date publishTime;
	/**
	 * 物业的发布人
	 */
	private Long publishPmcUserId;
	/**
	 * 状态：0 草稿 1 已发布
	 */
	private Integer status;
}
