package com.bit.ms.service;

import com.bit.ms.bean.SmsRequest;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2019-03-20
 **/
public interface SmsService {


     void sendSms(SmsRequest smsRequest);
}
