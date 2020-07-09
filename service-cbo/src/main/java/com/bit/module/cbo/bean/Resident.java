package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description 居民 物业员工 表内字段
 * @author chenduo
 */
@Data
public class Resident {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 曾用名
	 */
	private String usedName;
	/**
	 * 密码
	 */
	private String password;

	/**
	 * 小区ID
	 */
	private Long communityId;
	/**
	 * 盐
	 */
	private String salt;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 证件类型 1 身份证 2士官证 3护照 4港澳通行证
	 */
	private Integer cardType;
	/**
	 * 证件号码
	 */
	private String cardNum;

	private String type;
	/**
	 * 性别1男 2女
	 */
	private Integer sex;
	/**
	 * 生日yyyy-MM-dd
	 */
	private String birthday;
	/**
	 * 证件照片，逗号分隔
	 */
	private String credentialsPhotoIds;
	/**
	 * 头像id
	 */
	private Long icon;
	/**
	 * 1:居民 2：游客(当房屋认证通过后，将此值改变为1)
	 */
	private Integer role;
	/**
	 * 政治面貌：1群众、2党员、3团员、4民主党派、5其他
	 */
	private Integer faith;
	/**
	 * 婚姻状况:1未婚、2已婚、3离婚、4丧偶
	 */
	private Integer maritalStatus;
	/**
	 * 国籍
	 */
	private Integer nationality;
	/**
	 * 户籍类型 1-本市户籍 2-非本市户籍
	 */
	private Integer residentType;
	/**
	 * 账号的创建来源：1 web 端 2 app注册
	 */
	private Integer createType;
	/**
	 * 民族
	 */
	private Integer ethnicity;
	/**
	 * 工作单位
	 */
	private String serviceUnit;
	/**
	 * 账号状态 用户状态 状态 0停用 1正常 2待完善(只有web后台在居民管理中编辑补充后变为正常，不然为待完善,与有无关联房屋无关)
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	/**
	 * 创建者的ID,仅居委会创建时保存此字段
	 */
	private Long createUserId;
	/**
	 * 登录token
	 */
	private String token;
	/**
	 * 户籍地址
	 */
	private String residentAddress;

	/**
	 * 社区ID
	 */
	private Long orgId;

	/**
	 * 社区办标识 0-否 1-是
	 */
	private Integer flag;

	/**
	 * 身份 1,业主2，家属 3，租客
	 */
	private Integer identityType;
}
