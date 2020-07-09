package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/6 16:24
 **/
@Data
public class ResidentSuggestionVO extends BasePageVo{
	/**
	 * id
	 */
	private Long id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 意见内容
	 */
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

	/**---参数----**/
	/**
	 * 提交时间 -开始
	 */
	private String beginCreateTime;
	/**
	 * 提交时间 -结束
	 */
	private String endCreateTime;
	/**
	 * 反馈时间 -开始
	 */
	private String beginFeedBackTime;
	/**
	 * 反馈时间 -结束
	 */
	private String endFeedBackTime;

	/**
	 * 社区id集合
	 */
	private List<Long> orgIds;
	/**
	 * 小区id集合
	 */
	private List<Long> communityIds;
}
