package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/7 14:36
 **/
@Data
public class AnnouncementPageVO extends BasePageVo {
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


	/**
	 * 开始时间
	 */
	private String beginTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 是否社区办 0-不是 1-是
	 */
	private Integer flag;
	/**
	 * 社区id集合
	 */
	private List<Long> orgIds;
}
