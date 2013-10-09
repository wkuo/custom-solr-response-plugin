package com.atex.plugins.customsolr.util;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.polopoly.application.servlet.ApplicationServletUtil;
import com.polopoly.cm.client.CmClient;
import com.polopoly.cm.client.CmClientBase;

public class CmUtil {

    public static CmClient getCmClient(FilterConfig config) {
        return getCmClientByContext(config.getServletContext());
    }

    public static CmClient getCmClient(ServletConfig config) {
        return getCmClientByContext(config.getServletContext());
    }

    public static CmClient getCmClientByContext(ServletContext context) {
        return ((CmClient) ApplicationServletUtil
                .getApplication(context)
                .getApplicationComponent(CmClientBase.DEFAULT_COMPOUND_NAME));
    }

}
