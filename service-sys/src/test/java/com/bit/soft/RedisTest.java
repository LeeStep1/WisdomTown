package com.bit.soft;

import com.bit.ServiceSysApplication;
import com.bit.core.utils.CacheUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @Description: springboot 测试redis 集群
 * @Author: mifei
 * @Date: 2018-09-18
 **/
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = ServiceSysApplication.class) // 指定我们SpringBoot工程的Application启动类,1.5.4摒弃了SpringApplicationConfiguration注解
@WebAppConfiguration
public class RedisTest {

	@Autowired
	private CacheUtil cacheUtil;

	@Test
	public void testRedis() {
		cacheUtil.set("pos","abc");
		Object  str = (Object)cacheUtil.get("pos");
		System.out.println(str);
	}

}
