package com.atex.plugins.customsolr.util;

public class SolrStringUtil {

    public static String replaceFirst(String origin, String match, String replacement) {
        StringBuffer sb = new StringBuffer(origin);
        int begin = origin.indexOf(match);
        if (begin!=-1) {
            sb.delete(begin, begin + match.length());
            sb.insert(begin, replacement);
        }
        return sb.toString();
    }
}
