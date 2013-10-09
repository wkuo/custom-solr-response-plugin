package com.atex.plugins.customsolr.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.polopoly.util.StringUtil;


public class MapParamUtil {
    public static final Logger LOG = Logger.getLogger(MapParamUtil.class.getName());
    protected static final Integer DEF_PAGE = 1 ;
    protected static final Integer DEF_PAGE_START = 0;
    protected static final Integer DEF_PAGE_LIMIT = 10;
    protected static final String PARAM_KEY_PAGE = "page";
    protected static final String PARAM_KEY_START = "start";
    protected static final String PARAM_KEY_LIMIT = "rows";
    protected static final String PARAM_KEY_FILTERQUERY = "fq";
    protected static final String PARAM_VAL_QUERY_ITFILTER = "inputTemplate:(%s)";
    protected static final String ESC_CHAR = "amp;|amp%3B";

    Map parameters;

//  protected Map getEasyMap(Map paramMap) {
//      Map outputMap = new LinkedHashMap();
//      for (Object objKey:paramMap.keySet()) {
//          String rawKey = (String) objKey;
//          String key = rawKey.replaceAll("amp;|amp%3B", "");
//          String[] objValues = (String[]) paramMap.get(rawKey);
//          if (objValues!=null) {
//              String value = objValues[objValues.length - 1];
//              outputMap.put(key, value);
//              LOG.log(Level.INFO, 
//                      "key " + key  
//                      +" value " + value);
//          }
//      }
//      return outputMap;
//  }
   
    public MapParamUtil(Enumeration paramNames, Map parameterMap) {
        parameters = getLinkedHashMap(paramNames, parameterMap);
    }

    public Map getParameterMap() {
        return parameters;
    }
    
    public String processParamString() {
        return processPageParamString(null, null, null);
    }

    public String processPageParamString(Integer page, Integer limit, Integer start) {
        StringBuffer paramStr = new StringBuffer();
        int paramCount = 0;

        Map outputMap = parameters;

        if (start==null || start<0) {
            start = getIntFromParamByKey(PARAM_KEY_START);
        }
        if (limit==null || limit<0) {
            limit = getIntFromParamByKey(PARAM_KEY_LIMIT);
        }
        if (page==null || page<0) {
            page = getIntFromParamByKey(PARAM_KEY_PAGE);
        }

        // Fill in the value if it's not specified at param
        if (limit==null) {
            limit = DEF_PAGE_LIMIT;
            outputMap.put(PARAM_KEY_LIMIT, limit.toString());
        }
        if (start==null) {
            outputMap.put(PARAM_KEY_START, DEF_PAGE_START.toString());
        }
        // Page can only be positive
        if (page==null || page<=0) {
            page = DEF_PAGE;
            if (start==null) {
                start = DEF_PAGE_START;
                outputMap.put(PARAM_KEY_START, start.toString());
            }
        } else {
            start = getStartByPage(page, limit);
        }
        outputMap.put(PARAM_KEY_START, start.toString());
        outputMap.put(PARAM_KEY_PAGE, page.toString());

        for (Object objKey :outputMap.keySet()) {
            String key = (String) objKey;
            String value = (String) outputMap.get(objKey);
            if (paramCount>=1) {
                paramStr.append("&");
            }
            paramStr.append(key).append("=");
            paramStr.append(value);
            paramCount++;
        }
        if (outputMap.size()>0) {
            paramStr.insert(0, "?");
        }
        return paramStr.toString();
    }

    protected Map getLinkedHashMap(Enumeration names, Map paramMap) {
        Map outputMap = new LinkedHashMap();
        while (names.hasMoreElements()) {
            String rawKey = names.nextElement().toString();
            String key = rawKey.replaceAll(ESC_CHAR, "");
            String[] objValues = (String[]) paramMap.get(rawKey);
            if (objValues!=null) {
                String value = objValues[objValues.length - 1];
                try {
                    String pValue = StringUtil
                            .getURLEncodedString(value) ;
                    pValue = pValue.replaceAll("%0D%0A|%0D|%0A", "");
                    pValue = StringUtil
                            .getURLDecodedString(pValue);
                    outputMap.put(key, pValue);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return outputMap;
    }

    public Integer getIntFromParamByKey(String key) {
        String csKey = getCsKey(parameters, key);
        Object object = parameters.get(csKey);
        String text = null; 
        if (object instanceof String[]) {
            String[] values = (String[]) object;
            if (values!=null) {
                text = values[values.length - 1];
            }
        } else {
            text = (String) object;
        }
        return ConvertionUtil.strToInt(text);
    }

    public Boolean getBoolFromParamByKey(String key) {
        String csKey = getCsKey(parameters, key);
        Object object = parameters.get(csKey);
        String text = null; 
        if (object instanceof String[]) {
            String[] values = (String[]) object;
            if (values!=null) {
                text = values[values.length - 1];
            }
        } else {
            text = (String) object;
        }
        return ConvertionUtil.strToBool(text);
    }

    public String getStringFromParamByKey(String key) {
        String csKey = getCsKey(parameters, key);
        Object object = parameters.get(csKey);
        String text = null; 
        if (object instanceof String[]) {
            String[] values = (String[]) object;
            if (values!=null) {
                text = values[values.length - 1];
            }
        } else {
            text = (String) object;
        }
        return text;
    }

    protected String getCsKey(Map paramMap, String key) {
        String csKey = null;
        for (Object keyObj:paramMap.keySet() ) {
            String compare = ((String) keyObj).replace(ESC_CHAR, "");
            if (key.equalsIgnoreCase(compare)) {
                csKey = compare;
            }
        }
        return csKey;
    }

    protected Integer getStartByPage(Integer page, Integer limit) {
        Integer start = DEF_PAGE_START;
        start = limit * (page - 1);
        return start;
    }

    public void applyITFilter(List<String> its) {
        String filterString = "";
        StringBuffer sb = new StringBuffer();
        int idx = 0 ;
        for (String it : its) {
            if (idx > 0)
                sb.append(" OR ");
            sb.append(it);
            idx++;
        }
        if (its.size()>0) {
            filterString = String.format(PARAM_VAL_QUERY_ITFILTER, sb.toString());
        }
        parameters.put(PARAM_KEY_FILTERQUERY, filterString);
    }
    
}
