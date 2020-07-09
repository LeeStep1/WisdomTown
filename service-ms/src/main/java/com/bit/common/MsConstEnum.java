package com.bit.common;

/**
 * MS枚举类
 * @author Liy
 */
public enum MsConstEnum {

    /**
     * 发送成功code
     */
    JUPSHSUCCESS(200,"发送成功"),

    /**
     * 定时推送模板
     */
    TIMEDPUSH(10,"定时模板"),


    /**
     * 需要跳转
     */
    JUMPSTATUS(0,"跳转"),

    /**
     * 不需要跳转
     */
    UNJUMPSTATUS(1,"不跳转"),

    /**
     * 通知提醒
     */
    MSGTYPEFORNOTICE(3,"通知提醒"),

    /**
     * 公告提醒
     */
    MSGTYPEFORANNOUNCEMENT(4,"公告提醒"),

    /**
     * 业务ID字段名称
     */
    BUSSINESSID(1,"bussiness"),

    /**
     * appUrl字段名称
     */
    APPURL(2,"appUrl"),

    /**
     * 是否跳转字段名称
     */
    JUMPFLAG(3,"jumpFlag");

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
    MsConstEnum(int code, String info) {
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

