package com.atex.plugins.customsolr.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;

import com.atex.plugins.customsolr.ResponseElement;
import com.atex.plugins.customsolr.util.JdomUtil;
import com.atex.plugins.customsolr.util.MapParamUtil;
import com.polopoly.cm.client.CMException;

public class SolrFieldListFilter
    extends FilterServiceImpl 
{
    private static final Logger LOG = Logger.getLogger(SolrFieldListFilter.class.getName());

    protected static final String IP_FIELDS = "fields";
    protected static final String OP_RESPONSE = "response";
    protected static final String OP_FIELDS = "fields";
    protected static final String OP_SCHEMA = "schema";
    protected static final String OP_INDEX = "index";
    protected static final String OP_FIELD = "field";
    protected static final String OP_NAME = "name";
    protected static final String OP_DTYPE = "dtype";
    protected static final String OP_IS_DYNAMIC = "isDynamic";
    protected static final String PARAM_DETAIL = "detail";
    protected static final String PARAM_ALLFIELD = "allField";
    protected static final String CONTENTTYPE_XML = "application/xml";
    protected static final String LUKE_ADMIN = "/admin/luke";

    private Boolean detail;
    private Boolean allField;
    private String selectedCore;
    private Map<String, Map> outputMap;

    @Override
    public void initCm() throws CMException {
        outputMap = new HashMap<String, Map>();
        detail = false;
        allField = false;
    }

    @Override
    public void destroyCm() {
        outputMap = null;
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        solrFieldListing(req, resp);
    }

    protected void solrFieldListing(HttpServletRequest req, HttpServletResponse resp) {
        ResponseElement root = new ResponseElement();
        selectedCore = solrUtil.getSelectedCore(req);
        MapParamUtil paramUtil = new MapParamUtil(
                req.getParameterNames(), req.getParameterMap());
        detail = paramUtil.getBoolFromParamByKey(PARAM_DETAIL);
        allField = paramUtil.getBoolFromParamByKey(PARAM_ALLFIELD);

        if (selectedCore !=null) {
            Element eleCore = new Element(selectedCore);
            root.addResult(eleCore);
            String lukeUrl = solrServerUrl +"/" +selectedCore + LUKE_ADMIN;
            eleCore.addContent(readLukeRespose(lukeUrl));
        } else {
            // if no core defined or core not found, then loop all the cores
            List<String> cores = solrUtil.getSolrCores();
            for (String core : cores) {
                Element eleCore = new Element(core);
                selectedCore = core;
                root.addResult(eleCore);
                String lukeUrl = solrServerUrl +"/" +core + LUKE_ADMIN;
                eleCore.addContent(readLukeRespose(lukeUrl));
            }
        }
            // Response the caller with result

        root.write(resp);
    }

    protected Element readLukeRespose(String lukeUrl) {
        List<String> basic = new ArrayList<String>();
        basic = configUtil.getBasicFields(selectedCore);
        Element lukeRoot = JdomUtil.getElement(lukeUrl);
        List<Element> lst = lukeRoot.getChildren();
        for (Element ele: lst) {
            if (IP_FIELDS.equalsIgnoreCase(ele.getAttributeValue(OP_NAME))) {
                List sourceFields = ele.getChildren();
                for (Object fieldObj: sourceFields) {
                    Element field = (Element) fieldObj;
                    String name = field.getAttributeValue(OP_NAME);
                    String index = "";
                    String schema = "";
                    String type = "";
                    Boolean dynamic = false;
                    
                    /*
                     * The luke response differentiate 
                     * by attributes value of attribute name "name"
                    */
                    for (Object fieldAttrObj: field.getChildren()) {
                        Element fieldAttr = (Element) fieldAttrObj;
                        String attrName = fieldAttr.getAttributeValue(OP_NAME);
                        if ("type".equalsIgnoreCase(attrName) ) {
                            type = fieldAttr.getText();
                        } else if ("dynamicBase".equalsIgnoreCase(attrName)) {
                            dynamic = true;
                        } else if (OP_INDEX.equalsIgnoreCase(attrName)) {
                            index = fieldAttr.getText();
                        } else if (OP_SCHEMA.equalsIgnoreCase(attrName)) {
                            schema = fieldAttr.getText();
                        }
                    }
                    if (!allField) {
                        if (basic.contains(name)) {
                            loadToMap(name, type, dynamic.toString(), index, schema);
                        }
                    } else {
                        loadToMap(name, type, dynamic.toString(), index, schema);
                    }
                }
            }
        }
        return mapToElement();
    }

    protected void loadToMap(String... arg) {
        Map<String, String> attrs = new HashMap<String, String>(); 
        attrs.put(OP_DTYPE, arg[1]);
        if (detail) {
            attrs.put(OP_IS_DYNAMIC, arg[2]);
            attrs.put(OP_INDEX, arg[3]);
            attrs.put(OP_SCHEMA, arg[4]);
        }
        outputMap.put(arg[0], attrs);
    }

    protected Element mapToElement() {
        Element fields = new Element(OP_FIELDS);
        for (String key: outputMap.keySet()){
            Element field = new Element(OP_FIELD);
            field.addContent(processFieldEntry(OP_NAME, key));
            Map<String, String> subMap = outputMap.get(key);
            for (String subKey: subMap.keySet()) {
                Element subElement = processFieldEntry(subKey, subMap.get(subKey));
                    field.addContent(
                    subElement
                    );
            }
            fields.addContent(field);
        }
        return fields;
    }

    protected Element processField(String... arg) {
        Element field = new Element(OP_FIELD);
        field.addContent(processFieldEntry(OP_NAME, arg[0]));
        field.addContent(processFieldEntry(OP_DTYPE, arg[1]));
        if (detail) {
            field.addContent(processFieldEntry(OP_IS_DYNAMIC, arg[2]));
            field.addContent(processFieldEntry(OP_INDEX, arg[3]));
            field.addContent(processFieldEntry(OP_SCHEMA, arg[4]));
        }
        return field;
    }

    protected Element processFieldEntry(String name, String value) {
        Element entry = new Element(name);
        entry.setText(value);
        return entry;
    }

}
