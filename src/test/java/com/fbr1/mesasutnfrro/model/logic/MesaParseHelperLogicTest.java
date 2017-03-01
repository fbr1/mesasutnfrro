package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.entity.Examen;
import com.fbr1.mesasutnfrro.model.entity.Mesa;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class MesaParseHelperLogicTest {

    @Test
    public void examenBuiltSuccessfully() throws ParseException {

        String expectedOutput = "Examen" +
                                "{Id=0, " +
                                "fecha=Mon Jan 02 08:00:00 ART 2017, " +
                                "aula='405', " +
                                "materia=Materia{Id=0, nombre='Analisis Matematico', especialidad='ISI'}, " +
                                "mesa=null}";

        Examen examen = new MesaParseHelperLogic().buildExamen("ISI",
                                                        "405",
                                                        "Analisis Matematico 08:00:00",
                                                        "2017-01-02");
        assertEquals(expectedOutput, examen.toString());

    }

    @Test
    public void examenBuiltSuccessfullyWithMissingHourDigit() throws ParseException {

        String expectedOutput = "Examen" +
                "{Id=0, " +
                "fecha=Mon Jan 02 08:00:00 ART 2017, " +
                "aula='405', " +
                "materia=Materia{Id=0, nombre='Analisis Matematico', especialidad='ISI'}, " +
                "mesa=null}";

        Examen examen = new MesaParseHelperLogic().buildExamen("ISI",
                "405",
                "Analisis Matematico 8:00:00",
                "2017-01-02");
        assertEquals(expectedOutput, examen.toString());

    }

    @Test
    public void examenBuiltSuccessfullyWithWrongHourSeparator() throws ParseException {

        String expectedOutput = "Examen" +
                "{Id=0, " +
                "fecha=Mon Jan 02 08:00:00 ART 2017, " +
                "aula='405', " +
                "materia=Materia{Id=0, nombre='Analisis Matematico', especialidad='ISI'}, " +
                "mesa=null}";

        Examen examen = new MesaParseHelperLogic().buildExamen("ISI",
                "405",
                "Analisis Matematico 8.00:00",
                "2017-01-02");
        assertEquals(expectedOutput, examen.toString());

    }

    @Test(expected=ParseException.class)
    public void examenBuiltWithWrongHour() throws ParseException {

        Examen examen = new MesaParseHelperLogic().buildExamen("ISI",
                "405",
                "Analisis Matematico",
                "2017-01-02");

    }

    @Test(expected=ParseException.class)
    public void examenBuiltWithWrongDate() throws ParseException {

        Examen examen = new MesaParseHelperLogic().buildExamen("ISI",
                "405",
                "Analisis Matematico 8.00:00",
                "2017-01");

    }

    @Test
    public void mesaComplete() throws ParseException {

        String input = "ISI\n" +
                "201\n" +
                "Ingeniería y Sociedad 08:00:00\n" +
                "210\n" +
                "Probabilidad y Estadística 09:00:00\n" +
                "ISI\n" +
                "104/105\n" +
                "Ingeniería y Sociedad 14:00:00\n";

        Mesa mesa = new MesaParseHelperLogic("2017-02-23").buildAndGetMesa(input);

        assertEquals(mesa.getState(), Mesa.State.COMPLETE);

    }

    // Test case for mesa 2017-02-23
    @Test
    public void mesaIncompleteWithExtraText() throws ParseException {

        String input = "Extra text at the beginning\n" +
                "ISI\n" +
                "201\n" +
                "Ingeniería y Sociedad 08:00:00\n" +
                "210\n" +
                "Probabilidad y Estadística 09:00:00\n" +
                "ISI\n" +
                "104/105\n" +
                "Ingeniería y Sociedad 14:00:00\n";

        Mesa mesa = new MesaParseHelperLogic("2017-02-23").buildAndGetMesa(input);

        assertEquals(mesa.getState(), Mesa.State.INCOMPLETE);

    }

    // Test case for mesa 2016-09-20
    @Test
    public void mesaIncompleteWithMissingAula() throws ParseException {

        String input = "ISI\n" +
                "210\n" +
                "Probabilidad y Estadística 09:00:00\n" +
                "IC\n" +
                "Probabilidad y Estadística 09:00:00";

        Mesa mesa = new MesaParseHelperLogic("2016-09-20").buildAndGetMesa(input);

        assertEquals(mesa.getState(), Mesa.State.INCOMPLETE);

    }

}
