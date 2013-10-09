package com.atex.plugins.customsolr.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import com.atex.plugins.customsolr.util.CmUtil;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.CmClient;

public abstract class FilterServiceImpl 
    implements Filter
{
    private FilterConfig config;
    protected CmClient cmClient;

    public abstract void initCm() throws CMException;

    /* In progress of design and refactor */
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

    private void initCommonCm() {
        cmClient = CmUtil.getCmClient(config);
    }

    protected FilterConfig getWsConfig() {
        return config;
    }

    protected CmClient getCmClient() {
        return cmClient;
    }
}
