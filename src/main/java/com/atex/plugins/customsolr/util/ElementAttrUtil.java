package com.atex.plugins.customsolr.util;

import java.util.List;

import org.jdom.Element;

public class ElementAttrUtil {

    public static Element getElementByAttr(Element root, String attrName, String attrVal) {
        List<?> list = root.getChildren();
        if (attrVal.equalsIgnoreCase(root.getAttributeValue(attrName))) {
            return root;
        }
        for (Object obj : list) {
            Element element = (Element) obj;
            if (attrVal.equalsIgnoreCase(element.getAttributeValue(attrName))) {
                return element;
            } else {
                Element childElement = getElementByAttr(element, attrName, attrVal);
                if (childElement !=null) {
                    return childElement;
                }
            }
        }
        return null;
    }
}
