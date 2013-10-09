package com.atex.plugins.customsolr.util;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.solr.common.util.DateUtil;

public class DateFormatUtil {

    protected static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String formatUTC(Date date) {
        DateFormatUtil util =  new DateFormatUtil();
        return DateFormatUtils.formatUTC(
              date, 
              util.getDefaultDateFormatPattern());
    }

    protected String getDefaultDateFormatPattern() {
        Collection<String> defaultFormat = DateUtil.DEFAULT_DATE_FORMATS;
        for (Object format:defaultFormat) {
            return (String) format;
        }
        return DEFAULT_DATE_PATTERN;
    }

    public static String getDefaultDateFormat() {
        DateFormatUtil util =  new DateFormatUtil();
        return util.getDefaultDateFormatPattern();

    }
}
