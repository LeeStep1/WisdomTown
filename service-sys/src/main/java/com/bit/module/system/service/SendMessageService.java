package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.SendSms;

public interface SendMessageService {

    /**
     * 发送注册短信
     * @param sendSms 发送信息
     * @return
     */
    BaseVo SendRegSms(SendSms sendSms);

    /**
     * 发送密码找回短信
     * @param sendSms 发送信息
     * @return
     */
    BaseVo SendRetrievePwdSms(SendSms sendSms);

    /**
     * 社区app 发送验证码
     * @param sendSms
     * @return
     */
    BaseVo sendCboAppSMS(SendSms sendSms);
}
