package com.atex.plugins.customsolr.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolrStringUtilTest {

    @Test
    public void testReplaceFirst() {
        String origin = "/abc/def/efg/hij/k.lm";
        String expected = "/abc/def/g/hij/k.lm";
        assertEquals(expected, 
                SolrStringUtil.replaceFirst(origin, "efg", "g")
                );
        
        expected = "/solr/def/efg/hij/k.lm";
        assertEquals(expected, 
                SolrStringUtil.replaceFirst(origin, "/abc", "/solr")
                );
    }

}
