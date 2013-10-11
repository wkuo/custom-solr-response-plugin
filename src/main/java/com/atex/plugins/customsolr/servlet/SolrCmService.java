package com.atex.plugins.customsolr.servlet;

import com.polopoly.cm.client.CMException;

public interface SolrCmService {
    public void initCm() throws CMException;
    public void destroyCm();

}
