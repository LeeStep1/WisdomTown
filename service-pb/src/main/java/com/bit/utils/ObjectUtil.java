package com.bit.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * @autor xiaoyu.fang
 * @date 2019/8/21 13:34
 */
public class ObjectUtil {

    /**
     *
     * @param object
     * @return
     */
    public static boolean checkObjAllFieldsIsNull(Object object) {
        if (null == object) {
            return true;
        }
        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
