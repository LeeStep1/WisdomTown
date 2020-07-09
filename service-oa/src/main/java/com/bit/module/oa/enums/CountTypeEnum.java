package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/4/26 14:25
 */
public enum CountTypeEnum {
    TODAY(1, "今日"), ALL(2, "全部"), OTHER(3, "其他");

    private Integer type;
    private String info;

    CountTypeEnum(Integer type, String info) {
        this.type = type;
        this.info = info;
    }

    public Integer getType() {
        return type;
    }

    public String getInfo() {
        return info;
    }}
