package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/5 11:53
 **/
@Data
public class PmcAnnouncementPageVO extends BasePageVo{
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
	/**
	 * 是不是社区办的表示 0-不是 1-是
	 */
	private Integer flag;
	/**
	 * 社区id集合
	 */
	private List<Long> orgIds;
	/**
	 * 小区id集合
	 */
	private List<Long> communityIds;
	/**
	 * 开始时间
	 */
	private String beginTime;
	/**
	 * 结束时间
	 */
	private String endTime;
}
