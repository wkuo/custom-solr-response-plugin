package com.atex.plugins.customsolr.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.jdom.Element;

import com.atex.plugins.customsolr.ConfigPolicy;
import com.atex.plugins.customsolr.ResponseElement;
import com.atex.plugins.customsolr.util.ConfigUtil;
import com.atex.plugins.customsolr.util.SolrUtil;
import com.polopoly.application.servlet.ApplicationServletUtil;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.CmClient;
import com.polopoly.cm.client.CmClientBase;

public class SolrCoreListServlet 
extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SolrCoreListServlet.class.getName());

    protected ServletConfig config;
    protected SolrUtil solrUtil ;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        super.init(config);
    }

    protected void initCm() throws CMException {
            CmClient cmClient = ((CmClient) ApplicationServletUtil
                    .getApplication(config.getServletContext())
                    .getApplicationComponent(CmClientBase.DEFAULT_COMPOUND_NAME));
            solrUtil =  new SolrUtil(cmClient);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getCoreList(req, resp);
    }

    protected void getCoreList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ResponseElement root = new ResponseElement();
        Element coreElements = new Element("cores");
        try{
            initCm();
            List<String> cores = solrUtil.getSolrCores();
            for (String core:cores) {
                Element eleCore = new Element(core);
                coreElements.addContent(eleCore);
            }
            root.addResult(coreElements);
            root.setStatusMsg(HttpStatus.SC_OK, "Process Done");
        } catch (CMException e) {
            root.setStatusMsg(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Error retrieving info. ");
            LOG.log(Level.WARNING, "Error retrieving info from CmServer.", e);
        }
        // Response the caller with modified result
        root.write(resp);
    }
}
