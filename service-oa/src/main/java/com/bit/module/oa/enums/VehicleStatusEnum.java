package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/1/9 18:49
 */
public enum VehicleStatusEnum {

    DISABLE(0, "停用"), ENABLED(1, "启用");

    private Integer key;
    private String description;

    VehicleStatusEnum(Integer key, String description) {
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
    }}
