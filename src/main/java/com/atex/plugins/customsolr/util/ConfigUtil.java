package com.atex.plugins.customsolr.util;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.atex.plugins.customsolr.ConfigPolicy;
import com.polopoly.cm.ExternalContentId;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.CmClient;
import com.polopoly.cm.policy.Policy;
import com.polopoly.cm.policy.PolicyCMServer;

public class ConfigUtil 
{

    private static final Logger LOG = Logger.getLogger(ConfigUtil.class.getName());
    private PolicyCMServer cmServer;
    private ConfigPolicy configPolicy;


    public ConfigUtil(CmClient cmClient) {
        cmServer = cmClient.getPolicyCMServer();
        try {
            configPolicy = (ConfigPolicy) getConfigPolicy();
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Failed to retrieve configuration policy from CM");
        }
    }

    public Policy getConfigPolicy() throws CMException {
        ExternalContentId extId = new ExternalContentId(ConfigPolicy.EXTID_CONFIG);
        Policy policy = cmServer.getPolicy(extId);
        return policy;
    }

    public String getSolrServerUrl() {
        return configPolicy.getSolrServerUrl();
    }

    public List<String> getSelectedInputTemplates(String core) {
        return configPolicy.getInputTemplates(core);
    }

    public List<String> getBasicFields(String core) {
        return configPolicy.getBasicFields(core);
    }

    public boolean isAutoLogin() {
        return configPolicy.isAutoLogin();
    }
}
