package com.atex.plugins.customsolr.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class InputTemplateUtilTest {

    InputTemplateUtil target;

    @Mock
    Element responseRoot;
    
    String solrServerUrl = "http://localhost:8080";
    String selectedCore = "public";
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        target = spy(new InputTemplateUtil(solrServerUrl));
    }

    @Test
    public void testGetAllITWithNoUrl() {
        String query = target.getFqUrl(selectedCore);
        doReturn(responseRoot).when(target).getJdomElement(query);
        assertEquals(0, target.getAllInputTemplates(selectedCore).size());
    }

    @Test
    public void testGetAllIT() {
        List<Element> lst = new ArrayList<Element>();
        Element itEles = new Element("lst")
            .setAttribute(InputTemplateUtil.OP_NAME, InputTemplateUtil.FC_FIELD);
        Element imageIt = new Element("int")
            .setAttribute(InputTemplateUtil.OP_NAME, "example.Image");
        Element articleIt = new Element("int")
            .setAttribute(InputTemplateUtil.OP_NAME, "example.StandardArticle");
        itEles.addContent(imageIt);
        itEles.addContent(articleIt);
        String query = target.getFqUrl(selectedCore);
        doReturn(responseRoot).when(target).getJdomElement(query);
        doReturn(itEles).when(target).getElementByAttr(responseRoot);
        List<String> result = target.getAllInputTemplates(selectedCore);
        assertEquals(2, result.size());
        assertTrue(result.contains("example.StandardArticle"));
    }

}
