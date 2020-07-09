package com.bit.utils;

import com.alibaba.fastjson.JSON;

/**
 * @Description:
 * @Author: mifei
 * @Date: 2018-10-09
 **/
public class JSONUtil {

	public static String toJSONString(Object object) {
		return JSON.toJSONString(object);
	}

	/**
	 * string 转成 object
	 * @param str
	 * @return
	 */
	public static Object parse(String str) {
		return JSON.parse(str);
	}
}

