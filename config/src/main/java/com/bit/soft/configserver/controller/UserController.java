package com.bit.soft.configserver.controller;


import com.bit.base.vo.BaseVo;
import com.bit.soft.configserver.bean.User;
import com.bit.soft.configserver.service.CheckLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private CheckLoginService checkLoginService;

    /**
     * 用户登录校验
     * @param user
     * @return
     */
    @PostMapping("/Login")
    public BaseVo userLogin(@RequestBody User user){

        return checkLoginService.checkLogin(user);
    }

    /**
     * 增加用户
     * @return
     */
    @PostMapping("/insertUser")
    public BaseVo insertUser(){

        return checkLoginService.insertUser();
    }
}
