package com.bit.module.oa.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description :
 * @Date ： 2019/4/29 15:51
 */
public class EmojiRegexUtil {

    private static Pattern fullRegexPattern = Pattern
            .compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    public static boolean existEmoji(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }

        Matcher matcher = fullRegexPattern.matcher(str);

        return matcher.find();
    }
}
