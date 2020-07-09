package com.bit.module.oa.enums;

/**
 * @Description : 补卡状态
 * @Date ： 2019/1/9 18:49
 */
public enum ApplicationStatusEnum {
    // 补卡状态 0未审批 1通过 2驳回
    APPLY(0, "未审批"), AUDIT(1, "审核"), REJECT(2, "驳回");

    private Integer key;
    private String description;

    ApplicationStatusEnum(Integer key, String description) {
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

    public static String getByKey(int key) {
        for (ApplicationStatusEnum app : values()) {
            if (app.key == key) {
                return app.getDescription();
            }
        }
        return "";
    }
}
