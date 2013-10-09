package com.atex.plugins.customsolr.widget;

import com.atex.plugins.customsolr.CoreBasedDynamicSelectPolicy;
import com.atex.plugins.customsolr.util.ContentLocaleUtil;
import com.polopoly.cm.app.widget.OSelectPolicyWidget;
import com.polopoly.cm.policy.PolicyCMServer;
import com.polopoly.orchid.OrchidException;
import com.polopoly.orchid.context.OrchidContext;
import com.polopoly.util.LocaleUtil;
import com.polopoly.util.StringUtil;

public class OCoreBaseITSelectPolicyWidget 
    extends OSelectPolicyWidget 
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void initSelf(OrchidContext oc) throws OrchidException
    {
        super.initSelf(oc);
        PolicyCMServer cmServer = getContentSession().getPolicyCMServer();
        CoreBasedDynamicSelectPolicy policy = (CoreBasedDynamicSelectPolicy) getSelectPolicy();
        selectWidget.setMultiValued(policy.getMultiValued());
        Object[] optionData = policy.getOptions();
        String[] labels = (String[])optionData[0];
        ContentLocaleUtil cLocaleUtil = new ContentLocaleUtil(cmServer);
        for (int i = 0; i < labels.length; i++) {
            String label = LocaleUtil.format(
                cLocaleUtil.getITLabelKey(labels[i]), 
                oc.getMessageBundle());
            if (!StringUtil.isEmpty(label)) {
                labels[i] = label;
            }
        }
        selectWidget.setOptions(labels, (String[])optionData[1]);
    }

}
