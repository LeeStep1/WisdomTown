package com.bit.ms.bean;

import lombok.Data;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2019-03-20
 **/
@Data
public class SmsTemplate {


    /**编码**/
    private String code;

    /**模板的**/
    private int id;

    /**数量**/
    private int num;


    public SmsTemplate (String code,int id,int num){
       this.code=code;
       this.id=id;
       this.num=num;
    }

}
