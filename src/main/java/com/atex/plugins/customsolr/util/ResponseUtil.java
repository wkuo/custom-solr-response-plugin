package com.atex.plugins.customsolr.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class ResponseUtil {

    private static final Logger LOG = Logger.getLogger(ResponseUtil.class.getName());

    public static void write(HttpServletResponse resp, Element root) {
        XMLOutputter xmlOutputter = new XMLOutputter();
        resp.setContentType("application/xml");
        PrintWriter out;
        try {
            out = resp.getWriter();
            out.write(xmlOutputter.outputString(root));
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error writting to response. " , e );
        }
    }
}
