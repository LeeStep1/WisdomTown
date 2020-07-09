package com.bit.soft.sms.bean;

import com.bit.soft.sms.common.SmsAccountTemplateEnum;
import lombok.Data;

/**
 * @Description: 通知类的短信请求体
 * @Author: liyujun
 * @Date: 2020-05-18
 **/
@Data
public class SmsRequest {


    /**
     * 接入端ID
     */
    private Long terminalId;

    /**
     * 模板参数
     */
    private String[] params;

    /**
     * 电话号码
     */
    private String[] phoneNumbers;

    /**
     * 发送模板
     */
    private Integer smsTemplateId;


    /**
     * @return : boolean
     * @description: 验证参数方法
     * @author liyujun
     * @date 2020-05-18
     */
    public boolean checkParams() {
        boolean flag = true;
        if (terminalId == 0) {
            flag = false;
        } else if (params.length == 0) {
            flag = false;
        } else if (phoneNumbers.length == 0 || phoneNumbers == null) {
            flag = false;
        } else if (smsTemplateId == null) {
            flag = false;
        }
        return flag;
    }


}
