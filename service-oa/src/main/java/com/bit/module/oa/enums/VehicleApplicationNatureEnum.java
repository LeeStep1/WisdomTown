package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/1/17 15:18
 */
public enum VehicleApplicationNatureEnum {

    MEETING(1, "会议"), EMERGENCY(2, "应急"), RECEIVE(3, "接待"), INVESTMENT(4, "招商"), WELCOME(5, "迎检"), OTHER(0, "其他");

    private Integer key;

    private String description;

    VehicleApplicationNatureEnum(Integer key, String description) {
        this.key = key;
        this.description = description;
    }

    public Integer getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public static VehicleApplicationNatureEnum getByKey(int key) {
        for (VehicleApplicationNatureEnum vehicleApplicationNatureEnum : values()) {
            if (vehicleApplicationNatureEnum.key == key) {
                return vehicleApplicationNatureEnum;
            }
        }
        return OTHER;
    }
}
