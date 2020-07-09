package com.bit.common.enumerate;

/**
 * 学习计划发布状态
 */
public enum StudyReleaseStatusEnum {

    NOT_RELEASED(0,"草稿"),
    RELEASED(1,"已发布");

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
    StudyReleaseStatusEnum(int code, String info) {
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
