package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/1/10 17:45
 */
public enum VehicleIdleStatusEnum {
    BUSY(0, "忙碌"), IDLE(1, "空闲");

    private Integer key;

    private String description;

    VehicleIdleStatusEnum(Integer key, String description) {
        this.key = key;
        this.description = description;
    }

    public Integer getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }}
