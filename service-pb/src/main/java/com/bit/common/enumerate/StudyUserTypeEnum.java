package com.bit.common.enumerate;

/**
 * 学习人员类型
 */
public enum StudyUserTypeEnum {

    ABSOLUTE(0,"必学人员"),
    OPTIONAL(1,"自学人员");

    /**
     * 操作码
     */
    private int code;

    /**
     * 操作信息
     */
    private String info;

    /**
     * @param code  状态码
     * @param info  状态信息
     */
    StudyUserTypeEnum(int code, String info) {
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
