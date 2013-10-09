package com.atex.plugins.customsolr.util;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class FieldUtil {

    protected static final String LUKE_ADMIN = "/admin/luke";
    protected static final String IP_FIELDS = "fields";
    protected static final String OP_NAME = "name";

    public static List<String> getAllFieldNames(String solrServerUrl, String selectedCore) {

        List<String> fieldNames = new ArrayList<String>();

        String lukeUrl = solrServerUrl + "/" +selectedCore + LUKE_ADMIN;
        Element lukeRoot = JdomUtil.getElement(lukeUrl);
        List<Element> lst = lukeRoot.getChildren();
        for (Element ele: lst) {
            if (IP_FIELDS.equalsIgnoreCase(ele.getAttributeValue(OP_NAME))) {
                List sourceFields = ele.getChildren();
                for (Object fieldObj: sourceFields) {
                    Element field = (Element) fieldObj;
                    String name = field.getAttributeValue(OP_NAME);
                    fieldNames.add(name);
                }
            }
        }
        return fieldNames;
  }

}
