package com.atex.plugins.customsolr.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.atex.plugins.customsolr.ConfigPolicy;

public class UrlUtilTest {

    UrlUtil target;

    @Mock
    HttpServletRequest req;
    @Mock
    ConfigPolicy configPolicy;

    String servletPath = "/solr";
    String urlWithoutPath = "/fieldList/public";
    String reqUriStr = servletPath + urlWithoutPath;
    String solrUrl = "http://localhost:8080/solr";
    String queryStr = "allFields=true";
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        target = spy(new UrlUtil(configPolicy));
        when(req.getRequestURI()).thenReturn(reqUriStr);
        when(req.getServletPath()).thenReturn(servletPath);
        when(req.getQueryString()).thenReturn(queryStr);
        when(configPolicy.getSolrServerUrl()).thenReturn(solrUrl);
    }

    @Test
    public void testGetUrlStr() {
        String expected = solrUrl + urlWithoutPath;
        String expectedQuery = expected + "?" + queryStr;
        assertEquals(expected, target.getUrlStr(req, false));
        assertEquals(expectedQuery, target.getUrlStr(req, true));
    }

    @Test
    public void testGetFullSourceURI() throws URISyntaxException {
        String url = solrUrl + urlWithoutPath;
        String urlQuery = url + "?" + queryStr;
        URI expected = new URI(url);
        URI expectedQuery = new URI(urlQuery);
        assertEquals(expected,target.getFullSourceURI(req, false));
        assertEquals(expectedQuery, target.getFullSourceURI(req, true));
    }

    @Test
    public void testGetUriWithoutServletPath() {
        String expected = urlWithoutPath;
        assertEquals(expected, target.getUriWithoutServletPath(req));
    }

    @Test
    public void testGetUriWithoutServletPathReplace() {
        String expected = "custom" + urlWithoutPath;
        assertEquals(expected, UrlUtil.getUriWithoutServletPath(req, "custom"));
    }

}
