package com.bit.common.enumerate;

/**
 * @description: 办事台账 相关枚举
 * @author: liyang
 * @date: 2019-08-09
 **/
public enum ResidentApplyBaseEnum {

    /**
     * 已生成服务名单
     */
    GENERATEROSTER_TRUE(1,"已生成"),

    /**
     * 未生成服务名单
     */
    GENERATEROSTER_FALSE(0,"未生成"),

    /**
     * 不需要生成服务名单
     */
    GENERATEROSTER_NONEED(2,"不需要生成"),

    /**
     * 低保业务申请
     */
    BASE_APPLY_LIVING_ALLOWANCES(1,"t_cbo_resident_apply_basic_living_allowances"),

    BASE_APPLY_LIVING_ALLOWANCES_CLASS(1,"BasicLivingAllowancesImpl"),

    /**
     * 居家养老业务申请
     */
    BASE_RESIDENT_APPLY_HOME_CARE(2,"t_cbo_resident_apply_home_care"),

    BASE_RESIDENT_APPLY_HOME_CARE_CLASS(2,"ResidentApplyHomeCareImpl"),

    /**
     * 残疾人业务申请
     */
    BASE_APPLY_DISABLE(3,"t_cbo_resident_apply_disabled_individuals"),

    /**
     * 特殊扶助业务申请
     */
    BASE_RESIDENT_SPECIAL_SUPPORT(4,"t_cbo_resident_apply_special_support"),

    /**
     * 申请状态--1 进行中
     */
    APPLY_STATUS_USING(1,"进行中"),

    /**
     * 申请状态--1 待完善
     */
    APPLY_STATUS_CORVIDAE(2,"待完善"),

    /**
     * 申请状态--1 已办结
     */
    APPLY_STATUS_COMPLETE(3,"已办结"),

    /**
     * 申请状态--1 已终止
     */
    APPLY_STATUS_TERMINATED(4,"已终止"),

    /**
     * 流程审核状态  未审核
     */
    AUDIT_STATUS_UNREVIEWED(0,"未审核"),

    /**
     * 流程审核状态  通过
     */
    AUDIT_STATUS_SUCCESS(1,"通过"),

    /**
     * 流程审核状态  未通过
     */
    AUDIT_STATUS_FAIL(2,"未通过"),

    /**
     * 1 低保申请、
     */
    GUIDE_EXTEND_TYPE_LIVING(1,"t_cbo_resident_basic_living_roster"),

    /**
     * 2 居家养老
     */
    GUIDE_EXTEND_TYPE_HOME_CARE(2,"t_cbo_resident_apply_home_care"),
    /**
     * 3 残疾人申请
     */
    GUIDE_EXTEND_TYPE_DISABLE(3,"t_cbo_resident_apply_disabled_individuals"),

    /**
     * 4 特别扶助
     */
    GUIDE_EXTEND_TYPE_SPECIAL_SUPPORT(4,"t_cbo_resident_apply_special_support"),
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
    ResidentApplyBaseEnum(int code, String info) {
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
