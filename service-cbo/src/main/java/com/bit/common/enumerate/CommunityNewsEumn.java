package com.bit.common.enumerate;

/**
 * @description: 办事指南申请相关枚举
 * @author: liyang
 * @date: 2019-08-07
 **/
public enum CommunityNewsEumn {
    STATUS_DARFT(0,"草稿"),

    STATUS_PUBLISH(1,"已发布"),

    ;


    /**
     * 枚举的值
     */
    private int code;

    /**
     * 信息
     */
    private String info;

    /**
     * @param code  枚举的值
     * @param info  信息
     */
    CommunityNewsEumn(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }


    public String getInfo() {
        return info;
    }
}
