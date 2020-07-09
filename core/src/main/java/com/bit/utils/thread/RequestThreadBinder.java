package com.bit.utils.thread;

/**
 * @Description:线程绑定类
 * @Author: mifei
 * @Date: 2018-10-18
 **/
public class RequestThreadBinder {
	// 声明当前线程 指定泛型为request
	private static ThreadLocal<String> local = new ThreadLocal<String>();

	// 将request绑定到当前线程的方法
	public static void bindUser(String request) {
		local.set(request);
	}
	// 从当前线程获取request的方法
	public static String getUser() {
		return local.get();
	}

	// 从当前线程移除request的方法
	public static void removeUser() {
		local.remove();
	}
}
