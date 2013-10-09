package com.atex.plugins.customsolr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hamcrest.core.IsSame;

import com.atex.plugins.baseline.policy.BaselinePolicy;
import com.polopoly.cm.MajorInfo;
import com.polopoly.cm.app.impl.xml.validators.InputTemplateValidator;
import com.polopoly.cm.app.orchid.model.impl.InputTemplateListModel;
import com.polopoly.cm.app.policy.CheckboxPolicy;
import com.polopoly.cm.app.policy.DuplicatorPolicy;
import com.polopoly.cm.app.policy.DuplicatorPolicy.DuplicatorElement;
import com.polopoly.cm.app.policy.SelectPolicy;
import com.polopoly.cm.app.policy.SingleValuePolicy;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.policy.Policy;
import com.polopoly.cm.policy.PolicyCMServer;
import com.polopoly.cm.policy.PolicyUtil;
import com.polopoly.cm.policy.impl.PolicyCMServerExtended;
import com.polopoly.cm.templatemodel.impl.InputTemplateBase;
import com.polopoly.cm.templatemodel.impl.InputTemplateComponent;
import com.polopoly.cm.templatemodel.impl.InputTemplateModel;
import com.polopoly.common.lang.StringUtil;
import com.polopoly.model.pojo.cm.impl.InputTemplateModelType;
import com.polopoly.search.solr.SolrClientImpl;
import com.polopoly.search.solr.SolrServerUrl;

public class ConfigPolicy 
extends BaselinePolicy 
{

    public static final String EXTID_CONFIG = "com.atex.plugins.customsolr.CustomSolrConfigHome";

    private static final Logger LOG = Logger.getLogger(ConfigPolicy.class.getName());

    protected static final String BASICFIELD_SELECT = "basicfsel";
    protected static final String IT_SELECT = "itssel";
    protected static final String BASICFIELD_DUPLICATOR = "corebasedup";
    protected static final String AUTO_LOGIN = "autoLogin";
    protected static final String POLICY_DEF_SOLR_URL = "defaultSolrUrl";

    public List getDuplicatorElements() {
        List dupEle = new ArrayList();
        try {
            DuplicatorPolicy dupPolicy = (DuplicatorPolicy) getChildPolicy(BASICFIELD_DUPLICATOR);
            dupEle = dupPolicy.getDuplicatorElements();
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Failed to retrieve duplicator. ", e);
        }
        return dupEle;
    }

    public Policy getCoreBasePolicy(String core, String subField) {
        Policy policy = null;
        try {
            List dupEle = getDuplicatorElements();
            for (Object element: dupEle) {
                if (element instanceof DuplicatorElement) {
                    DuplicatorElement de = (DuplicatorElement) element;
                    Policy namePol = de.getChildPolicy(CoreBasedDynamicSelectPolicy.BASICFIELD_CORE);
                    String name = "";
                    if (namePol instanceof SingleValuePolicy) {
                        name = ((SingleValuePolicy) namePol).getValue();
                        name = StringUtil.notNull(name, "");
                    }
                    if (name.equalsIgnoreCase(core)) {
                        return de.getChildPolicy(subField);
                    }
                }
            }
        } catch (CMException e){
            LOG.log(Level.WARNING, "Error retrieving BasicFieldSelectPolicy for core " + core, e);
        }
        return policy;
    }

    public List<String> getBasicFields(String core) {
        try {
            CoreBasedDynamicSelectPolicy selectPolicy = (CoreBasedDynamicSelectPolicy) getCoreBasePolicy(core, BASICFIELD_SELECT);
            if (selectPolicy instanceof BasicFieldSelectPolicy) {
                return selectPolicy.getValues();
            }
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Error retrieving info from CmServer." , e);
        }
        return new ArrayList<String>();
    }

    public List<String> getInputTemplates(String core) {
        try {
            CoreBasedDynamicSelectPolicy selectPolicy = (CoreBasedDynamicSelectPolicy) getCoreBasePolicy(core, IT_SELECT);
            if (selectPolicy instanceof InputTemplateSelectPolicy) {
                return selectPolicy.getValues();
            }
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Error retrieving info from CmServer." , e);
        }
        return new ArrayList<String>();
    }

    protected String getDefaultSolrServerUrl() {
        String url = null;
        try {
            Policy policy = getChildPolicy(POLICY_DEF_SOLR_URL);
            if (policy!=null) {
                SingleValuePolicy svp = (SingleValuePolicy) policy;
                String value = svp.getValue();
                if (!StringUtil.isEmpty(value)) {
                    url = svp.getValue();
                }
            }
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Error retrieving default solr url.", e);
        }
        return url;
    }

    public String getSolrServerUrl() {
        String solrUrlString = null;
        solrUrlString = getDefaultSolrServerUrl();
        if (solrUrlString!=null) {
            return solrUrlString;
        }
        SolrServerUrl solrServerUrl = new SolrServerUrl();
        if (solrServerUrl!=null) {
            solrUrlString = solrServerUrl.getUrl();
        }
        return solrUrlString;
    }

    public boolean isAutoLogin() {
        try {
            Policy policy = getChildPolicy(AUTO_LOGIN);
            if (policy instanceof CheckboxPolicy) {
                return ((CheckboxPolicy) policy).getChecked();
            }
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Error retrieving autoLogin field." , e);
            return false;
        }
        return false;
    }

}
