package com.atex.plugins.customsolr.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;

import com.atex.plugins.customsolr.ResponseElement;
import com.atex.plugins.customsolr.util.InputTemplateUtil;
import com.atex.plugins.customsolr.util.MapParamUtil;

public class SolrITListFilter 
    extends FilterServiceImpl 
{

    protected static final String PARAM_ALLIT = "allIt";

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        processITList(req, resp);
    }

    protected void processITList(HttpServletRequest req, HttpServletResponse resp) {
        ResponseElement root = new ResponseElement();
        Element itElements = new Element("inputTemplates");
        List<String> its = new ArrayList<String>();
        MapParamUtil paramUtil = getMapParamUtil(
                req.getParameterNames(), req.getParameterMap());
        boolean allIT = paramUtil.getBoolFromParamByKey(PARAM_ALLIT);
        if (allIT) {
            InputTemplateUtil itUtil =  new InputTemplateUtil(solrServerUrl);
            its = itUtil.getAllInputTemplates(solrUtil.getSelectedCore(req));
        } else {
            its = configUtil.getSelectedInputTemplates(solrUtil.getSelectedCore(req));
        }
        for (String it: its) {
            Element itElement = new Element(it);
            itElements.addContent(itElement);
        }
        root.addResult(itElements);
        root.write(resp);
    }

    protected MapParamUtil getMapParamUtil(Enumeration names, Map map) {
        return new MapParamUtil(names, map);
    }

}
