package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/16 16:55
 **/
@Data
public class ResidentRelLocation {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 居民id
	 */
	private Long residentId;
	/**
	 * 社区id
	 */
	private Long orgId;
	/**
	 * 小区id
	 */
	private Long communityId;
	/**
	 * 房屋id
	 */
	private Long addressId;
	/**
	 * 1-业主 2-家属 3-租客
	 */
	private Integer identityType;
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
}
