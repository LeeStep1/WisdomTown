package com.bit.common.enumerate;

/**
 * 物业相关枚举类
 * @author liyang
 * @create 2019-07-18
 */
public enum PmcCompanyEnum {

    /**
     * 启用标识
     */
    USING_FLAG(1,"启用"),

    /**
     * 停用标识
     */
    DISABLE_FLAG(0,"停用"),

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
    PmcCompanyEnum(int code, String info) {
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
