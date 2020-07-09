package com.bit.soft.dao;

import com.bit.ServiceSysApplication;
import com.bit.module.system.service.UserService;
import com.bit.module.system.vo.UserVO;
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
public class UserTest {

	@Autowired
	private UserService userService;

	@Test
	public void testLogin() {
		UserVO userVo = new UserVO();
		userVo.setUsername("mifei");
		userVo.setPassword("abc");
//		userService.login(userVo);
	}
	@Test
	public void testList() {
		UserVO userVo = new UserVO();
		userVo.setUsername("mifei");
		UserVO userVo2 = (UserVO) userService.list(userVo);
	}
}
