package com.bit.module.oa.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description : 0待发布 1待审核 2已驳回 3待开始 4进行中 5已取消 6已结束
 * @Date ： 2019/1/9 18:49
 */
public enum MeetingStatusEnum {

    DEFAULT(-1, "无状态"),
    UNPUBLISHED(0, "未发布"),
    TO_AUDIT(1, "待审核"),
    REJECT(2, "已驳回"),
    READY(3, "待开始"),
    EXECUTE(4, "进行中"),
    CANCEL(5, "已取消"),
    END(6, "已结束"),
    INVALID(7, "已失效");

    private Integer key;
    private String description;

    MeetingStatusEnum(Integer key, String description) {
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

    public static String getDescriptionByKey(int key) {
        for (MeetingStatusEnum inspectStatusEnum : values()) {
            if (inspectStatusEnum.key == key) {
                return inspectStatusEnum.getDescription();
            }
        }
        return "";
    }

    public static MeetingStatusEnum getByKey(Integer key) {
        for (MeetingStatusEnum inspectStatusEnum : values()) {
            if (inspectStatusEnum.key.equals(key)) {
                return inspectStatusEnum;
            }
        }
        return DEFAULT;
    }

    /**
     * 判断是否处于审核范围内
     * @param toJudgeStatus
     * @return
     */
    public static Boolean isAuditStatus(Integer toJudgeStatus) {
        Set<Integer> auditStatusSet = new HashSet<>(Arrays.asList(TO_AUDIT.key, REJECT.key, READY.key));
        return auditStatusSet.contains(toJudgeStatus);
    }
}
