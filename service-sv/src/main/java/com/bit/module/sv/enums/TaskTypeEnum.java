package com.bit.module.sv.enums;

import com.bit.module.sv.bean.IdName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务类型枚举类
 *
 * Created by decai.liu
 * at 20190719
 */
public enum TaskTypeEnum {

    DAILY_CHECK(1, "RCXJ", "日常巡检"),
    SPECIAL_CHECK(2, "ZXPC", "专项排查"),
    ABNORMAL_CHECK(3, "YCPC", "异常排查");

    public int value;

    public String phrase;

    public String desc;

    private static Map<Integer, String> map = new HashMap();
    public static List<IdName> list = new ArrayList<>();

    static {
        for (TaskTypeEnum taskStatusEnum : TaskTypeEnum.values()){
            map.put(taskStatusEnum.value, taskStatusEnum.desc);
            IdName idName = new IdName();
            idName.setId(Long.valueOf(taskStatusEnum.value));
            idName.setName(taskStatusEnum.desc);
            list.add(idName);
        }
    }

    TaskTypeEnum(int value, String phrase, String desc) {
        this.value = value;
        this.phrase = phrase;
        this.desc = desc;
    }

    public static TaskTypeEnum getByValue(int value){
        for (TaskTypeEnum taskStatusEnum : values()) {
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
