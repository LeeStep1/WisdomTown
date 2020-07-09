package com.bit.module.cbo.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @Description 社区公告表
 * @Author chenduo
 * @Date 2019/8/5 9:22
 **/
@Data
public class Announcement {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 标题
	 */
	@NotEmpty(message = "标题不能为空")
	@Length(max = 60,message = "标题长度不能超过30")
	private String title;
	/**
	 * 内容
	 */
	@NotEmpty(message = "内容不能为空")
//	@Size(max = 2000,message = "内容长度不能超过2000")
	private String content;
	/**
	 * 发布方名称
	 */
	@NotEmpty(message = "发布方不能为空")
	@Length(max = 40,message = "发布方长度不能超过20")
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
}
