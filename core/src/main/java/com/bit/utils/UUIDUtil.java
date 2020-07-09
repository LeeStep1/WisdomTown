package com.bit.utils;

import java.util.UUID;

/**
 * @Description:生成随机字符串的工具类
 * @Author: mifei
 * @Date: 2018-10-16
 **/
public class UUIDUtil {
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static void main(String[] args) {
		System.out.println("格式前的UUID ： " + UUID.randomUUID().toString());
		System.out.println("格式化后的UUID ：" + getUUID());
	}
}
