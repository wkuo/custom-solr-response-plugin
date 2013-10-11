package com.atex.plugins.customsolr.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.util.NamedList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.atex.plugins.customsolr.ConfigPolicy;
import com.polopoly.cm.client.CmClient;

public class SolrUtilTest {

    SolrUtil target;

    @Mock
    CmClient cmClient;
    @Mock
    ConfigPolicy configPolicy;
    @Mock
    HttpServletRequest req;
    @Mock
    HttpSolrServer httpSolrServer;
    @Mock
    CoreAdminRequest caReq;
    @Mock
    CoreAdminResponse caResp;
    
    List<String> cores = new ArrayList<String>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(configPolicy.getSolrServerUrl()).thenReturn("http://localhost:8080/solrd");
        target = spy(new SolrUtil(configPolicy));
        cores.add("internal");
        cores.add("public");
    }

    @Test
    public void testGetSolrCores() throws SolrServerException, IOException {
        NamedList<NamedList<Object>> coresnl = new NamedList<NamedList<Object>>();
        coresnl.add("internal", null);
        coresnl.add("public", null);
        doReturn(httpSolrServer).when(target).getHttpSolrServer();
        doReturn(caReq).when(target).getCoreAdminReq();
        doReturn(caResp).when(caReq).process(httpSolrServer);
        when(caResp.getCoreStatus()).thenReturn(coresnl);
        assertEquals(cores.size(), target.getSolrCores().size());
    }

    @Test
    public void testGetSelectedCore() {
        String expected = "public";
        StringBuffer sb = new StringBuffer("http://localhost:8080/solr/public/admin");
        doReturn(cores).when(target).getSolrCores();
        when(req.getRequestURL()).thenReturn(sb);
        assertEquals(expected, target.getSelectedCore(req));
    }
}
