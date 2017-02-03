package com.fbr1.mesasutnfrro.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MesasUtilTest {

    @Test
    public void testWhiteSpaceRemoval(){
        assertEquals("Test Text", MesasUtil.removeExtraSpaces("  Test  Text "));
    }

    @Test
    public void testStringComparisonIgnoringAccents(){
        String str1 = "Miercoles";
        String str2 = "miércoles";
        assertTrue(MesasUtil.equalsStringIgnoringAccents(str1, str2));
    }
}
