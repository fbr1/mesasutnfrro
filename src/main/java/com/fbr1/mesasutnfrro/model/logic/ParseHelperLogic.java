package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.entity.Examen;
import com.fbr1.mesasutnfrro.model.entity.Materia;
import com.fbr1.mesasutnfrro.model.entity.ParseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseHelperLogic {

    static private final int HOUR_STR_LENGHT = 8;
    static private final String HOUR_STR_FILLER = "0";

    private final Logger logger = LoggerFactory.getLogger(ParseHelperLogic.class);

    /**
     * Returns a List of Examenes populated from unprocessed data in the ParseHelper
     *
     * @param helper - ParseHelper object
     * @param date - Mesa's date in string format (dd-MM-yy)
     * @return      List of Examenes
     */
    public ArrayList<Examen> getExamenes(ParseHelper helper, String date){

        helper = processData(helper);

        ArrayList<Examen> examenes = new ArrayList<>();
        for(ParseHelper especialidadHelper : helper.getChilds() ){

            for(ParseHelper aulaHelper : especialidadHelper.getChilds() ){

                for(ParseHelper examenHelper : aulaHelper.getChilds() ){

                    examenes.add(buildExamen(especialidadHelper.getName(), aulaHelper.getName(),
                                             examenHelper.getName(), date));
                }
            }
        }

        return examenes;
    }

    /**
     * Returns a ParseHelper Object processed recursively by the function
     * It requires a ParseHelper Object with a text property cleaned and normalized to avoid errors in classification
     *
     * @param helper - first ParseHelper object
     * @return      ParseHelper Object filled with data from the first helper
     */
    private ParseHelper processData(ParseHelper helper){
        ArrayList<ParseHelper> helpers = new ArrayList<>();

        String[] splitted = helper.getText().split(helper.getSplitPattern());
        String[] lines;
        StringBuilder stringBuilder;
        for(String textPart :splitted){

            lines = textPart.split(System.getProperty("line.separator"));
            stringBuilder = new StringBuilder();

            // Create helper and increment in 1 the depth
            int depth = helper.getDepth() + 1;
            ParseHelper ph = new ParseHelper(depth);

            // Set helper name
            ph.setName(lines[0]);

            // Remove name from the line
            for (int i = 1; i < lines.length; i++) {
                stringBuilder.append(lines[i]).append(System.getProperty("line.separator"));
            }

            // Set helper text
            ph.setText(stringBuilder.toString());

            // End condition = max depth reached
            if(ph.getDepth() <= ParseHelper.MAX_DEPTH){
                ph = processData(ph);
            }
            helpers.add(ph);
        }
        helper.setChilds(helpers);
        return helper;
    }

    /**
     * Returns a Examen Object built from the parameters of the function
     *
     * @param especialidad - especialidad from the materia
     * @param aula - aula in which the examen is going to occur
     * @param examenStr - Materia's name and examen's hour
     * @param date - Mesa's date in string format (dd-MM-yy)
     * @return      Examen Object
     */
    private Examen buildExamen(String especialidad, String aula, String examenStr, String date){

        // Extract hour
        Pattern hoursPattern = Pattern.compile("(\\d{1,2}:\\d{2}:\\d{2})");
        Matcher m = hoursPattern.matcher(examenStr);
        String hourStr = null;
        if(m.find()){
            hourStr = m.group(1);
        }

        // Extract materia
        String materiaName = examenStr.replace(" " + hourStr, "");
        Materia materia = new Materia(materiaName, especialidad);

        // Transform hourStr from String to Date
        DateFormat examenDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        examenDateFormat.setTimeZone(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

        if(hourStr.length() < HOUR_STR_LENGHT ){
            hourStr = HOUR_STR_FILLER + hourStr;
        }
        hourStr = date + " " + hourStr;
        Date fecha = new Date();
        try {
            fecha = examenDateFormat.parse(hourStr);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }

        return new Examen(fecha, aula, materia);
    }
}
