package com.atex.plugins.customsolr.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.polopoly.application.servlet.ApplicationServletUtil;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.CmClient;
import com.polopoly.cm.client.CmClientBase;

public abstract class ServletServiceImpl 
    extends HttpServlet
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ServletConfig config;
    protected CmClient cmClient;

    public abstract void initCm() throws CMException;

    /* In progress of design and refactor */
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

    private void initCommonCm() {
        cmClient = ((CmClient) ApplicationServletUtil
                .getApplication(config.getServletContext())
                .getApplicationComponent(CmClientBase.DEFAULT_COMPOUND_NAME));
    }
    
    protected ServletConfig getWsConfig() {
        return config;
    }

    protected CmClient getCmClient() {
        return cmClient;
    }
}
