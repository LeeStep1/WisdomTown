package com.bit.soft.sms.bean;

import com.bit.base.vo.BasePageVo;
import lombok.Data;


/**
 * @Description:
 * @Author: liyujun
 * @Date: 2020-05-21
 **/
@Data
public class SmsLogPage  extends BasePageVo{


    /**
     *  接入端ID
     */
    private Long terminalId;


    /**
     *  短信類型
     */
    private Integer  smsType;



}
