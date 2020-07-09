package com.bit.ms.bean;

import lombok.Data;

/**
 * @Description:  短信请求
 * @Author: liyujun
 * @Date: 2019-03-20
 **/
@Data
public class SmsSendLog {

    /** 应用ID**/

    private Integer appId;

    /** 应用模板code**/

    private String templateCode;

    /** 参数code**/

    private String []param;

    /** 所要发的手机号**/

    private String []phoneNumbers;

    /**发送时间**/
    private String sendTime;
}
