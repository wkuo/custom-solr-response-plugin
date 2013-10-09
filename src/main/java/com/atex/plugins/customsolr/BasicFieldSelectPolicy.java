package com.atex.plugins.customsolr;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.atex.plugins.customsolr.util.FieldUtil;
import com.polopoly.cm.app.policy.MultiValued;
import com.polopoly.cm.client.CMException;
import com.polopoly.common.lang.StringUtil;

public class BasicFieldSelectPolicy 
    extends CoreBasedDynamicSelectPolicy 
    implements MultiValued 
{
    private static final Logger LOG = Logger.getLogger(BasicFieldSelectPolicy.class.getName());

    @Override
    protected void loadCustomOptions() {
        String core = "";
        try {
            core = getCoreName();
            if (!StringUtil.isEmpty(core)) {
                List<String> solrFields = FieldUtil.getAllFieldNames(getSolrServerUrl(), core);
                for (String fieldName:solrFields) {
                    if (!isExistInData(fieldName)) {
                        addToData(fieldName);
                        addToLabels(fieldName);
                        addToImages(null);
                    }
                }
            }
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Error retrieving solr field for core " + core, e);
        }
    }

}
