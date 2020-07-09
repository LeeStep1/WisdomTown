package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/1/14 18:33
 */
public enum VehicleApplicationStatusEnum {
    DRAFT(0, "未派车"),
    SEND(1, "已派车"),
    END(2, "已结束"),
    REJECT(3, "已拒绝"),
    INVALID(4, "已失效");

    private Integer key;

    private String description;

    VehicleApplicationStatusEnum(Integer key, String description) {
        this.key = key;
        this.description = description;
    }

    public Integer getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }}
