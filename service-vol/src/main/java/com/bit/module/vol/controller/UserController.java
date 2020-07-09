package com.bit.module.vol.controller;


import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.Volunteer;
import com.bit.module.vol.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private VolunteerService volunteerService;

    /**
     * 设置用户业务信息
     * @param volunteer 界面填写的用户信息
     * @return
     */
    @PostMapping("/businessInfo")
    public BaseVo businessInfo(@RequestBody Volunteer volunteer){
        return volunteerService.setBusinessInfo(volunteer);
    }

    /**
     * 根据手机号和身份证号获取志愿者业务表中信息
     * @return
     */
    @PostMapping("/getVolunteerBusInessInfo")
    public BaseVo getVolunteerBusInessInfo(){
        return volunteerService.getVolunteerBusInessInfo();
    }

    /**
     * 获取所有志愿者ID
     * @author liyang
     * @date 2019-04-09
     */
    @GetMapping("/getAllVolUserIds")
    public List<Long> getAllVolUserIds(){
        return volunteerService.getAllVolUserIds();
    }
}
