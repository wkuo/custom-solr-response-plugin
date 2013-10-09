package com.atex.plugins.customsolr.servlet;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Ignore;
import org.junit.Test;

import com.atex.integration.authentication.QueryParameterDecoder;
import com.atex.integration.authentication.QueryParameterDecoder.ProcessingFailedException;

public class SolrLoginFilterTest {

    @Ignore
    @Test
    public void testDecodeUserId() {
        String encodedId = "U51ZHw89ch3UKKqF4qYo5A==";
        String expected = "sysadmin";
        String decoderClass = "com.atex.integration.authentication.AESEncoderDecoder";
        String key = "-65, -69, 36, 94, -62, 120, -70, 110, 36, -13, -97, 15, -70, 127, 56, 70";
        String[] byteStrings = key.split(",");
        byte[] bytes = new byte[byteStrings.length];
        for (int i = 0; i < byteStrings.length; i++) {
            bytes[i] = Byte.valueOf(byteStrings[i].trim()).byteValue();
        }
        try {
            Object obj = Class.forName(decoderClass 
                    ).getConstructors()[0].newInstance(new Object[] {bytes});
            QueryParameterDecoder decoder = (QueryParameterDecoder) obj;
//            System.out.println(decoder.encode("someone"));
            assertEquals(expected, decoder.decode(encodedId));
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProcessingFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
