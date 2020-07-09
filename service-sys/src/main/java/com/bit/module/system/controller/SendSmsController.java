package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.SendSms;
import com.bit.module.system.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送短信相关请求
 * @author Liy
 * @date 2019-3-12
 */
@RestController
@RequestMapping(value = "/sendSMS")
public class SendSmsController {

    @Autowired
    private SendMessageService sendMessageService;

    /**
     * 注册用户发送验证码
     * @param sendSms 短信信息
     * @return
     */
    @PostMapping("/sendRegSMS")
    public BaseVo sendRegSMS(@RequestBody SendSms sendSms){
        return sendMessageService.SendRegSms(sendSms);
    }

    /**
     * 密码找回发送验证码
     * @param sendSms 短信信息
     * @return
     */
    @PostMapping("/sendRePwdSMS")
    public BaseVo sendRePwdSMS(@RequestBody SendSms sendSms){
        return sendMessageService.SendRetrievePwdSms(sendSms);
    }

    /**
     * 社区三个app 发送验证码
     * @param sendSms
     * @return
     */
    @PostMapping("/sendCboAppSMS")
    public BaseVo sendCboAppSMS(@RequestBody SendSms sendSms){
        return sendMessageService.sendCboAppSMS(sendSms);
    }
}
