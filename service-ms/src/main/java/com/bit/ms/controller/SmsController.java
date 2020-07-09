package com.bit.ms.controller;

import com.bit.base.vo.BaseVo;
import com.bit.ms.bean.SmsRequest;
import com.bit.ms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @Description:
 * @Author: liyujun
 * @Date: 2019-03-20
 **/
@RestController
@RequestMapping(value = "/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;


    @PostMapping("/sendSms")
    public BaseVo sendSms(@RequestBody SmsRequest smsRequest){
        smsService.sendSms(smsRequest);
        return new BaseVo();
    }
}
