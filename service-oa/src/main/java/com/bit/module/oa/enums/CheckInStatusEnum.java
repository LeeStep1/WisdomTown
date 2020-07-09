package com.bit.module.oa.enums;

import lombok.AllArgsConstructor;

/**
 * @Description : 签到状态
 * @Date ： 2019/1/9 18:49
 */
@AllArgsConstructor
public enum CheckInStatusEnum {
    // 0缺卡 1正常 2补卡 3补卡成功 4补卡失败
    MISSING(0, "缺卡"), NORMAL(1, "正常"), PATCH(2, "补卡"), PATCH_SUCCESS(3, "补卡成功"), PATCH_FAILED(4, "补卡失败");

    private Integer key;
    private String description;

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
        for (CheckInStatusEnum checkInStatusEnum : values()) {
            if (checkInStatusEnum.key == key) {
                return checkInStatusEnum.getDescription();
            }
        }
        return "";
    }
}
