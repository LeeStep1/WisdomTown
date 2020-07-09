package com.bit.common.enumerate;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-03 13:57
 */
public enum LevelAuditEnum {
    /**
     * 志愿者等级审核状态  0-审核中下级审批 1-审核中上级审批 2-已通过 3-已退回
     */
    LEVEL_AUDIT_STATUS_VERIFYING_OWN(0,"审核中下级审批"),
    LEVEL_AUDIT_STATUS_VERIFYING_UP(1,"审核中上级审批"),
    LEVEL_AUDIT_STATUS_PASSED(2,"已通过"),
    LEVEL_AUDIT_STATUS_REJECTED(3,"已退回");

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
    LevelAuditEnum(int code, String info) {
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
