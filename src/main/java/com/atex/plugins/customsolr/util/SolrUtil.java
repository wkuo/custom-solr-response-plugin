package com.atex.plugins.customsolr.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;

import com.atex.plugins.customsolr.ConfigPolicy;
import com.polopoly.cm.client.CmClient;

public class SolrUtil {

    private static final Logger LOG = Logger.getLogger(SolrUtil.class.getName());
    private ConfigPolicy configPolicy;
    private String solrUrl = "";

    public SolrUtil(CmClient cmClient) {
        ConfigUtil configUtil = getConfigUtil(cmClient);
        configPolicy = configUtil.getConfigPolicy();
        solrUrl = configPolicy.getSolrServerUrl();
    }

    public SolrUtil(ConfigPolicy configPolicy) {
        this.configPolicy = configPolicy;
        solrUrl = configPolicy.getSolrServerUrl();
    }

    public List<String> getSolrCores() {
        List<String> result = new ArrayList<String>();
        try {
            SolrServer solrServer = getHttpSolrServer();
            CoreAdminRequest caReq = getCoreAdminReq();
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

    public String getSelectedCore(HttpServletRequest req) {
        List<String> cores = getSolrCores();
        StringBuffer url = req.getRequestURL();
        for (String core: cores) {
            if (url.indexOf(core)!= -1) {
                return core;
            }
        }
        return null;
    }

    protected ConfigUtil getConfigUtil(CmClient cmClient) {
        return new ConfigUtil(cmClient);
    }

    protected HttpSolrServer getHttpSolrServer() {
        return new HttpSolrServer(solrUrl);
    }

    protected CoreAdminRequest getCoreAdminReq() {
        return new CoreAdminRequest();
    }
}
