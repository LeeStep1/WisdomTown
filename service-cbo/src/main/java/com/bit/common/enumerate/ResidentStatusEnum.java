package com.bit.common.enumerate;

/**
 * 居民状态类型枚举类
 * @author chenduo
 * @create 2019-04-11
 */
public enum ResidentStatusEnum {
    /**
     * 停用
     */
    RESIDENT_STATUS_STOP(0,"停用"),

    /**
     * 正常
     */
    RESIDENT_STATUS_NORMAL(1,"正常"),

    /**
     * 待完善
     */
    RESIDENT_STATUS_NEED_FILL(2,"待完善"),



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
    ResidentStatusEnum(int code, String info) {
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
