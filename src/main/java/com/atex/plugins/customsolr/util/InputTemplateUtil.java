package com.atex.plugins.customsolr.util;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class InputTemplateUtil {

    protected static final String FC_FIELD = "inputTemplate";
    protected static final String IT_FACET_QUERY = "/select?facet=true&facet.field=" + FC_FIELD;
    protected static final String OP_NAME = "name";
    private String solrServerUrl;

    public InputTemplateUtil(String solrServerUrl) {
        this.solrServerUrl = solrServerUrl;
    }

    public List<String> getAllInputTemplates(String selectedCore) {
        List<String> fieldNames = new ArrayList<String>();
        String queryUrl = getFqUrl(selectedCore);
        Element responseRoot = getJdomElement(queryUrl);
        Element fcField = getElementByAttr(responseRoot); 
        if (fcField!=null) {
            List<?> its = fcField.getChildren();
            for (Object obj : its) {
                Element it = (Element) obj;
                String name = it.getAttributeValue(OP_NAME);
                fieldNames.add(name);
            }
        }
        return fieldNames;
    }

    protected String getFqUrl(String selectedCore) {
        return solrServerUrl + "/" + selectedCore + IT_FACET_QUERY;
    }

    protected Element getJdomElement(String queryUrl) {
        return JdomUtil.getElement(queryUrl);
    }

    protected Element getElementByAttr(Element root) {
        return ElementAttrUtil.getElementByAttr(root, OP_NAME, FC_FIELD);
    }
}
