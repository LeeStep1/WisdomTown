package com.bit.soft.push.msEnum;

/**
 * @Description:  模板属性
 * @Author: liyujun
 * @Date: 2019-08-30
 **/

public enum MessageTemplatePropertyEnum {

    /**
     * 需要跳转
     */
    JUMPSTATUS(0,"跳转"),

    /**
     * 不需要跳转
     */
    UNJUMPSTATUS(1,"不跳转");


    private int property;

    private String info;


    MessageTemplatePropertyEnum(int property, String info){

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
