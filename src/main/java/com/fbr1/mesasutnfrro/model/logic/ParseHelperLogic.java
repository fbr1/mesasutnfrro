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
    public ArrayList<Examen> getExamenes(ParseHelper helper, String date) throws ParseException{

        helper = processData(helper);

        ArrayList<Examen> examenes = new ArrayList<>();
        for(ParseHelper especialidadHelper : helper.getChilds() ){
            logger.debug(especialidadHelper.getName());

            for(ParseHelper aulaHelper : especialidadHelper.getChilds() ){
                logger.debug(aulaHelper.getName());

                for(ParseHelper examenHelper : aulaHelper.getChilds() ){
                    logger.debug(examenHelper.getName());

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
    private ParseHelper processData(ParseHelper helper) throws ParseException{
        ArrayList<ParseHelper> helpers = new ArrayList<>();
        Matcher matcher;

        int depth = helper.getDepth() + 1;

        ParseHelper lastHelper = new ParseHelper(depth);

        for(String line : helper.getLines()){
            matcher = helper.getMatchPattern().matcher(line);
            if(matcher.find()){

                if (checkHelper(lastHelper) != null){
                    helpers.add(lastHelper);
                }

                // Create helper and increment the depth by 1
                depth = helper.getDepth() + 1;
                lastHelper = new ParseHelper(depth);

                // Set helper name
                lastHelper.setName(matcher.group(1));

            }else{
                lastHelper.getLines().add(line);
            }
        }

        if (checkHelper(lastHelper) != null){
            helpers.add(lastHelper);
        }

        helper.setChilds(helpers);
        return helper;
    }

    private ParseHelper checkHelper(ParseHelper lastHelper) throws ParseException{

        // Check for empty helper
        if(lastHelper.getName() != null || !lastHelper.getLines().isEmpty()){
            if(lastHelper.getDepth() <= ParseHelper.MAX_DEPTH){
                lastHelper = processData(lastHelper);
            }
            return lastHelper;
        }
        return null;
    }

    /**
     * Returns a Examen Object built from the parameters of the function
     *
     * @param especialidad - especialidad from the materia
     * @param aula - aula in which the examen is going to occur
     * @param examenStr - Materia's name and examen's hour
     * @param dateStr - Mesa's date in string format (yyyy-MM-dd)
     * @return      Examen Object
     */
    private Examen buildExamen(String especialidad, String aula, String examenStr, String dateStr) throws ParseException{

        // Extract hour
        Matcher m = ParseHelper.HOURS_REGEX.matcher(examenStr);
        String hourStr;
        if(m.find()){
            hourStr = m.group(1);
        }else{
            throw new ParseException("Hour not found in Examen", 0);
        }

        // Extract materia
        String materiaName = examenStr.replace(" " + hourStr, "");
        Materia materia = new Materia(materiaName, especialidad);

        // Correct Edge Case when '.' is used instead of ':'
        hourStr = hourStr.replace(".",":");

        // Transform hourStr from String to Date
        DateFormat examenDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        examenDateFormat.setTimeZone(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

        // Add a 0 to the left if there is one missing.
        // Example: 5:00:00 -> 05:00:00
        if(hourStr.length() < HOUR_STR_LENGHT ){
            hourStr = HOUR_STR_FILLER + hourStr;
        }

        hourStr = dateStr + " " + hourStr;

        Date fecha = examenDateFormat.parse(hourStr);

        return new Examen(fecha, aula, materia);
    }
}
