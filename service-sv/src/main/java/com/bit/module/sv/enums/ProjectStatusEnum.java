package com.bit.module.sv.enums;

import com.bit.module.sv.bean.IdName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目进度状态枚举类
 *
 * Created by decai.liu
 * at 20190827
 */
public enum ProjectStatusEnum {

    PENDING_APPROVAL(0, "待审批"),
    IN_PROGRESS(1, "进行中"),
    PAUSED(2, "已暂停"),
    COMPLETED(3, "已完成");

    public int value;

    public String phrase;

    private static Map<Integer, String> map = new HashMap();
    public static List<IdName> list = new ArrayList<>();

    static {
        for (ProjectStatusEnum projectStatusEnum : ProjectStatusEnum.values()){
            map.put(projectStatusEnum.value, projectStatusEnum.phrase);
            IdName idName = new IdName();
            idName.setId(Long.valueOf(projectStatusEnum.value));
            idName.setName(projectStatusEnum.phrase);
            list.add(idName);
        }
    }

    ProjectStatusEnum(int value, String phrase) {
        this.value = value;
        this.phrase = phrase;
    }

    public static ProjectStatusEnum getByValue(int value){
        for (ProjectStatusEnum projectStatusEnum : values()) {
            if (projectStatusEnum.value == value) {
                return projectStatusEnum;
            }
        }
        return null;
    }

    public static String getPhrase(Integer value){
        return map.get(value) == null ? "未知状态" : map.get(value);
    }
}
