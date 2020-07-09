package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/8 16:15
 **/
@Data
public class NoticePageVO extends BasePageVo {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 社区ID
	 */
	private Long orgId;
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
	 * 封面图片的id
	 */
	private Long pic;
	/**
	 * 状态：1 已发布，0 草稿
	 */
	private Integer status;
	/**
	 * 发布方名称
	 */
	@NotEmpty(message = "发布方不能为空")
	@Length(max = 40,message = "发布方长度不能超过20")
	private String authorName;

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
	 * 发布的人群类型码，对应字典表的扩展信息
	 */
	private String label;
	/**
	 * 目标阅读总数
	 */
	private Integer totalNum;
	/**
	 * 已阅读人数
	 */
	private Integer readNum;

//	参数
	/**
	 * 社区id集合
	 */
	private List<Long> orgIds;
	/**
	 * 开始时间
	 */
	private String beginTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 是否是社区办 0-不是 1-是
	 */
	private Integer flag;
	/**
	 * 居民id
	 */
	private Long residentId;
	/**
	 * 通知id
	 */
	private Long noticeId;
	/**
	 * 是否已读  0 - 未读   1 - 已读
	 */
	private Integer readStatus;
	/**
	 * 姓名
	 */
	private String realName;
	/**
	 * 接收状态 0-全部 1-部分人接收 2-所有人接收  指的是通知有多少人全部接收的概念
	 */
	private Integer receiveStatus;

	/**
	 * app居委会端 时间范围 	0-全部 1- 3天内 2- 一周内 3- 一个月内 4- 三个月内
	 */
	private Integer timeLimit;
}
