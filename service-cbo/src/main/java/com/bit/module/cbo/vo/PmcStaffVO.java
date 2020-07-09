package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/17 15:28
 **/
@Data
public class PmcStaffVO extends BasePageVo{
	/**
	 * id
	 */
	private Long id;
	/**
	 * 物业人员的姓名
	 */
	private String name;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 密码盐
	 */
	private String salt;
	/**
	 * 权限 2维修工 1管理员
	 */
	private Integer role;
	/**
	 * 账号状态 用户状态 0停用 1启用
	 */
	private Integer status;
	/**
	 * 公司id
	 */
	private Long companyId;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	/**
	 * 创建者id
	 */
	private Long createUserId;
	/**
	 * 账号的创建来源：1,web 端管理员创建 ；2，app物业人员创建
	 */
	private Integer createType;
	/**
	 * 登录token
	 */
	private String token;

	/**
	 * 所属小区
	 */
	private List<Long> communityIds;

	/**
	 * 是否修改过小区 0未修改  1 修改
	 */
	private Integer communityModifyFlg;

	/**
	 * 1-手机号+验证码登录  2-手机号+密码登录
	 */
	private Integer loginType;
	/**
	 * 验证码
	 */
	private String smsCode;
	/**
	 * 接入端id
	 */
	private Integer terminalId;
}
