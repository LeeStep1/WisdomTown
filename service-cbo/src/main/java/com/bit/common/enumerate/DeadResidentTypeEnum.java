package com.bit.common.enumerate;

/**
 * 死亡人员类型枚举类
 * @author chenduo
 * @create 2019-04-11
 */
public enum DeadResidentTypeEnum {
    /**
     * 残疾
     */
    DEAD_RESIDENT_TYPE_DISABLE(1,"残疾"),

    /**
     * 居家养老
     */
    DEAD_RESIDENT_TYPE_OLD(2,"居家养老"),

    /**
     * 失独
     */
    DEAD_RESIDENT_TYPE_LONELY(3,"失独"),

    /**
     * 低保
     */
    DEAD_RESIDENT_TYPE_LOW(4,"低保"),

    /**
     * 其他
     */
    DEAD_RESIDENT_TYPE_OTHER(5,"其他"),
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
    DeadResidentTypeEnum(int code, String info) {
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
