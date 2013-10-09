package com.atex.plugins.customsolr;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.atex.plugins.customsolr.util.ContentLocaleUtil;
import com.atex.plugins.customsolr.util.InputTemplateUtil;
import com.polopoly.cm.client.CMException;
import com.polopoly.util.StringUtil;


public class InputTemplateSelectPolicy 
    extends CoreBasedDynamicSelectPolicy
{

    private static final Logger LOG = Logger.getLogger(InputTemplateSelectPolicy.class.getName());

    protected void loadCustomOptions() {
        String core = "";
        ContentLocaleUtil cLocaleUtil = new ContentLocaleUtil(getCMServer());
        try {
            core = getCoreName();
            if (!StringUtil.isEmpty(core)) {
                List<String> solrITs = InputTemplateUtil.getAllInputTemplates(getSolrServerUrl(), core);
                for (String itExtIdStr:solrITs) {
                    if (!isExistInData(itExtIdStr)) {
                        addToData(itExtIdStr);
                        addToLabels(itExtIdStr);
                        addToImages(null);
                    }
                }
            }
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Error retrieving input templates for core " + core, e);
        }
    }

    
}
