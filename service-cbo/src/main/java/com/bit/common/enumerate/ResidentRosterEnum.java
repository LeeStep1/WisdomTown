package com.bit.common.enumerate;

/**
 * @description: 服务名单 相关枚举
 * @author: liyang
 * @date: 2019-08-09
 **/
public enum ResidentRosterEnum {

    /**
     * 管理员手动创建的服务名单
     */
    DATA_TYPE_INSERT(0,"手动创建"),

    /**
     * 台账转换过来
     */
    DATA_TYPE_CONVERT(1,"台账转换"),


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
    ResidentRosterEnum(int code, String info) {
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
