package com.bit.module.cbo.vo;

import com.bit.module.cbo.bean.ResidentExtend;
import com.bit.module.cbo.bean.ResidentRelLocation;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description 居民  传递参数使用
 * @author chenduo
 */
@Data
public class ResidentVO {

	//----居民信息-----

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
	 * 验证码
	 */
	private String smsCode;
	/**
	 * 1-手机号+验证码登录  2-手机号+密码登录
	 */
	private Integer loginType;
	/**
	 * 接入端id  7-社区居民app 8-社区物业app 9-社区居委会app
	 */
	private Integer terminalId;
	/**
	 * 户籍地址
	 */
	private String residentAddress;


	//-----住房信息-----

	/**
	 * 小区ID
	 */
	private Long comunityId;
	/**
	 * 社区的ID
	 */
	private Long orgId;
	/**
	 * 房屋的ID
	 */
	private Long addressId;
	/**
	 * 1,业主2，家属 3，租客
	 */
	private Integer identityType;
	//-----扩展信息------
	/**
	 * 居民类型id 1-自管党员 2-报道党员 3-居家养老 4-困难户 5-特困户 6-边缘户 7-残疾 8-优抚对象 9-享受低保 10-救助对象 11-矫正人员 12-邢释解救人员 13-精神病人 14-志愿者 15-退役军人',
	 */
	private List<Integer> extendType;
	/**
	 * 扩展信息的居民id
	 */
	private Long extendResidentId;
	/**
	 * 扩展信息的社区id
	 */
	private Long extendOrgId;

	//----编辑使用字段-----
	/**
	 * 居民住房信息
	 */
	private List<ResidentRelLocation> residentRelLocationList;
	/**
	 * 居民扩展信息
	 */
	private List<ResidentExtend> residentExtendList;

	//----点击减号使用----
	/**
	 * 居民住房关系id
	 */
	private Long locationRelId;
	/**
	 * 居民id
	 */
	private Long residentId;

	/**
	 * 社区的ID
	 */
	private Long norgId;
	/**
	 * 居民的扩展信息
	 */
	private List<Integer> extendTypeIds;
	/**
	 * 居民的住房信息
	 */
	private List<ResidentRelLocationVO> residentRelLocationVOS;
	/**
	 * 头像的图片
	 */
	private FileInfo iconFile;
	/**
	 * 证件照片 文件信息
	 */
	private List<FileInfo> credentialsPhotoFileInfos;
	/**
	 * 民族名称
	 */
	private String ethnicityName;
	/**
	 * 国籍名称
	 */
	private String nationalityName;
}
