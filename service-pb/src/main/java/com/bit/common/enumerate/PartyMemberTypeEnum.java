package com.bit.common.enumerate;

/**
 * 党员类型
 */
public enum PartyMemberTypeEnum {

    ADMIN(0,"管理员"),
    MEMBER(1,"党员");

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
    PartyMemberTypeEnum(int code, String info) {
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
