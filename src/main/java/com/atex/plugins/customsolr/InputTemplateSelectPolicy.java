package com.atex.plugins.customsolr;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.atex.plugins.customsolr.util.InputTemplateUtil;
import com.polopoly.cm.client.CMException;
import com.polopoly.util.StringUtil;


public class InputTemplateSelectPolicy 
    extends CoreBasedDynamicSelectPolicy
{

    private static final Logger LOG = Logger.getLogger(InputTemplateSelectPolicy.class.getName());

    protected void loadCustomOptions() {
        String core = "";
        try {
            core = getCoreName();
            if (!StringUtil.isEmpty(core)) {
                InputTemplateUtil itUtil =  new InputTemplateUtil(getSolrServerUrl());
                List<String> solrITs = itUtil.getAllInputTemplates(core);
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
