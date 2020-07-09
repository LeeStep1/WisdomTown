package com.bit.soft.sms.service;

import com.bit.base.vo.BaseVo;
import com.bit.soft.sms.bean.CheckSmsRequest;
import com.bit.soft.sms.bean.SmsCodeRequest;
import com.bit.soft.sms.bean.SmsRequest;


/**
 * @Description:
 * @Author: liyujun
 * @Date: 2019-03-20
 **/
public interface SmsService {

    /**
     * @param smsRequest :  短信请求实体
     * @return : void
     * @description: 发送普通短信
     * @author liyujun
     * @date 2020-05-19
     */
    BaseVo sendSmsMessage(SmsRequest smsRequest);


    /**
     * @param smsRequest : 验证码请求体
     * @return : com.bit.base.vo.BaseVo
     * @description: 发送验证使用的短信
     * @author liyujun
     * @date 2020-05-19
     */
    BaseVo sendCheckSms(CheckSmsRequest smsRequest);


    /**
     * @return : com.bit.base.vo.BaseVo
     * @description: 验证短信code 值
     * @author liyujun
     * @date 2020-05-21
     */
    BaseVo checkSmsCode(SmsCodeRequest request);


    /**
     * @param smsRequest :
     * @return : com.bit.base.vo.BaseVo
     * @description:  测试使用的方法
     * @author liyujun
     * @date 2020-06-05
     */
    BaseVo sendCheckSmsFeign(CheckSmsRequest smsRequest);


}
