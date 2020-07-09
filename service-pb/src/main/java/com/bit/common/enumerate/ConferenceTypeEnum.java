package com.bit.common.enumerate;

public enum ConferenceTypeEnum {

    CONFERENCE_TYPE_DANGYUANDAHUI(1,"党员大会"),
    CONFERENCE_TYPE_DANGZHIBUWEIYUANHUI(2,"支部委员会"),
    CONFERENCE_TYPE_DANGXIAOZUHUI(3,"党小组会"),
    CONFERENCE_TYPE_DANGKE(4,"党课");

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
    ConferenceTypeEnum(int code, String info) {
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
