package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/2/15 15:34
 */
public enum InspectTypeEnum {
    INTELLIGENT(1, "ZN", "智能巡检"), LOCUS(2, "GJ", "轨迹巡检"), APPLY(3, "BK", "补卡申请"), OTHER(0, "", "");

    private Integer key;

    private String prefix;

    private String description;

    InspectTypeEnum(Integer key, String prefix, String description) {
        this.key = key;
        this.prefix = prefix;
        this.description = description;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDescription() {
        return description;
    }

    public Integer getKey() {
        return key;
    }

    public static InspectTypeEnum getByKey(int key) {
        for (InspectTypeEnum inspectTypeEnum : values()) {
            if (inspectTypeEnum.key == key) {
                return inspectTypeEnum;
            }
        }
        return OTHER;
    }
}
