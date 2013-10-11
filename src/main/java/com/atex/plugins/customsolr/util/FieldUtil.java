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
        Element sfElement = ElementAttrUtil.getElementByAttr(lukeRoot, OP_NAME, IP_FIELDS);
        if (sfElement!=null) {
            List<?> sourceFields = sfElement.getChildren(); 
            for (Object fieldObj: sourceFields) {
                Element field = (Element) fieldObj;
                String name = field.getAttributeValue(OP_NAME);
                fieldNames.add(name);
            }
        }
        return fieldNames;
    }
}
