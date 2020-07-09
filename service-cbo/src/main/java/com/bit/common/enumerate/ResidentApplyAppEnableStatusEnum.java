package com.bit.common.enumerate;

/**
 * app端居民住房认证审核类型枚举
 * @author chenduo
 * @create 2019-04-11
 */
public enum ResidentApplyAppEnableStatusEnum {
    /**
     * 待审核
     */
    APPLY_STATUS_NOT_VERIFY(0,"待审核"),

    /**
     * 审核通过
     */
    APPLY_STATUS_PASSED(1,"审核通过"),

    /**
     * 审核驳回
     */
    APPLY_STATUS_REJECTED(2,"审核驳回"),

    /**
     * 失效
     */
    APPLY_STATUS_LOST_FUNCTION(3,"失效"),


    ;


    /**
     * 枚举的值
     */
    private int code;

    /**
     * 信息
     */
    private String info;

    /**
     * @param code  枚举的值
     * @param info  信息
     */
    ResidentApplyAppEnableStatusEnum(int code, String info) {
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
