package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/1/15 9:47
 */
public enum VehicleApplicationUsageEnum {
    OFFICIAL(1, "GW", "公务用车"), RENT(2, "ZL", "租赁用车"), OTHER(3, "", "");

    private Integer key;

    private String prefix;

    private String description;

    VehicleApplicationUsageEnum(Integer key, String prefix, String description) {
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

    public static VehicleApplicationUsageEnum getByKey(int key) {
        for (VehicleApplicationUsageEnum vehicleApplicationUsageEnum : values()) {
            if (vehicleApplicationUsageEnum.key == key) {
                return vehicleApplicationUsageEnum;
            }
        }
        return OTHER;
    }
}
