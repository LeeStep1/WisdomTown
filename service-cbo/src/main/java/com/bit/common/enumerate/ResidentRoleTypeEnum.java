package com.bit.common.enumerate;

/**
 * 居民角色类型枚举类
 * @author chenduo
 * @create 2019-04-11
 */
public enum ResidentRoleTypeEnum {
    /**
     * 居民
     */
    RESIDENT_ROLE_TYPE_RESIDENT(1,"居民"),

    /**
     * 游客
     */
    RESIDENT_ROLE_TYPE_TOURIST(2,"游客"),



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
    ResidentRoleTypeEnum(int code, String info) {
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
