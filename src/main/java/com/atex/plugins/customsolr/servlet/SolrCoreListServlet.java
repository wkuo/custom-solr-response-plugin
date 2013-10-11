package com.atex.plugins.customsolr.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.jdom.Element;

import com.atex.plugins.customsolr.ResponseElement;

public class SolrCoreListServlet 
extends ServletServiceImpl {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getCoreList(req, resp);
    }

    protected void getCoreList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ResponseElement root = new ResponseElement();
        Element coreElements = new Element("cores");
        List<String> cores = solrUtil.getSolrCores();
        for (String core:cores) {
            Element eleCore = new Element(core);
            coreElements.addContent(eleCore);
        }
        root.addResult(coreElements);
        root.setStatusMsg(HttpStatus.SC_OK, "Process Done");
        // Response the caller with modified result
        root.write(resp);
    }

}
