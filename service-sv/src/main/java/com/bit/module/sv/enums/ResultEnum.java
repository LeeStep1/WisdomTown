package com.bit.module.sv.enums;

import com.bit.module.sv.bean.IdName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 巡检结果枚举类
 *
 * Created by decai.liu
 * at 20190722
 */
public enum ResultEnum {

    PASS(1, "通过"),
    WITHOUT(2, "无此项"),
    FAIL(0, "不通过");

    public int value;

    public String phrase;

    private static Map<Integer, String> map = new HashMap();
    public static List<IdName> list = new ArrayList<>();

    static {
        for (ResultEnum taskStatusEnum : ResultEnum.values()){
            map.put(taskStatusEnum.value, taskStatusEnum.phrase);
            IdName idName = new IdName();
            idName.setId(Long.valueOf(taskStatusEnum.value));
            idName.setName(taskStatusEnum.phrase);
            list.add(idName);
        }
    }

    ResultEnum(int value, String phrase) {
        this.value = value;
        this.phrase = phrase;
    }

    public static ResultEnum getByValue(int value){
        for (ResultEnum taskStatusEnum : values()) {
            if (taskStatusEnum.value == value) {
                return taskStatusEnum;
            }
        }
        return null;
    }

    public static String getPhrase(Integer value){
        return map.get(value) == null ? "未知结果" : map.get(value);
    }
}
