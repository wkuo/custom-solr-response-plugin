package com.atex.plugins.customsolr.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.polopoly.cm.ExternalContentId;
import com.polopoly.cm.app.util.impl.ResourceBundleUtil;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.Content;
import com.polopoly.cm.policy.Policy;
import com.polopoly.cm.policy.PolicyCMServer;
import com.polopoly.common.i18n.ExtendedResourceBundle;

public class ContentLocaleUtilTest {

    ContentLocaleUtil target;

    @Mock
    PolicyCMServer cmServer;
    @Mock
    ExternalContentId extId;
    @Mock
    Policy itPolicy;
    @Mock
    Content content;
    @Mock
    ResourceBundleUtil rbUtil;
    @Mock
    ExtendedResourceBundle rb;

    String extIdStr = "extid";
    String prefered = "en-US";
    String lblKey = "lblKey";
    Locale locale = new Locale(prefered);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        target = spy(new ContentLocaleUtil(cmServer));
    }

    @Test
    public void testGetITLabelKey() throws CMException {
        String expected = "lblKey";
        doReturn(extId).when(target).getITExtId(extIdStr);
        when(cmServer.getPolicy(extId)).thenReturn(itPolicy);
        when(itPolicy.getContent()).thenReturn(content);
        when(content.getComponent(
                ContentLocaleUtil.POLOPOLY_CLIENT, 
                ContentLocaleUtil.LABEL)).thenReturn(expected);
        assertEquals(expected, target.getITLabelKey(extIdStr));
    }

    @Test
    public void testGetITLabelNoPrefered() {
        String expected = "Standard article";
        doReturn(lblKey).when(target).getITLabelKey(extIdStr);
        doReturn(expected).when(target).getLabelByKey(lblKey, null);
        assertEquals(expected, target.getITLabel(extIdStr, ""));
    }

    @Test
    public void testGetITLabel() {
        String expected = "Standard article";
        doReturn(lblKey).when(target).getITLabelKey(extIdStr);
        doReturn(locale).when(target).getLocale(prefered);
        doReturn(expected).when(target).getLabelByKey(lblKey, locale);
        assertEquals(expected, target.getITLabel(extIdStr, prefered));
    }

    @Test
    public void testGetLabelByKeyNoPrefered() {
        String expected = "Standard article";
        doReturn(rbUtil).when(target).getResourceBundleUtil();
        when(rbUtil.getDefaultResourceBundle(cmServer)).thenReturn(rb);
        doReturn(expected).when(target).getLocaleFormat(lblKey, rb);
        assertEquals(expected, target.getLabelByKey(lblKey, null));
    }

    @Test
    public void testGetLabelByKey() {
        String expected = "Standard article";
        doReturn(rbUtil).when(target).getResourceBundleUtil();
        when(rbUtil.getResourceBundle(cmServer, locale)).thenReturn(rb);
        doReturn(expected).when(target).getLocaleFormat(lblKey, rb);
        assertEquals(expected, target.getLabelByKey(lblKey, locale));
    }

}
