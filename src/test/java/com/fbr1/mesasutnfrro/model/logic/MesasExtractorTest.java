package com.fbr1.mesasutnfrro.model.logic;

import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MesasExtractorTest {

    @Test
    public void textCleanedSuccessfully() throws ParseException{

        String input =  " \n" +
                        " \n" +
                        "DISTRIBUCION DE AULAS \n" +
                        "8vo LLAMADO \n" +
                        "MIERCOLES 10 DE FEBRERO DE 2016 \n" +
                        "          \n" +
                        "Horario    Esp. Aula     Nombre de materia    Horario\n" +
                        "1-Mañana ISI \n" +
                        "201 Ingeniería y Sociedad 08:00:00 \n" +
                        "210 Probabilidad y Estadística 09:00:002-Tarde \n" +
                        "ISI \n" +
                        "104/105 Ingeniería y Sociedad 14:00:00 \n" +
                        " \n" +
                        "SUM \n" +
                        "Análisis Matemático II \n" +
                        "17:00:00 \n" +
                        "Álgebra y Geometría Analítica \n" +
                        "110/111 Química 16:30:00 \n" +
                        "501 Administración de Recursos 17:30:00 \n" +
                        " \n" +
                        " ";

        String expectedOutput = "ISI\n" +
                                "201 Ingeniería y Sociedad 08:00:00\n" +
                                "210 Probabilidad y Estadística 09:00:00\n" +
                                "ISI\n" +
                                "104/105 Ingeniería y Sociedad 14:00:00\n" +
                                "SUM\n" +
                                "Análisis Matemático II\n" +
                                "17:00:00\n" +
                                "Álgebra y Geometría Analítica\n" +
                                "110/111 Química 16:30:00\n" +
                                "501 Administración de Recursos 17:30:00\n";

        MesasExtractor mesasExtractor = new MesasExtractor();

        List<String> cleanedText = mesasExtractor.cleanText(input);

        StringBuilder stringBuilder = new StringBuilder();
        for(String line : cleanedText){
            stringBuilder.append(line).append(System.getProperty("line.separator"));
        }
        String output = stringBuilder.toString();

        assertEquals(expectedOutput, output);
        assertEquals(2016, mesasExtractor.getAñoLlamado());
        assertEquals(8, mesasExtractor.getNroLlamado());

    }

    @Test
    public void textAfterOrBeforeEspecialidadNormalizedSuccessfully() throws ParseException{

        String input =  "1/24/3 REDES 8:00:00 ISI\n" +
                        "ISI 308 Matemática Discreta 8:00:00\n";

        String expectedOutput = "1/24/3\n" +
                                "REDES 8:00:00\n" +
                                "ISI\n" +
                                "ISI\n" +
                                "308\n" +
                                "Matemática Discreta 8:00:00\n";

        testTextNormalizing(input, expectedOutput);

    }

    @Test
    public void aulaInTwoLinesNormalizedSuccessfully() throws ParseException{

        String input =  "210 Sistemas y Organizaciones 15:00:00\n" +
                        "308\n" +
                        "309\n" +
                        "Algoritmos y Estructuras de Datos 13:30:00\n" +
                        "IQ\n";

        String expectedOutput = "210\n" +
                                "Sistemas y Organizaciones 15:00:00\n" +
                                "308/309\n" +
                                "Algoritmos y Estructuras de Datos 13:30:00\n" +
                                "IQ\n";

        testTextNormalizing(input, expectedOutput);

    }

    @Test
    public void horarioInASingleLineNormalizedSuccessfully() throws ParseException{

        String input =  "SUM\n" +
                        "Análisis Matemático I\n" +
                        "17:00:00\n" +
                        "Análisis Matemático II\n";

        String expectedOutput = "SUM\n" +
                                "Análisis Matemático I 17:00:00\n" +
                                "Análisis Matemático II 17:00:00\n";

        testTextNormalizing(input, expectedOutput);

    }

    private void testTextNormalizing(String input, String expectedOutput) throws ParseException{

        List<String> inputList = new ArrayList<>();

        inputList.addAll(Arrays.asList(input.split(System.getProperty("line.separator"))));

        MesasExtractor mesasExtractor = new MesasExtractor();

        String output = mesasExtractor.normalizeText(inputList);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void parseDateSuccessfully() throws ParseException{
        String date = new MesasExtractor().parseDate("15","OCTUBRE","2015");
        assertEquals("2015-10-15",date);

    }

    @Test
    public void parseDateWithError() throws ParseException{
        try {
            new MesasExtractor().parseDate("15", "JUPITER", "2015");
            Assert.fail();
        }catch(ParseException e){

        }

    }
}
