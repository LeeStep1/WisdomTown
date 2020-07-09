package com.bit.common.enumerate;

/**
 * 居民户籍枚举类
 * @author chenduo
 * @create 2019-04-11
 */
public enum ResidentResidenceEnum {
    /**
     * 本市户籍
     */
    RESIDENT_CREATE_TYPE_LOCAL_ENUM(1,"本市户籍"),

    /**
     * 非本市户籍
     */
    RESIDENT_CREATE_TYPE_NOT_LOCAL_ENUM(2,"非本市户籍"),



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
    ResidentResidenceEnum(int code, String info) {
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
