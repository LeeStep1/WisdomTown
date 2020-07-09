package com.bit.common.enumerate;

/**
 * 物业公司人员角色类型枚举
 * @author chenduo
 * @create 2019-04-11
 */
public enum PmcStaffRoleTypeEnum {
    /**
     * 管理员
     */
    PROPERTY_MANAGEMENT_ROLE_TYPE_ADMIN(1,"管理员"),

    /**
     * 维修工
     */
    PROPERTY_MANAGEMENT_ROLE_TYPE_REPAIR(2,"维修工"),

    /**
     * 账号的创建来源：1,web 端管理员创建
     */
    CREATE_TYPE_WEB(1,"web"),

    /**
     * 账号的创建来源：2，app物业人员创建
     */
    CREATE_TYPE_APP(2,"app"),

    /**
     * 修改过物业员工关系
     */
    COMMUNITYMODIFYFLG_TRUE(1,"已修改"),

    /**
     * 未修改过物业员工关系
     */
    COMMUNITYMODIFYFLG_FALSE(0,"未修改"),



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
    PmcStaffRoleTypeEnum(int code, String info) {
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
