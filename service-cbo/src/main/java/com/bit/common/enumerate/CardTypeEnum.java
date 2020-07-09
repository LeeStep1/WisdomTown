package com.bit.common.enumerate;

/**
 * 证件类型枚举
 * @author chenduo
 * @create 2019-04-11
 */
public enum CardTypeEnum {
    /**
     * 身份证
     */
    CARD_TYPE_ID_CARD(1,"身份证"),

    /**
     * 士官证
     */
    CARD_TYPE_OFFICER_CARD(2,"士官证"),

    /**
     * 护照
     */
    CARD_TYPE_PASSPORT(3,"护照"),

    /**
     * 港澳通行证
     */
    CARD_TYPE_HONGKONG_MACAO_PASSPORT(4,"港澳通行证"),




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
    CardTypeEnum(int code, String info) {
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
