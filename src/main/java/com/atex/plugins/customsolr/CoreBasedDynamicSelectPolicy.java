package com.atex.plugins.customsolr;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.polopoly.cm.ExternalContentId;
import com.polopoly.cm.app.policy.MultiValued;
import com.polopoly.cm.app.policy.SelectPolicy;
import com.polopoly.cm.app.policy.SingleValuePolicy;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.policy.Policy;
import com.polopoly.common.lang.StringUtil;

public abstract class CoreBasedDynamicSelectPolicy 
    extends SelectPolicy 
    implements MultiValued 
{

    private static final Logger LOG = Logger.getLogger(BasicFieldSelectPolicy.class.getName());
    public static final String BASICFIELD_CORE = "core";

    private ArrayList labels = new ArrayList();
    private ArrayList data = new ArrayList();
    private ArrayList images = new ArrayList();

    protected abstract void loadCustomOptions();

    @Override
    public Object[] getOptions() {
        // Copy the options into list so that can be manipulate
        Object[] parentOpts = super.getOptions();
        for (String lblObj: (String[]) parentOpts[0]) {
            addToLabels(lblObj);
        }
        for (String dataObj: (String[]) parentOpts[1]) {
            addToData(dataObj);
        }
        for (String dataObj: (String[]) parentOpts[2]) {
            addToImages(dataObj);
        }

        // Insert custom options
        loadCustomOptions();

        parentOpts[0] = labels.toArray(new String[0]);
        parentOpts[1] = data.toArray(new String[0]);
        parentOpts[2] = images.toArray(new String[0]);

        return parentOpts ;
    }

    protected boolean isExistInData(Object obj) {
        return data.contains(obj);
    }

    protected void addToLabels(Object obj) {
        labels.add(obj);
    }

    protected void addToData(Object obj) {
        data.add(obj);
    }

    protected void addToImages(Object obj) {
        images.add(obj);
    }

    /* 
     * This got to trace via parent policy.
     * Make this policy to aware the current core
    */
    public String getCoreName() throws CMException {
        String coreName = "";
        Policy parent = getParentPolicy();
        Policy namePolicy = parent.getChildPolicy(BASICFIELD_CORE);
        if (namePolicy instanceof SingleValuePolicy) {
            coreName = ((SingleValuePolicy) namePolicy).getValue();
        }
        return StringUtil.notNull(coreName, "");
    }

    protected ConfigPolicy getConfigPolicy() throws CMException {
        ExternalContentId extId = new ExternalContentId(ConfigPolicy.EXTID_CONFIG);
        ConfigPolicy configPolicy = (ConfigPolicy) getCMServer().getPolicy(extId);
        return configPolicy;
    }

    protected String getSolrServerUrl() {
        try {
            return getConfigPolicy().getSolrServerUrl();
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Error retrieving solr server url.", e);
        }
        return "";
    }
}
