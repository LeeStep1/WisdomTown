package com.bit.soft.sms.client;

import com.bit.base.vo.BaseVo;
import com.bit.soft.sms.bean.CheckSmsRequest;
import com.bit.soft.sms.bean.SmsCodeRequest;
import com.bit.soft.sms.bean.SmsRequest;
import com.bit.soft.sms.fallback.SmsHystrixFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description:  内部调用短信服务类
 * @Author: liyujun
 * @Date: 2020-06-04
 **/
@FeignClient(/*name ="SMSCLIENT",*/ value ="service-sms-product",fallback=SmsHystrixFallback.class)
public interface SmsFeignClient {


    /**
     * 发送短信验证码
     * @param smsRequest
     * @return
     */
    @RequestMapping(value = "/sms/code",method = RequestMethod.POST)
    BaseVo sendSmsCode(@RequestBody CheckSmsRequest checkSmsRequest);

    /**
     * 发送普通短信信息
     * @param smsRequest
     * @return
     */
    @RequestMapping(value = "/sms/general",method = RequestMethod.POST)
    BaseVo sendRegSms(@RequestBody SmsRequest smsRequest);

    /**
     * @description: 验证验证码
     * @author liyujun
     * @date 2020-06-04
     * @param smsCodeRequest : 
     * @return : com.bit.base.vo.BaseVo
     */
    @RequestMapping(value="/sms/checkCode", method = RequestMethod.POST)
     BaseVo checkCode(@RequestBody SmsCodeRequest smsCodeRequest) ;
}
