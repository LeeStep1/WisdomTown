package com.bit.common.enumerate;

/**
 * 党费已交未交
 */
public enum PartyDueStatusEnum {

    NOT_PAY(0,"未交"),
    PAID(1,"已交");

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
    PartyDueStatusEnum(int code, String info) {
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
