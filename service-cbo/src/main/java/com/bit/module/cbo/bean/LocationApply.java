package com.bit.module.cbo.bean;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/18 8:45
 **/
@Data
public class LocationApply {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 居民申请的id
	 */
	private Long residentId;
	/**
	 * 小区id
	 */
	private Long communityId;
	/**
	 * 小区名称
	 */
	private String communityName;
	/**
	 * 社区id
	 */
	private Long orgId;
	/**
	 * 社区名称
	 */
	private String orgName;
	/**
	 * 房屋id
	 */
	private Long addressId;
	/**
	 * 房屋结构信息，xx小区，xx楼栋，xx单元，xx层
	 */
	private String addressStructure;
	/**
	 * 申请时间
	 */
	private Date createTime;
	/**
	 * 审核时间
	 */
	private Date updateTime;
	/**
	 * 审核人ID
	 */
	private Long updateUserId;
	/**
	 * 申请房屋时用户所在此房屋的身份:1,业主2，家属 3，租客
	 */
	private Integer identityType;
	/**
	 * web端显示的审批状态：0认证中、1已认证、2已拒绝
	 */
	private Integer applyStatus;
	/**
	 * app审核记录状态的显示0认证中、1已认证、2已拒绝,3失效
	 */
	private Integer enable;
	/**
	 * 数据锁
	 */
	private Integer version;
	/**
	 * 拒绝的理由
	 */
	private String comment;
	/**
	 * 证件类型 1 身份证 2士官证 3护照 4港澳通行证
	 */
	private Integer cardType;
	/**
	 * 证件号码
	 */
	private String cardNum;
	/**
	 * 证件照片，逗号分隔
	 */
	private String credentialsPhotoIds;
	/**
	 * 居民姓名
	 */
	private String residentName;
	/**
	 * 申请记录来源类型 0 - web 1-app
	 */
	private Integer applyType;
}
