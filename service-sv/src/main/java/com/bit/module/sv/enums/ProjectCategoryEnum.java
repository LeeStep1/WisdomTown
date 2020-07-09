package com.bit.module.sv.enums;

import com.bit.module.sv.bean.IdName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工程类别枚举类
 *
 * Created by decai.liu
 * at 20190827
 */
public enum ProjectCategoryEnum {
    //1：城建，2：市政，3：拆迁，4：土整

    URBAN_CONSTRUCTION(1, "城建"),
    MUNICIPAL_ENGINEERING(2, "市政"),
    DEMOLITION(3, "拆迁"),
    LAND_CONSOLIDATION(4, "土整");

    public int value;

    public String phrase;

    private static Map<Integer, String> map = new HashMap();
    public static List<IdName> list = new ArrayList<>();

    static {
        for (ProjectCategoryEnum projectCategoryEnum : ProjectCategoryEnum.values()){
            map.put(projectCategoryEnum.value, projectCategoryEnum.phrase);
            IdName idName = new IdName();
            idName.setId(Long.valueOf(projectCategoryEnum.value));
            idName.setName(projectCategoryEnum.phrase);
            list.add(idName);
        }
    }

    ProjectCategoryEnum(int value, String phrase) {
        this.value = value;
        this.phrase = phrase;
    }

    public static ProjectCategoryEnum getByValue(int value){
        for (ProjectCategoryEnum projectCategoryEnum : values()) {
            if (projectCategoryEnum.value == value) {
                return projectCategoryEnum;
            }
        }
        return null;
    }

    public static String getPhrase(Integer value){
        return map.get(value) == null ? "未知状态" : map.get(value);
    }
}
