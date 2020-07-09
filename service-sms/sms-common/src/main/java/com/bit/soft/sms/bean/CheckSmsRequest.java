package com.bit.soft.sms.bean;

import lombok.Data;

/**
 * @Description: 验证码请求体
 * @Author: liyujun
 * @Date: 2020-05-19
 **/
@Data
public class CheckSmsRequest {


    /**
     * 接入端ID
     */
    private long terminalId;

    /**
     * 电话号码
     */
    private String mobileNumber;

    /**
     * 模板id
     */
    private Integer smsTemplateId;

}
