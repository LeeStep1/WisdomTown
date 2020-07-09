package com.bit.module.cbo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/5 9:25
 **/
@Data
public class AnnouncementVO {
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
	 * 封面图片的id
	 */
	private Long pic;
	/**
	 * 社区ID
	 */
	private Long orgId;
	/**
	 * 创建人ID
	 */
	private Long createUserId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 发布时间
	 */
	private Date publishTime;
	/**
	 * 发布人员id
	 */
	private Long publishUserId;
	/**
	 * 状态：1 已发布，0 草稿
	 */
	private Integer status;
	/**
	 * 社区名称
	 */
	private String orgName;
	/**
	 * 公告封面
	 */
	private FileInfo picFileInfo;
}

