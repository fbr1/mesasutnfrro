package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.entity.Examen;
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

}
