package com.atex.plugins.customsolr.util;

import static org.junit.Assert.*;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class ElementAttrUtilTest {

    Element root;
    Element result;
    Element wanted;

    String attrName = "name";
    String attrVal = "theone";
    String lst = "lst";

    @Before
    public void setUp() {
        root = new Element("response");
        Element header = new Element(lst).setAttribute(attrName, "header");
        result = new Element("result").setAttribute(attrName, "response");
        Element footer = new Element(lst).setAttribute(attrName, "footer");
        wanted = new Element(lst).setAttribute(attrName, attrVal);
        root.addContent(header);
        root.addContent(result);
        root.addContent(footer);
    }

    @Test
    public void testGetElementByAttr() {
        Element b1 = new Element(lst).setAttribute(attrName, "b1");
        Element b2 = new Element(lst).setAttribute(attrName, "b2");
        Element b2l1 = new Element(lst).setAttribute(attrName, "b2l1");
        b2.addContent(b2l1);
        Element b3 = new Element(lst).setAttribute(attrName, "b3");
        b3.addContent(wanted);
        result.addContent(b1);
        result.addContent(b2);
        assertNull(ElementAttrUtil.getElementByAttr(root, attrName, attrVal));
        result.addContent(b3);
        assertEquals(wanted ,ElementAttrUtil.getElementByAttr(root, attrName, attrVal));
    }
}
