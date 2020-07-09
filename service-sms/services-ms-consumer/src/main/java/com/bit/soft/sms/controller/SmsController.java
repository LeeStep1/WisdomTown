package com.bit.soft.sms.controller;

import com.bit.base.vo.BaseVo;

import com.bit.soft.sms.bean.CheckSmsRequest;
import com.bit.soft.sms.bean.SmsCodeRequest;
import com.bit.soft.sms.bean.SmsLogPage;
import com.bit.soft.sms.bean.SmsRequest;
import com.bit.soft.sms.service.SmsLogService;
import com.bit.soft.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Description: 短信服务
 * @Author: liyujun
 * @Date: 2019-03-20
 **/
@RestController
@RequestMapping(value = "/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsLogService smsLogService;

    /**
     * @param checkSmsRequest :
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author liyujun
     * @date 2020-06-02
     */
    @PostMapping("/code")
    public BaseVo sendSms(@RequestBody CheckSmsRequest checkSmsRequest) {
        return smsService.sendCheckSms(checkSmsRequest);
    }

    /**
     * @param smsRequest : 普通短信消息
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author liyujun
     * @date 2020-06-02
     */
    @PostMapping("/general")
    public BaseVo sendRegSms(@RequestBody SmsRequest smsRequest) {

        return smsService.sendSmsMessage(smsRequest);
    }

    /**
     * @description:  验证验证码
     * @author liyujun
     * @date 2020-06-02
     * @param smsCodeRequest :
     * @return : com.bit.base.vo.BaseVo
     */
    @PostMapping("/checkCode")
    public BaseVo checkCode(@RequestBody SmsCodeRequest smsCodeRequest) {
        return  smsService.checkSmsCode(smsCodeRequest);
    }


    /**
     * @param smsLogPage :
     * @return : com.bit.base.vo.BaseVo
     * @description: 查询对应的短信记录
     * @author liyujun
     * @date 2020-06-02
     */
    @PostMapping("/logs")
    public BaseVo getLog(@RequestBody SmsLogPage smsLogPage) {
        return smsLogService.smsSendLogPage(smsLogPage);
    }


    @GetMapping("/code")
    public BaseVo code() {
        return smsService.sendCheckSmsFeign(null);
    }
}
