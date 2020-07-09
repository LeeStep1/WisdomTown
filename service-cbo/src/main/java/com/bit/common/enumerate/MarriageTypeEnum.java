package com.bit.common.enumerate;

/**
 * 婚姻类型枚举
 * @author chenduo
 * @create 2019-04-11
 */
public enum MarriageTypeEnum {
    /**
     * 未婚
     */
    MARRIAGE_TYPE_NEVER_MARRIED(1,"未婚"),

    /**
     * 已婚
     */
    MARRIAGE_TYPE_MARRIED(2,"已婚"),

    /**
     * 离婚
     */
    MARRIAGE_TYPE_DIVORCE(3,"离婚"),

    /**
     * 丧偶
     */
    MARRIAGE_TYPE_WIDOWED(4,"丧偶"),




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
    MarriageTypeEnum(int code, String info) {
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
