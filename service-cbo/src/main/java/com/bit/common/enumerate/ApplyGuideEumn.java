package com.bit.common.enumerate;

/**
 * @description: 办事指南申请相关枚举
 * @author: liyang
 * @date: 2019-08-07
 **/
public enum ApplyGuideEumn {

    /**
     * 是否有业务扩展信息:1有，0无
     */
    APPLY_EXTEND_FALSE(0,"无扩展信息"),

    /**
     * 是否有业务扩展信息:1有，0无
     */
    APPLY_EXTEND_TRUE(1,"有扩展信息"),

    /**
     * 低保业务申请表
     */
    RESIDENT_APPLY_BASE_LIVING_ALLOWANCES(1,"t_cbo_resident_apply_basic_living_allowances"),

    /**
     * 居家养老申请表
     */
    RESIDENT_APPLY_HOME_CARE(2,"t_cbo_resident_apply_home_care"),
    /**
     * 残疾人申请表
     */
    RESIDENT_APPLY_DISABLED_INDIVIDUALS(3,"t_cbo_resident_apply_disabled_individuals"),
    /**
     * 特别扶助申请表
     */
    RESIDENT_APPLY_SPECIAL_SUPPORT(4,"t_cbo_resident_apply_special_support"),


    /**
     * 扩展信息在字典表中的标识
     */
    EXTEND_TYPE(1,"cbo_resident_apply_extend_type"),

    /**
     * 办事指南类别
     */
    APPLY_GUIDE_GUIDE(1,"类别"),

    /**
     * 办事指南事项
     */
    APPLY_GUIDE_ITEMS(0,"事项"),

    /**
     * 类别事项启用标识
     */
    APPLY_GUIDE_USING(1,"启用"),

    /**
     * 类别事项停用标识
     */
    APPLY_GUIDE_DISABLE(0,"停用"),

    /**
     * 类别事项停用标识
     */
    APPLY_GUIDE_DRAFT(2,"草稿"),

    /**
     *  申请状态--进行中
     */
    APPLY_STATUS_USING(1,"进行中"),

    /**
     * 申请状态--待完善,
     */
    APPLY_STATUS_UNCOMMPLETE(2,"待完善"),

    /**
     * 申请状态--3已办结,，3已办结，4 已终止
     */
    APPLY_STATUS_COMMPLETE(3,"已办结"),

    /**
     * 申请状态--已终止
     */
    APPLY_STATUS_DISABLE(4,"已终止"),

    /**
     * 办理条件
     */
    APPLY_ITEMS_TYPE_CONDITIONS(1,"办理条件"),

    /**
     * 材料名称
     */
    APPLY_ITEMS_TYPE_MATERIAL_NAME(2,"材料名称"),

    /**
     * 办事流程
     */
    APPLY_ITEMS_TYPE_HANDLING(3,"办事流程"),
    /**
     * 附件
     */
    APPLY_ITEMS_TYPE_ATTACH_FILE(4,"附件"),

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
    ApplyGuideEumn(int code, String info) {
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
