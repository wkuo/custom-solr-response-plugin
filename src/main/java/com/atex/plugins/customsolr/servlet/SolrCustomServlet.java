package com.atex.plugins.customsolr.servlet;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.atex.plugins.customsolr.util.UrlUtil;
import com.polopoly.cm.client.CMException;

public class SolrCustomServlet 
extends ServletServiceImpl 
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(SolrCustomServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleMiscRequest(req, resp);
    }

    protected void handleMiscRequest(HttpServletRequest req, HttpServletResponse resp) {
        HttpClient httpClient = new DefaultHttpClient();

        try {
            UrlUtil urlUtil = new UrlUtil(cmClient);
            URI uri = urlUtil.getFullSourceURI(req, true);
            HttpGet get = new HttpGet(uri.toString());
            HttpResponse httpResponse ;
            httpResponse = httpClient.execute(get);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity!=null) {
                // Copy headers info
                Header[] headers = httpResponse.getAllHeaders();
                for (int hCount = 0; hCount<headers.length; hCount++) {
                    resp.addHeader(headers[hCount].getName(), headers[hCount].getValue());
                }
                resp.getWriter().write(EntityUtils.toString(httpEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CMException e) {
            LOG.log(Level.WARNING, "Error retrieving info from CmServer." + e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}
