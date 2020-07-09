package com.bit.common;

/**
 * 日志枚举类
 * @author Liy
 */
public enum ApplyLogCode {
    /**
     * 审核通过
     */
    AUDIT_PASS(1, "审核通过"),
    /**
     * 审核拒绝
     */
    AUDIT_BACK(2, "审核拒绝"),

    /**
     * 志愿者
     */
    /**
     * 审核志愿者风采
     */
    AUDIT_NEWS(3, "审核志愿者风采"),

    /**
     * 退回志愿者风采
     */
    BACK_NEWS(9, "退回志愿者风采"),

    /**
     * 提交志愿者风采
     */
    DEPELOYING_NEWS(10, "提交了一条志愿者风采审批"),


    /**
     * 志愿者等级
     */
    /**
     * 提交
     */
    AUDIT_LEVEL_SUBMIT(4, "提交"),
    /**
     * 退回
     */
    AUDIT_LEVEL_REJECTED(5, "退回"),

    /**
     * 共建单位
     */
    /**
     * 共建单位提交
     */
    AUDIT_PARTNER_ORG_SUBMIT(6, "提交"),
    /**
     * 共建单位提交
     */
    AUDIT_PARTNER_ORG_REJECTED(7, "退回");


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
    ApplyLogCode(int code, String info) {
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

