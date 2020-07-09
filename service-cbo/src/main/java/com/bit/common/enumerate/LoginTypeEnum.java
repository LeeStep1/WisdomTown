package com.bit.common.enumerate;
/**
 * 登录方式枚举
 * @author chenduo
 * @create 2019-04-11
 */
public enum LoginTypeEnum {
	/**
	 * 手机号+验证码登录
	 */
	LOGIN_TYPE_MOBILE_SMS_CODE(1,"手机号+验证码登录"),

	/**
	 * 手机号+密码登录
	 */
	LOGIN_TYPE_MOBILE_PASSWORD(2,"手机号+密码登录"),



	;


	/**
	 * 操作码
	 */
	private int code;

	/**
	 * 操作信息
	 */
	private String info;

	/**
	 * @param code  状态码
	 * @param info  状态信息
	 */
	LoginTypeEnum(int code, String info) {
		this.code = code;
		this.info = info;
	}

	public int getCode() {
		return code;
	}


	public String getInfo() {
		return info;
	}
}
