package com.bit.common.enumerate;

/**
 * 政治面貌类型枚举
 * @author chenduo
 * @create 2019-04-11
 */
public enum PoliticalTypeEnum {
    /**
     * 群众
     */
    POLITICAL_TYPE_PEOPLE(1,"群众"),

    /**
     * 党员
     */
    POLITICAL_TYPE_PARTY_MEMBER(2,"党员"),

    /**
     * 团员
     */
    POLITICAL_TYPE_LEAGUE_MEMBER(3,"团员"),

    /**
     * 民主党派
     */
    POLITICAL_TYPE_DEMOCRATIC_PARTY(4,"民主党派"),

    /**
     * 其他
     */
    POLITICAL_TYPE_OTHER(5,"其他"),




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
    PoliticalTypeEnum(int code, String info) {
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
