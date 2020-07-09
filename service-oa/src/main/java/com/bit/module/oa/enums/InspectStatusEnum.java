package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/1/9 18:49
 */
public enum InspectStatusEnum {

    UNPUBLISHED(0, "未发布"), READY(1, "未执行"), EXECUTE(2, "执行中"), FINISH(3, "已完成"), GIVE_UP(4, "已终止"), END(5, "已结束");

    private Integer key;
    private String description;

    InspectStatusEnum(Integer key, String description) {
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
        for (InspectStatusEnum inspectStatusEnum : values()) {
            if (inspectStatusEnum.key == key) {
                return inspectStatusEnum.getDescription();
            }
        }
        return "";
    }
}
