package com.atex.plugins.customsolr.util;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class JdomUtil {

    private static final Logger LOG = Logger.getLogger(JdomUtil.class.getName());

    public static Element getElement(URI uri) {
        return JdomUtil.getElement(uri.toString());
    }

    public static Element getElement(String url) {
        Element root = new Element("response");
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = (Document) builder.build(url);
            root = document.getRootElement();
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error accessing resource " + url , e );
        } catch (JDOMException e) {
            LOG.log(Level.WARNING, "Error parsing jdom " + url , e );
        }
        return root;
    }
}
