package com.bit.common.consts;

/**
 * @Description:  应用枚举类
 * @Author: liyujun
 * @Date: 2019-08-30
 **/
public enum ApplicationTypeEnum {


    /**
     * 党建应用
     */
    APPLICATION_PB(1,"党建"),
    /**
     * 政务应用
     */
    APPLICATION_OA(2,"政务"),

    /**
     * 安检应用
     */
    APPLICATION_SV(3,"安检"),

    /**
     * 环保应用
     */
    APPLICATION_HB(4,"环保"),

    /**
     * 社区应用
     */
    APPLICATION_CBO(5,"社区"),

    /**
     * 城建应用
     */
    APPLICATION_CJ(6,"城建"),

    /**
     * 城建应用
     */
    APPLICATION_MANAGE(7,"后台管理"),

    /**
     * 志愿者
     */
    APPLICATION_VOL(8,"志愿者");


    private Integer applicationId;

    private String applicationName;


    private ApplicationTypeEnum  (Integer applicationId,String applicationName){

        this.applicationId=applicationId;

        this.applicationName=applicationName;

    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }
}
