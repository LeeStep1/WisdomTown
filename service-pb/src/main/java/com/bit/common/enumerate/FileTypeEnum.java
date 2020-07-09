package com.bit.common.enumerate;

/**
 * 附件类型
 */
public enum FileTypeEnum {

    STUDY_PLAN_FILE(1,"学习计划附件"),
    CONFERENCE_FILE(2,"会议附件");

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
    FileTypeEnum(int code, String info) {
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
