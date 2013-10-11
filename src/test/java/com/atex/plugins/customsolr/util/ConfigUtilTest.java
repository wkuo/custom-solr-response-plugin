package com.atex.plugins.customsolr.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.atex.plugins.customsolr.ConfigPolicy;
import com.polopoly.cm.client.CMException;

public class ConfigUtilTest {

    ConfigUtil target;

    @Mock
    ConfigPolicy configPolicy;

    @Before
    public void setUp() throws CMException {
        MockitoAnnotations.initMocks(this);
        target = spy(new ConfigUtil());
        doReturn(configPolicy).when(target).getConfigPolicy();
    }

    @Test
    public void testGetConfigPolicy() throws CMException {
        assertEquals(configPolicy, target.getConfigPolicy());
    }

    @Test
    public void testGetSolrServerUrl() throws CMException {
        String expected = "http://localhost:8080/solr";
        when(configPolicy.getSolrServerUrl()).thenReturn(expected);
        assertEquals(expected, target.getSolrServerUrl());
    }

}
