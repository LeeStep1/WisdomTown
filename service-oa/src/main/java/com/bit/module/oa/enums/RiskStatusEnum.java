package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/1/9 18:49
 */
public enum RiskStatusEnum {

    SUBMIT(0, "未反馈"), FEEDBACK(1, "已反馈");

    private Integer key;
    private String description;

    RiskStatusEnum(Integer key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Integer getKey() {
        return key;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static String getDescriptionByStringKey(String key) {
        for (RiskStatusEnum riskStatusEnum : values()) {
            if (riskStatusEnum.key.equals(Integer.valueOf(key))) {
                return riskStatusEnum.getDescription();
            }
        }
        return "";
    }
}
