package com.bit.soft.sms.bean;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:  短信请求日志 存放调用记录
 * @Author: liyujun
 * @Date: 2019-03-20
 **/
@Data
@Document(collection = "log-sms")
public class SmsSendLog  implements Serializable{

    @Id
    private ObjectId id;

    /** 应用ID**/

    private Integer appId;


    /**接入端ID**/

    private long terminalId;

    /** 应用模板code**/

    private String templateCode;


    private int  smsType;

    /** 参数code**/

    private String []param;

    /** 所要发的手机号**/

    private String []phoneNumbers;

    /**发送时间**/
    private Date sendTime;

}
