package com.bit.soft.push.msEnum;

/**
 * @Description:  极光推送中相关属性
 * @Author: liyujun
 * @Date: 2019-08-30
 **/
public enum JpushPropertyEnum {

    /**
     * 发送成功code
     */
    JUPSHSUCCESS(200,"发送成功"),

    /**
     * 定时推送模板
     */
    TIMEDPUSH(10,"定时模板");


    private int property;

    private String info;


    JpushPropertyEnum(int property,String info){

        this.property=property;

        this.info=info;
    }

    public int getProperty() {
        return property;
    }

    public String getInfo() {
        return info;
    }


}
