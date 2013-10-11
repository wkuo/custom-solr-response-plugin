package com.atex.plugins.customsolr.util;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.polopoly.cm.ExternalContentId;
import com.polopoly.cm.app.util.impl.ResourceBundleUtil;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.policy.Policy;
import com.polopoly.cm.policy.PolicyCMServer;
import com.polopoly.util.LocaleUtil;
import com.polopoly.util.StringUtil;

public class ContentLocaleUtil {

    private PolicyCMServer cmServer;
    private static final Logger LOG = Logger.getLogger(ContentLocaleUtil.class.getName());
    protected static final String POLOPOLY_CLIENT = "polopoly.Client";
    protected static final String LABEL = "label";

    public ContentLocaleUtil(PolicyCMServer cmServer) {
        this.cmServer = cmServer;
    }

    public String getITLabelKey(String itExtIdStr) {
        try {
            ExternalContentId itExtId = getITExtId(itExtIdStr);
            Policy itPolicy = cmServer.getPolicy(itExtId);
            String labelKey = itPolicy.getContent().getComponent(POLOPOLY_CLIENT, LABEL);
            return labelKey;
        } catch (CMException e) {
            LOG.log(Level.FINE, "Unable to get label for ", itExtIdStr);
            return "";
        }
    }

    public String getITLabel(String itExtIdStr, String prefered) {
        String itName = "";
        Locale locale = null;
        if (!StringUtil.isEmpty(prefered)) {
            locale = getLocale(prefered);
        }
        String labelKey = getITLabelKey(itExtIdStr);
        itName = getLabelByKey(labelKey, locale);
        if (!StringUtil.isEmpty(itName)) {
            return itName;
        }
        return itExtIdStr;
    }

    public String getLabelByKey(String key, Locale prefered) {
        ResourceBundleUtil rbUtil = getResourceBundleUtil();
        ResourceBundle rb = null;
        if (prefered!=null) {
            rb = rbUtil.getResourceBundle(cmServer, prefered);
        }
        if (rb==null) {
            rb = rbUtil.getDefaultResourceBundle(cmServer);
        }
        return getLocaleFormat(key, rb);
    }

    // For unit test
    protected ExternalContentId getITExtId(String itExtIdStr) {
        return new ExternalContentId(itExtIdStr);
    }

    protected Locale getLocale(String prefered) {
        return new Locale(prefered);
    }

    protected ResourceBundleUtil getResourceBundleUtil() {
        return new ResourceBundleUtil();
    }

    protected String getLocaleFormat(String key, ResourceBundle rb) {
        return LocaleUtil.format(key, rb);
    }
}
