package com.fbr1.mesasutnfrro.model.logic;

import org.junit.Test;

public class LlamadosLogicTest {

    @Test
    public void weekTypeMinArgumentWithingRange(){
        LlamadosLogic.validateWeekType(1);
    }

    @Test
    public void weekTypeMaxArgumentWithingRange(){
        LlamadosLogic.validateWeekType(128);
    }

    @Test(expected=IllegalArgumentException.class)
    public void weekTypeMinArgumentOutsideOfRange(){
        LlamadosLogic.validateWeekType(0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void weekTypeMaxArgumentOutsideOfRange(){
        LlamadosLogic.validateWeekType(129);
    }

}
