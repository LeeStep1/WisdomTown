package com.bit.soft.sms.bean;

import lombok.Data;

/**
 * @Description: 校验验证码请求体
 * @Author: liyujun
 * @Date: 2020-05-21
 **/
@Data
public class SmsCodeRequest {


    /**
     * 验证的code
     */
    private String smsCode;

    /**
     * 模板id
     */
    private Integer smsTemplateId;

    /**
     * 电话号码
     */
    private String mobileNumber;


    /**
     * 接入端ID
     */
    private Long terminalId;


}
