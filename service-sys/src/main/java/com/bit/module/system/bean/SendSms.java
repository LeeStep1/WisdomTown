package com.bit.module.system.bean;

import lombok.Data;

/**
 * 发送短信属性
 * @author Liy
 */
@Data
public class SendSms {
    /**
     * 发送短信手机号
     */
    public String mobileNumber;

    /**
     * 接入端ID
     */
    public String terminalId;

    /**
     * 发送类型 1、注册  2、找回  3、登录  4、社区app修改手机号密码
     */
    public int operationFlg;

    /**
     * 短信模板ID
     */
    public String templateId;
}
