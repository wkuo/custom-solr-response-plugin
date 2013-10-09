package com.atex.plugins.customsolr.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConvertionUtilTest {



    @Test
    public void testStr2Int() {
        assertEquals(Integer.valueOf(-100), ConvertionUtil.strToInt("-100"));
        assertNull(ConvertionUtil.strToInt(null));
    }

    @Test
    public void testStr2Bool() {
        assertTrue(ConvertionUtil.strToBool("true"));
        assertFalse(ConvertionUtil.strToBool("false"));
        assertFalse(ConvertionUtil.strToBool(null));
        assertFalse(ConvertionUtil.strToBool("gigo"));
    }
    
}
