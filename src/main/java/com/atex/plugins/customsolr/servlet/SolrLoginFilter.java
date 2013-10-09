package com.atex.plugins.customsolr.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;

import com.atex.integration.authentication.AutoLoginFilter;
import com.atex.plugins.customsolr.ResponseElement;
import com.atex.plugins.customsolr.util.ConfigUtil;
import com.atex.plugins.customsolr.util.ResponseUtil;
import com.polopoly.application.servlet.ApplicationServletUtil;
import com.polopoly.cm.client.CmClient;
import com.polopoly.cm.client.CmClientBase;
import com.polopoly.common.lang.StringUtil;
import com.polopoly.user.server.Caller;

public class SolrLoginFilter 
extends AutoLoginFilter
{

    FilterConfig filterConfig;
    boolean autoLogin = false;

    public static final String QUERY_PARAMETER_NAME = "QueryParameterName";

    private static final Logger LOG = Logger.getLogger(SolrLoginFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        this.filterConfig = filterConfig;
        initCm();
    }

    protected void initCm() {
        CmClient cmClient = ((CmClient) ApplicationServletUtil
                .getApplication(filterConfig.getServletContext())
                .getApplicationComponent(CmClientBase.DEFAULT_COMPOUND_NAME));
        ConfigUtil configUtil = new ConfigUtil(cmClient);
        autoLogin = configUtil.isAutoLogin();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String accessCodeName = filterConfig.getInitParameter(QUERY_PARAMETER_NAME);
        // if the access code name in web fragment xml not defined then no authentication needed
        if (StringUtil.isEmpty(accessCodeName)) {
            LOG.log(Level.WARNING, "accessCodeName not found in web-fragment.xml");
            chain.doFilter(req, resp);
        }
        if (autoLogin) {
            String accessCode = req.getParameter(accessCodeName);
            if (accessCode!=null || isAlreadyLoggedInSession(req, resp)) {
                super.doFilter(req, resp, chain);
            } else {
                ResponseElement root = new ResponseElement();
                root.setStatusMsg(HttpStatus.SC_UNAUTHORIZED, "Authentication required: " + accessCodeName + " missing in parameter.");
                ResponseUtil.write(resp, root.getResponse());
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    protected boolean isAlreadyLoggedInSession(HttpServletRequest request, HttpServletResponse response) {
        try {
            Object[] result = getUserAndCallerIfPresent(request, response);
            if (result[1] != null) {
                Caller caller = (Caller)result[1];
                return caller.isLoggedIn(getUserServer());
            }
        } catch (Exception e) {
            LOG.log(Level.FINE, "Failed to validate cookie based session", e);
        }
        return false;
    }

}
