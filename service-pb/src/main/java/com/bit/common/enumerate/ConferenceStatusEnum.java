package com.bit.common.enumerate;

/**
 * 会议状态
 */
public enum ConferenceStatusEnum {

    STATUS_DRAFT(0,"草稿"),
    STATUS_NOT_START(1,"未开始"),
    STATUS_RUNNING(2,"进行中"),
    STATUS_END(3,"已结束"),
    STATUS_CANCELED(4,"已取消");

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
    ConferenceStatusEnum(int code, String info) {
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
