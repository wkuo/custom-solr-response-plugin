package com.atex.plugins.customsolr.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atex.plugins.customsolr.util.CmUtil;
import com.atex.plugins.customsolr.util.ConfigUtil;
import com.atex.plugins.customsolr.util.SolrUtil;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.CmClient;

public abstract class FilterServiceImpl 
    implements Filter, SolrCmService
{
    private FilterConfig config;
    protected CmClient cmClient;
    protected ConfigUtil configUtil;
    protected SolrUtil solrUtil;
    protected String solrServerUrl;

    public abstract void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException;

    public void initCm() throws CMException{
    }

    public void destroyCm(){
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.config = config;
        try {
            initCommonCm();
            initCm();
        } catch (CMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void initCommonCm() {
        cmClient = CmUtil.getCmClientByContext(
                getServletContext());
        configUtil = getConfigUtil() ;
        solrUtil = getSolrUtil();
        solrServerUrl = configUtil.getSolrServerUrl();
    }

    @Override
    public void destroy() {
        cmClient = null;
        configUtil = null;
        config = null;
        destroyCm();
    }


    @Override
    public void doFilter(ServletRequest sReq, 
            ServletResponse sResp, FilterChain chain) 
                    throws IOException ,ServletException {
        doFilter((HttpServletRequest) sReq, 
                (HttpServletResponse) sResp, 
                chain);
    };

    protected FilterConfig getWsConfig() {
        return config;
    }

    protected ServletContext getServletContext() {
        return getWsConfig().getServletContext();
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
