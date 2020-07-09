package com.bit.common.enumerate;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-03 9:48
 */
public enum CampaignAuditEnum {
    /**
     * 审批状态 状态 0-已通过 1-草稿 2-待审核 3-已拒绝
     */
    CAMPAIGN_AUDIT_STATUS_PASSED(0, "已通过"),
    CAMPAIGN_AUDIT_STATUS_DRAFT(1, "草稿"),
    CAMPAIGN_AUDIT_STATUS_WAIT_VERIFY(2, "待审核"),
    CAMPAIGN_AUDIT_STATUS_REJECTED(3, "已拒绝");

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
    CampaignAuditEnum(int code, String info) {
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
