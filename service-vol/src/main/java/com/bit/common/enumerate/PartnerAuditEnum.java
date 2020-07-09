package com.bit.common.enumerate;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-03 9:56
 */
public enum  PartnerAuditEnum {

    /**
     * 共建单位审核状态 0待审核，1已通过，2：已退回
     */
    PARTENER_AUDIT_STATE_WAIT_VERIFY(0,"待审核"),
    PARTENER_AUDIT_STATE_PASSED(1,"已通过"),
    PARTENER_AUDIT_STATE_REJECTED(2,"已退回");

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
    PartnerAuditEnum(int code, String info) {
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
