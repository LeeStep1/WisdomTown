package com.bit.module.system.vo;

import lombok.Data;

/**
 * @Description 更改密码和手机号的传参
 * @Author chenduo
 * @Date 2019/8/29 9:03
 **/
@Data
public class ChangePassWordMobileVO {
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 旧密码
	 */
	private String oldPassWord;
	/**
	 * 新密码
	 */
	private String newPassWord;
	/**
	 * 验证码
	 */
	private String smsCode;
}
