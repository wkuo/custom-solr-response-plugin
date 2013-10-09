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

    public ContentLocaleUtil(PolicyCMServer cmServer) {
        this.cmServer = cmServer;
    }

    public String getITLabelKey(String itExtIdStr) {
        try {
            ExternalContentId itExtId = new ExternalContentId(itExtIdStr);
            Policy itPolicy = cmServer.getPolicy(itExtId);
            String labelKey = itPolicy.getContent().getComponent("polopoly.Client", "label");
            return labelKey;
        } catch (CMException e) {
            return "";
        }
    }

    public String getITLabel(String itExtIdStr, String prefered) {
        String itName = "";
        Locale locale = null;
        if (!StringUtil.isEmpty(prefered)) {
            locale = new Locale(prefered);
        }
        String labelKey = getITLabelKey(itExtIdStr);
        itName = getLabelByKey(labelKey, locale);
        if (!StringUtil.isEmpty(itName)) {
            return itName;
        }
        return itExtIdStr;
    }

    public String getLabelByKey(String key, Locale prefered) {
        ResourceBundleUtil rbUtil = new ResourceBundleUtil();
        ResourceBundle rb = null;
        if (prefered!=null) {
            rb = rbUtil.getResourceBundle(cmServer, prefered);
            LOG.log(Level.INFO, "Prefered is null");
        }
        if (rb==null) {
            rb = rbUtil.getDefaultResourceBundle(cmServer);
            LOG.log(Level.INFO, "RB is null before and after is " + rb);
        }
        return LocaleUtil.format(key, rb);
    }

}
