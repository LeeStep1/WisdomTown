package com.bit.module.system.bean;

import lombok.Data;

/**
 * @Description 社区app重置密码传递参数使用
 * @Author chenduo
 * @Date 2019/7/17 15:22
 **/
@Data
public class CommonModel {
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 验证码
	 */
	private String smsCode;
	/**
	 * 密码
	 */
	private String password;
}
