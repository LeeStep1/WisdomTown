package com.bit.common.enumerate;

/**
 * 居民身份枚举类
 * @author chenduo
 * @create 2019-04-11
 */
public enum ResidentIdentityEnum {
    /**
     * 业主
     */
    RESIDENT_IDENTITY_OWNER(1,"业主"),

    /**
     * 家属
     */
    RESIDENT_IDENTITY_FAMILY_MEMBER(2,"家属"),

    /**
     * 租客
     */
    RESIDENT_IDENTITY_RENT(3,"租客"),

    

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
    ResidentIdentityEnum(int code, String info) {
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
