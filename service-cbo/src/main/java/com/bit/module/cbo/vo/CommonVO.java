package com.bit.module.cbo.vo;

import lombok.Data;

/**
 * @Description app重置密码传递参数使用
 * @Author chenduo
 * @Date 2019/7/17 15:22
 **/
@Data
public class CommonVO {
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
	/**
	 * 居民id or 物业人员id
	 */
	private Long id;
}
