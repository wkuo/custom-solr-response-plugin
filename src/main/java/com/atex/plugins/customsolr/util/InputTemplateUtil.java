package com.atex.plugins.customsolr.util;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class InputTemplateUtil {

    protected static final String IT_FACET_QUERY = "/select?facet=true&facet.field=inputTemplate";
    protected static final String OP_NAME = "name";
    protected static final String OP_FC_COUNT = "facet_counts";
    protected static final String OP_FC_FIELDS = "facet_fields";

    public static List<String> getAllInputTemplates(String solrServerUrl, String selectedCore) {

        List<String> fieldNames = new ArrayList<String>();

        String queryUrl = solrServerUrl + "/" + selectedCore + IT_FACET_QUERY;
        Element responseRoot = JdomUtil.getElement(queryUrl);
        List<Element> lst = responseRoot.getChildren();
        for (Element ele : lst) {
            if (OP_FC_COUNT.equalsIgnoreCase(ele.getAttributeValue(OP_NAME))) {
                List<Element> facets = ele.getChildren();
                for (Element facet : facets) {
                    if (OP_FC_FIELDS.equalsIgnoreCase(facet.getAttributeValue(OP_NAME))) {
                        Element fcField = facet.getChild("lst");
                        List<Element> its = fcField.getChildren();
                        for (Element it : its) {
                            String name = it.getAttributeValue(OP_NAME);
                            fieldNames.add(name);
                        }
                    }
                }
            }
        }
        return fieldNames;
    }
}
