package com.atex.plugins.customsolr.util;

import com.polopoly.util.StringUtil;

public class ConvertionUtil {
    public static Integer strToInt(String text) {
        Integer value = null;
        if (!StringUtil.isEmpty(text)) {
            try {
                value = Integer.valueOf(text);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return value;
    }

    public static Boolean strToBool(String text) {
        Boolean value = false;
        if (!StringUtil.isEmpty(text)) {
            value = Boolean.valueOf(text);
        }
        return value;
    }

}
