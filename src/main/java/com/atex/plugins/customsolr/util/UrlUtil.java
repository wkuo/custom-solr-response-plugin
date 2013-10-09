package com.atex.plugins.customsolr.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.atex.plugins.customsolr.ConfigPolicy;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.CmClient;
import com.polopoly.util.StringUtil;

public class UrlUtil {

    private static final Logger LOG = Logger.getLogger(UrlUtil.class.getName());
    protected ConfigPolicy configPolicy;
    
    public UrlUtil(ConfigPolicy configPolicy) {
        this.configPolicy = configPolicy;
    }

    public UrlUtil(CmClient cmClient) throws CMException {
        ConfigUtil configUtil = new ConfigUtil(cmClient);
        this.configPolicy = (ConfigPolicy) configUtil.getConfigPolicy();
    }

    public String getUriWithoutServletPath(HttpServletRequest req) {
        return getUriWithoutServletPath(req, "");
    }

    public static String getUriWithoutServletPath(HttpServletRequest req, String replacement) {
        String url = req.getRequestURI();
        String pattern = req.getServletPath();
        url = SolrStringUtil.replaceFirst(url, pattern, replacement);
        return url;
    }

    protected String getUrlStr(HttpServletRequest req, boolean includeQueryStr) {
        StringBuffer sb = new StringBuffer(
                configPolicy.getSolrServerUrl());
        String path = getUriWithoutServletPath(req);
        sb.append(path);
        if (includeQueryStr) {
            String queryString = req.getQueryString();
            if (!StringUtil.isEmpty(queryString)) {
                sb.append("?");
            } 
            sb.append(StringUtil.notNull(queryString, ""));
        }
        return sb.toString();
    }

    public URI getFullSourceURI(HttpServletRequest req, boolean includeQueryStr) {
        URI uri = null;
        try {
            uri = new URI(getUrlStr(req, includeQueryStr));
        } catch (URISyntaxException e) {
            LOG.log(Level.WARNING, "Invalid url " , e);
        }
        return uri;
    }
}
