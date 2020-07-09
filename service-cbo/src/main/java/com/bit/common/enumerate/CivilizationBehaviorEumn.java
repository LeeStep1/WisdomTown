package com.bit.common.enumerate;

/**
 * @description: 文明行为相关枚举
 * @author: liyang
 * @date: 2019-08-07
 **/
public enum CivilizationBehaviorEumn {

    /**
     * 状态 待审核
     */
    STATUS_PENDING(1,"待审核"),

    /**
     * 状态 处理中
     */
    STATUS_BEING_PROCESSED(2,"处理中"),

    /**
     * 状态 待审核
     */
    STATUS_PROCESSED(3,"已处理"),

    /**
     * 数据来源--物业
     */
    DATE_TYPE_PMC(2,"物业"),

    /**
     *  数据来源--居民
     */
    DATE_TYPE_RESIDENT(3,"居民"),

    /**
     * 文明上报
     */
    REMIND_MESSAGE_CBO_CATEGORY(8,"文明上报"),

    /**
     * 文明上报
     */
    TASK_MESSAGE_CBO_CATEGORY(4,"文明上报"),
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
    CivilizationBehaviorEumn(int code, String info) {
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
