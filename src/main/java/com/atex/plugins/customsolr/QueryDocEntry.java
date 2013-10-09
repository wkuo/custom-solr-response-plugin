package com.atex.plugins.customsolr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

public class QueryDocEntry 
    extends Element
    {

    public static final String ENTRY_TYPE_STR = "str";
    public static final String ENTRY_TYPE_DATE = "date";
    protected static final String ATTR_NAME = "name";

    Map<String, Element> entry;

    public QueryDocEntry() {
        entry = new HashMap<String, Element>();
    }

    public Element getEntry(String name) {
        return entry.get(name);
    }

    public void setEntry(String name, String type, String value) {
        Element element = new Element(type);
        element.setAttribute(ATTR_NAME, name);
        element.setText(value);
        entry.put(name, element);
    }

    public List<Element> getEntries() {
        List<Element> list = new ArrayList<Element>();
        for (Object key:entry.keySet()) {
            list.add(entry.get(key));
        }
        return list;
    }


}
