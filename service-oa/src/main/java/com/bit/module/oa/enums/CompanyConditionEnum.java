package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/1/17 13:14
 */
public enum CompanyConditionEnum {
    OPENING(1, "开业"), HOLD(2, "存续"), CLOSED(3, "停业"), CLEAR(4, "清算"), OTHER(5, "");

    private Integer key;

    private String description;

    CompanyConditionEnum(Integer key, String description) {
        this.key = key;
        this.description = description;
    }

    public Integer getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public static CompanyConditionEnum getByKey(int key) {
        for (CompanyConditionEnum companyConditionEnum : values()) {
            if (companyConditionEnum.key == key) {
                return companyConditionEnum;
            }
        }
        return OTHER;
    }
}
