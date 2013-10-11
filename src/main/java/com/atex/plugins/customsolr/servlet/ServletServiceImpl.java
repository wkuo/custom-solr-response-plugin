package com.atex.plugins.customsolr.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.atex.plugins.customsolr.util.CmUtil;
import com.atex.plugins.customsolr.util.ConfigUtil;
import com.atex.plugins.customsolr.util.SolrUtil;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.CmClient;

public abstract class ServletServiceImpl 
    extends HttpServlet
    implements SolrCmService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ServletConfig config;
    protected CmClient cmClient;
    protected ConfigUtil configUtil;
    protected SolrUtil solrUtil;
    protected String solrServerUrl;

    public void initCm() throws CMException {
    }

    public void destroyCm(){
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        try {
            initCommonCm();
            initCm();
        } catch (CMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void initCommonCm() {
        cmClient = CmUtil.getCmClientByContext(
                getWsConfig().getServletContext());
        configUtil = getConfigUtil() ;
        solrUtil = getSolrUtil();
        solrServerUrl = configUtil.getSolrServerUrl();
    }
    
    protected ServletConfig getWsConfig() {
        return config;
    }

    protected CmClient getCmClient() {
        return cmClient;
    }

    protected ConfigUtil getConfigUtil() {
        return new ConfigUtil(cmClient);
    }

    protected SolrUtil getSolrUtil() {
        return new SolrUtil(cmClient);
    }
}
