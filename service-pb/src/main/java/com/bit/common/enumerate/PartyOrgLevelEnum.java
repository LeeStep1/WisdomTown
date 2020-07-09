package com.bit.common.enumerate;

/**
 * 党组织类型
 * 1-镇党委 2-党总支 3-党支部 4-基层党委 5-社区党建工作部
 */
public enum PartyOrgLevelEnum {

    LEVEL_ONE(1,"等级1"),
    LEVEL_TWO(2,"等级2"),
    LEVEL_THREE(3,"等级3"),
    LEVEL_FOUR(4,"等级4"),
    LEVEL_FIVE(5,"等级5");

    /**
     * 操作码
     */
    private int code;

    /**
     * 操作信息
     */
    private String info;

    /**
     * @param code  状态码
     * @param info  状态信息
     */
    PartyOrgLevelEnum(int code, String info) {
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
