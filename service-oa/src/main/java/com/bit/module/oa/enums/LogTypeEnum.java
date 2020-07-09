package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/2/14 15:09
 */
public enum LogTypeEnum {
    OTHER(0, "其他"), INSPECT(1, "巡检"), RISK(2, "隐患"), APPLY(3, "补卡申请"), MEETING(4, "会议申请");

    private Integer key;
    private String description;

    LogTypeEnum(Integer key, String description) {
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
}
