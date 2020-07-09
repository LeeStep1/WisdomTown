package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/1/9 18:49
 */
public enum ReportStatusEnum {

    SUBMIT(0, "未确认"), CONFIRM(1, "确认");

    private Integer key;
    private String description;

    ReportStatusEnum(Integer key, String description) {
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
