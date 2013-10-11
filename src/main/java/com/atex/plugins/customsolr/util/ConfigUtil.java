package com.atex.plugins.customsolr.util;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.atex.plugins.customsolr.ConfigPolicy;
import com.polopoly.cm.ExternalContentId;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.CmClient;
import com.polopoly.cm.policy.PolicyCMServer;

public class ConfigUtil 
{

    private static final Logger LOG = Logger.getLogger(ConfigUtil.class.getName());
    private PolicyCMServer cmServer;
    private ConfigPolicy configPolicy;

    protected ConfigUtil() {
    }

    public ConfigUtil(CmClient cmClient) {
        cmServer = cmClient.getPolicyCMServer();
        try {
            ExternalContentId extId = getConfigPolicyExtId();
            configPolicy = (ConfigPolicy) cmServer.getPolicy(extId);
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Failed to retrieve configuration policy from CM");
        }
    }

    public ConfigPolicy getConfigPolicy() {
        return configPolicy;
    }

    protected ExternalContentId getConfigPolicyExtId() {
        return new ExternalContentId(ConfigPolicy.EXTID_CONFIG);
    }

    public String getSolrServerUrl() {
        return getConfigPolicy().getSolrServerUrl();
    }

    public List<String> getSelectedInputTemplates(String core) {
        return getConfigPolicy().getInputTemplates(core);
    }

    public List<String> getBasicFields(String core) {
        return getConfigPolicy().getBasicFields(core);
    }

    public boolean isAutoLogin() {
        return getConfigPolicy().isAutoLogin();
    }
}
