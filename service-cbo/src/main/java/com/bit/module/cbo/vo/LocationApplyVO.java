package com.bit.module.cbo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description 房屋认证申请展示用
 * @Author chenduo
 * @Date 2019/7/19 13:23
 **/
@Data
public class LocationApplyVO {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 居民id
	 */
	private Long residentId;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 证件类型 1 身份证 2士官证 3护照 4港澳通行证
	 */
	private Integer cardType;
	/**
	 * 证件号码
	 */
	private String cardNum;
	/**
	 * 申请房屋时用户所在此房屋的身份:1,业主2，家属 3，租客
	 */
	private Integer identityType;
	/**
	 * 房屋结构信息，xx小区，xx楼栋，xx单元，xx层
	 */
	private String addressStructure;
	/**
	 * 申请时间
	 */
	private Date createTime;
	/**
	 * web端显示的审批状态：0认证中、1已认证、2已拒绝
	 */
	private Integer applyStatus;

	/**
	 * app审核记录状态的显示0认证中、1已认证、2已拒绝,3失效
	 */
	private Integer enable;

	/**
	 * 申请房屋时用户所在此房屋的身份:1,业主2，家属 3，租客 字符串描述
	 */
	private String identityTypeStr;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 居民证件照ids
	 */
	private String credentialsPhotoIds;

	/**
	 * 证件照片 文件信息
	 */
	private List<FileInfo> credentialsPhotoFileInfos;
	/**
	 * 小区名称
	 */
	private String communityName;
	/**
	 * 版本号 锁
	 */
	private Integer version;
	/**
	 * 拒绝原因
	 */
	private String comment;
	/**
	 * 真实姓名
	 */
	private String residentName;

	/**
	 * 社区id
	 */
	private Long orgId;
	/**
	 * 社区名称
	 */
	private String orgName;


	/**
	 * 小区id
	 */
	private Long communityId;

	/**
	 * 房屋id
	 */
	private Long addressId;


	/**
	 * 审核时间
	 */
	private Date updateTime;
	/**
	 * 审核人ID
	 */
	private Long updateUserId;

	/**
	 * 申请记录来源类型 0 - web 1-app
	 */
	private Integer applyType;
}
