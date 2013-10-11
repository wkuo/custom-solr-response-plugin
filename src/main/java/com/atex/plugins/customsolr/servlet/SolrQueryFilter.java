package com.atex.plugins.customsolr.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;

import com.atex.plugins.customsolr.QueryDocEntry;
import com.atex.plugins.customsolr.util.ContentUtil;
import com.atex.plugins.customsolr.util.ConvertionUtil;
import com.atex.plugins.customsolr.util.JdomUtil;
import com.atex.plugins.customsolr.util.MapParamUtil;
import com.atex.plugins.customsolr.util.ResponseUtil;
import com.atex.plugins.customsolr.util.UrlUtil;
import com.polopoly.cm.client.CMException;
import com.polopoly.util.StringUtil;

public class SolrQueryFilter 
    extends FilterServiceImpl
{

    protected static final String CONTENT_TYPE = "application/xml";
    protected static final String ATTR_NAME = "name";
    protected static final String ATTR_REL = "rel";
    protected static final String FN_CONTENTID = "contentId";
    protected static final String TAG_RESP_RESULT = "result";
    protected static final String TAG_RESP_DOC = "doc";
    protected static final String TAG_RESP_NUMFOUND = "numFound";
    protected static final Integer DEF_PAGE = 1 ;
    protected static final Integer DEF_PAGE_START = 0;
    protected static final Integer DEF_PAGE_LIMIT = 10;
    protected static final String PARAM_KEY_PAGE = "page";
    protected static final String PARAM_KEY_START = "start";
    protected static final String PARAM_KEY_LIMIT = "rows";
    protected static final String ESC_CHAR = "amp;|amp%3B";
    protected static final String PAGE_IND_FIRST = "first";
    protected static final String PAGE_IND_PREV = "previous";
    protected static final String PAGE_IND_NEXT = "next";

    private static final Logger LOG = Logger.getLogger(SolrQueryFilter.class.getName());

    private MapParamUtil paramUtil;
    private UrlUtil urlUtil;
    private List<String> allowedIT;

    @Override
    public void initCm() throws CMException {
      urlUtil = new UrlUtil(cmClient);
    }

    @Override
    public void destroyCm() {
        paramUtil = null;
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        try {
            initCm();
            initParamUtil(req);
            allowedIT = configUtil.getSelectedInputTemplates(solrUtil.getSelectedCore(req));
            solrQueryProcessing(req, resp);
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Error retrieving info from CmServer." , e);
        }
    }

    protected void initParamUtil(HttpServletRequest req) {
        paramUtil =  new MapParamUtil(req.getParameterNames(), req.getParameterMap());
    }

    protected void loadFilterQueryParam() {
        LOG.log(Level.INFO, "fq is " + paramUtil.getStringFromParamByKey("fq"));
        if (StringUtil.isEmpty(paramUtil.getStringFromParamByKey("fq"))) {
            paramUtil.applyITFilter(allowedIT);
        }
        LOG.log(Level.INFO, "allowIt  = " + allowedIT);
    }

    protected void solrQueryProcessing(HttpServletRequest req, HttpServletResponse resp) {
        ContentUtil contentUtil = new ContentUtil(cmClient);
        loadFilterQueryParam();
        String fullUrl = getFullSolrUrlStr(req);
        Element rootNode = JdomUtil.getElement(fullUrl);
        Element resultsNode = rootNode.getChild(TAG_RESP_RESULT);
        Integer numFound = ConvertionUtil.strToInt(resultsNode.getAttributeValue(TAG_RESP_NUMFOUND));
        String preferLang = req.getParameter("lang");

        List docs = resultsNode.getChildren(TAG_RESP_DOC);
        int entryCount = 0;
        for(Object docObj:docs) {
            Element doc = (Element) docObj;
            QueryDocEntry de = new QueryDocEntry();
            for (Object dcObj :doc.getChildren()) {
                Element docChild = (Element) dcObj;
                if (FN_CONTENTID.equalsIgnoreCase(docChild.getAttributeValue(ATTR_NAME))){
                    String id = docChild.getText();
                    de = contentUtil.getDocEntryById(id, preferLang);
                }
            }
            for (Element entry:de.getEntries()){
                doc.addContent(entry);
            }
            entryCount++;
        }
        for (Element pageLinks: getPageLinks(req, entryCount, numFound)) {
            rootNode.addContent(pageLinks);
        }
        // Response the caller with modified result
        ResponseUtil.write(resp, rootNode);
    }

    protected String getFullSolrUrlStr(HttpServletRequest req) {
        StringBuffer url = new StringBuffer(
                urlUtil.getFullSourceURI(req, false).toString());
        url.append(paramUtil.processParamString(
                ));
        return url.toString();
    }

    protected List<Element> getPageLinks(HttpServletRequest req, Integer entryCount, Integer totalFound) {
        List<Element> output = new ArrayList<Element>();
        Integer page = paramUtil.getIntFromParamByKey(PARAM_KEY_PAGE);
        Integer limit = paramUtil.getIntFromParamByKey(PARAM_KEY_LIMIT);
        if (limit==null) {
            limit = DEF_PAGE_LIMIT;
        }
        if (page==null || page<=0) {
            page = DEF_PAGE;
        }
        output.add(getPageLink(req, 1, limit, PAGE_IND_FIRST));
        if (page>=2 
                && ( totalFound >= ( (page - 1 ) * limit) || limit > totalFound) ) {
            output.add(getPageLink(req, page -1, limit, PAGE_IND_PREV));
        }
        if (entryCount<=limit 
                && ( totalFound > ( page * limit) ) ) {
            output.add(getPageLink(req, page + 1, limit, PAGE_IND_NEXT));
        }
        return output;
    }

    protected Element getPageLink(HttpServletRequest req, Integer page, Integer limit, String identity) {
        Element link = new Element("link");
        link.setAttribute(ATTR_REL, identity);
        StringBuffer linkStr = new StringBuffer(req.getRequestURI()) ;
        linkStr.append(paramUtil.processPageParamString(page, limit, null));
        link.setText(linkStr.toString());
        return link;
    }

}
