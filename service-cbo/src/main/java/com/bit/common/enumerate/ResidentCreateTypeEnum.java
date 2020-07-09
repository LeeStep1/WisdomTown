package com.bit.common.enumerate;

/**
 * 居民账号创建来源枚举类
 * @author chenduo
 * @create 2019-04-11
 */
public enum ResidentCreateTypeEnum {
    /**
     * web 端
     */
    RESIDENT_CREATE_TYPE_WEB_ENUM(1,"web端"),

    /**
     * app注册
     */
    RESIDENT_CREATE_TYPE_APP_ENUM(2,"app注册"),



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
    ResidentCreateTypeEnum(int code, String info) {
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
