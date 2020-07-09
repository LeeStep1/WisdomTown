package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author Liyang
 * @Date 2019/7/19 11:34
 **/
@Data
public class CommunityVO extends BasePageVo{
	/**
	 * id
	 */
	private Long id;
	/**
	 * 小区名称
	 */
	private String communityName;
	/**
	 * 所属的社区组织id
	 */
	private Long orgId;
	/**
	 * 物业公司id
	 */
	private Long pmcCompanyId;
	/**
	 * 房屋户型图资料的ids,逗号分隔
	 */
	private String attachmentIds;

	/**
	 * 创建者ID
	 */
	private Long createUserId;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	/**
	 * 更新者ID
	 */
	private Long updateUserId;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date updateTime;
	/**
	 * 社区组织名称
	 */
	private String orgName;
	/**
	 * 房屋id
	 */
	private Long addressId;
	/**
	 * 社区房屋是否验证 0-未验证 1-已验证
	 */
	private Integer status;
}
