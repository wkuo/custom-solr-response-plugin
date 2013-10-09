package com.atex.plugins.customsolr.util;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.atex.plugins.customsolr.QueryDocEntry;
import com.polopoly.cm.ContentId;
import com.polopoly.cm.ContentInfo;
import com.polopoly.cm.VersionedContentId;
import com.polopoly.cm.app.policy.SingleValuePolicy;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.CmClient;
import com.polopoly.cm.policy.Policy;
import com.polopoly.cm.policy.PolicyCMServer;
import com.polopoly.util.StringUtil;

public class ContentUtil {

    private PolicyCMServer cmServer;
    private static final Logger LOG = Logger.getLogger(ContentUtil.class.getName());

    protected static final String FN_TITLE = "title";
    protected static final String FN_IT = "inputTemplate";
    protected static final String FN_LEAD = "lead";
    protected static final String FN_CREATEDDATE = "createdDate";
    protected static final String FN_TYPE = "type";
    protected static final String[] LOCALE_EXTID_STR = { "p.GuiLanguages", "extra.GuiLanguages"};

    public ContentUtil(CmClient cmClient) {
        this.cmServer = cmClient.getPolicyCMServer();
    }

    // When less or more field, change this method.
    public QueryDocEntry getDocEntryById(String contentId, String preferLang) {
        QueryDocEntry de =  new QueryDocEntry();
        Policy policy = getPolicyById(contentId);
        ContentLocaleUtil cLocaleUtil = new ContentLocaleUtil(cmServer);
        try {
            de.setEntry(FN_TITLE, QueryDocEntry.ENTRY_TYPE_STR,getChildValue(policy, "name"));
            de.setEntry(FN_LEAD, QueryDocEntry.ENTRY_TYPE_STR, getChildValue(policy, "lead"));
            de.setEntry(FN_CREATEDDATE, QueryDocEntry.ENTRY_TYPE_DATE,
                DateFormatUtil.formatUTC(
                    getCreated(
                        policy.getContentId() )));
            String itString = policy.getInputTemplate().getExternalId().getExternalId();
            de.setEntry(FN_IT, QueryDocEntry.ENTRY_TYPE_STR, itString);
            String itName =  cLocaleUtil.getITLabel(itString, preferLang);
            de.setEntry(FN_TYPE, QueryDocEntry.ENTRY_TYPE_STR, itName);
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Fail to retrive extra info ", e);
        }
        return de;
    }

    protected String getChildValue(Policy policy, String childName) throws CMException {
        Policy namePol = policy.getChildPolicy(childName);
        if (namePol instanceof SingleValuePolicy) {
            SingleValuePolicy svp = (SingleValuePolicy) namePol;
            return svp.getValue();
        }
        return "";
    }

    protected Date getCreated(VersionedContentId versionedContentId)
        throws CMException
    {
        ContentInfo info = cmServer.getContentInfo(versionedContentId);
        return new Date(info.getCreationTime());
    }

    protected Policy getPolicyById(String contentId) {
        return getPolicyById(getContentIdFromString(contentId));
    }

    protected ContentId getContentIdFromString(String contentId) {
        ContentId cid = null;
        if (StringUtil.isEmpty(contentId)) {
            return cid;
        }
        String[] id = contentId.split("\\.");
        if (id.length>1) {
            try {
                int major = Integer.valueOf(id[0]);
                int minor = Integer.valueOf(id[1]);
                cid = new ContentId(major, minor);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return cid;
            }
        }
        return cid;
    }

    protected Policy getPolicyById(ContentId contentId) {
        Policy policy = null;
        try {
             policy = cmServer.getPolicy(contentId);
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Fail to retrieve policy " , e);
        }
        return policy;
    }

}
