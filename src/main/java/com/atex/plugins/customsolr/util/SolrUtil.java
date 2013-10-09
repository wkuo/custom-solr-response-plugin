package com.atex.plugins.customsolr.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;

import com.atex.plugins.customsolr.ConfigPolicy;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.CmClient;

public class SolrUtil {

    private static final Logger LOG = Logger.getLogger(SolrUtil.class.getName());
    private String solrUrl = "";

    public SolrUtil(CmClient cmClient) throws CMException {
        ConfigUtil configUtil = new ConfigUtil(cmClient);
        ConfigPolicy configPolicy;
        configPolicy = (ConfigPolicy) configUtil.getConfigPolicy();
        solrUrl = configPolicy.getSolrServerUrl();
    }

    public SolrUtil(ConfigPolicy configPolicy) {
        solrUrl = configPolicy.getSolrServerUrl();
    }

//    public static List<String> getSolrCores(ConfigPolicy configPolicy) {
//        return new SolrUtil(configPolicy).getSolrCores();
//    }

    public List<String> getSolrCores() {
        List<String> result = new ArrayList<String>();
        try {
            SolrServer solrServer = new HttpSolrServer(solrUrl);
            CoreAdminRequest caReq = new CoreAdminRequest();
            caReq.setAction(CoreAdminAction.STATUS);
            CoreAdminResponse caResp = caReq.process(solrServer);
            NamedList<?> cores = caResp.getCoreStatus();
            for (int i=0; i<cores.size();i++) {
                String core = cores.getName(i);
                result.add(core);
            }
        } catch (SolrServerException e) {
            LOG.log(Level.WARNING, "Error getting response from solr server " + solrUrl , e );
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error getting response from solr server " + solrUrl , e );
        }
        return result;
    }

}
