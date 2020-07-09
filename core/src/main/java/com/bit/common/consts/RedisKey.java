package com.bit.common.consts;

/**
 * @Description:  静态类
 * @Author: mifei
 **/
public enum RedisKey {

    /**
     * 短信注册总和
     */
    SMS_REGISTER_TOTAL("register:total:{0}:{1}"),
    /**
     * 短信注册验证码
     */
    SMS_REGISTER_VERIFICATION_CODE("register:verification:{0}:{1}"),
    /**
     * 密码找回总和
     */
    SMS_BACKPWD_TOTAL("retrieve:total:{0}:{1}"),
    /**
     * 密码找回验证码
     */
    SMS_BACKPWD_VERIFICATION_CODE("retrieve:verification:{0}:{1}"),

    /**
     * 志愿者报名key
     */
    VOL_ENROLL("campaign:id:"),
    /**
     * 志愿者排行榜key
     */
    VOL_BOARD("volunteer:board:"),
    /**
     * 字典缓存
     */
	DICT_CACHE("sys:dict:{0}"),

	/**
	 * 社区三个app 登录
	 */
	SMS_CBO_LOGIN_CODE("login:verification:{0}:{1}"),


	/**
	 * 社区三个app 登录总数
	 */
	SMS_CBO_LOGIN_TOTAL_CODE("login:total:{0}:{1}"),
	/**
	 * 社区三个app 改变手机号
	 */
	SMS_CBO_CHANGE_MOBILE_CODE("changeMobile:verification:{0}:{1}"),
	/**
	 * 社区三个app 改变手机号总数
	 */
	SMS_CBO_CHANGE_TOTAL_MOBILE_CODE("changeMobile:total:{0}:{1}"),

	;

	/**
     * 操作信息
     */
    private String key;

    /**
     * @param key  状态信息
     */
    RedisKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
