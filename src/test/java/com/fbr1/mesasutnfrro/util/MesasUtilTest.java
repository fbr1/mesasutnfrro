package com.fbr1.mesasutnfrro.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MesasUtilTest {

    @Test
    public void testWhiteSpaceRemoval(){
        assertEquals("Test Text", MesasUtil.removeExtraSpaces("  Test  Text "));
    }
}
