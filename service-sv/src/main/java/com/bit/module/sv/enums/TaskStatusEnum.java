package com.bit.module.sv.enums;

import com.bit.module.sv.bean.IdName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务状态枚举类
 *
 * Created by decai.liu
 * at 20190719
 */
public enum TaskStatusEnum {

    PENDING_CHECK(0, "待巡检"),
    CHECKING(1, "巡检中"),
    PENDING_RECTIFY(2, "待整改"),
    PENDING_REVIEW(3, "待复查"),
    COMPLETED(4, "已完成");

    public int value;

    public String phrase;

    private static Map<Integer, String> map = new HashMap();
    public static List<IdName> list = new ArrayList<>();

    static {
        for (TaskStatusEnum taskStatusEnum : TaskStatusEnum.values()){
            map.put(taskStatusEnum.value, taskStatusEnum.phrase);
            IdName idName = new IdName();
            idName.setId(Long.valueOf(taskStatusEnum.value));
            idName.setName(taskStatusEnum.phrase);
            list.add(idName);
        }
    }

    TaskStatusEnum(int value, String phrase) {
        this.value = value;
        this.phrase = phrase;
    }

    public static TaskStatusEnum getByValue(int value){
        for (TaskStatusEnum taskStatusEnum : values()) {
            if (taskStatusEnum.value == value) {
                return taskStatusEnum;
            }
        }
        return null;
    }

    public static String getPhrase(Integer value){
        return map.get(value) == null ? "未知状态" : map.get(value);
    }
}
