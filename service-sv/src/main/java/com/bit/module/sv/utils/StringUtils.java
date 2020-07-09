package com.bit.module.sv.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 根据api请求路径区分app应用，从而设定数据来源
     * @param request
     * @return
     */
    public static Integer getSourceByHttpServletRequest(HttpServletRequest request) {
        String path = request.getHeader("x-forwarded-prefix");
        if ("/sv".equals(path)) {
            return 3;
        }
        if ("/ep".equals(path)) {
            return 4;
        }
        if ("/uc".equals(path)) {
            return 6;
        }
        return null;
    }

    /**
     * 校验数据来源是否合法
     * @param source
     * @return
     */
    public static boolean checkSourceValid(Integer source) {
        if (Arrays.asList(3, 4, 6).contains(source)) {
            return true;
        }
        return false;
    }
}
