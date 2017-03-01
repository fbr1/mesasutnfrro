package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.entity.Examen;
import com.fbr1.mesasutnfrro.model.entity.Materia;
import com.fbr1.mesasutnfrro.model.entity.Mesa;
import com.fbr1.mesasutnfrro.model.entity.MesaParseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;

public class MesaParseHelperLogic {

    static private final int HOUR_STR_LENGHT = 8;
    static private final String HOUR_STR_FILLER = "0";
    private String dateStr;

    private static final Logger logger = LoggerFactory.getLogger(MesaParseHelperLogic.class);

    /**
     * Returns a Mesa object populated from unprocessed data in the ParseHelper
     *
     * @param text - string containing normalized and clean text from a Mesa's PDF
     * @return      Mesa with examenes and state
     */
    public Mesa buildAndGetMesa(String text) throws ParseException{

        Mesa mesa = new Mesa();

        MesaParseHelper helper = new MesaParseHelper();
        helper.setLines(Arrays.asList(text.split(System.getProperty("line.separator"))));

        helper = processData(helper);

        ArrayList<Examen> examenes = new ArrayList<>();
        for(MesaParseHelper especialidadHelper : helper.getChilds() ){
            logger.debug(especialidadHelper.getName());

            for(MesaParseHelper aulaHelper : especialidadHelper.getChilds() ){
                logger.debug(aulaHelper.getName());

                for(MesaParseHelper examenHelper : aulaHelper.getChilds() ){
                    logger.debug(examenHelper.getName());

                    if (examenHelper.getName() != null && especialidadHelper.getName() != null){
                        examenes.add(buildExamen(especialidadHelper.getName(), aulaHelper.getName(),
                                examenHelper.getName(), dateStr));
                    }

                    if(mesa.getState() != Mesa.State.INCOMPLETE){

                        if(examenHelper.getName() == null || aulaHelper.getName() == null ||
                                especialidadHelper.getName() == null){

                            mesa.setState(Mesa.State.INCOMPLETE);

                        }

                    }
                }
            }
        }

        mesa.setExamenes(examenes);

        return mesa;
    }

    /**
     * Returns a MesaParseHelper Object processed recursively by the function
     * It requires a MesaParseHelper Object with a text property cleaned and normalized to avoid errors in classification
     *
     * @param helper - first MesaParseHelper object
     * @return      MesaParseHelper Object filled with data from the first helper
     */
    private MesaParseHelper processData(MesaParseHelper helper) throws ParseException{
        ArrayList<MesaParseHelper> helpers = new ArrayList<>();
        Matcher matcher;

        int depth = helper.getDepth() + 1;

        MesaParseHelper lastHelper = new MesaParseHelper(depth);

        for(String line : helper.getLines()){
            matcher = helper.getMatchPattern().matcher(line);
            if(matcher.find()){

                if (checkHelper(lastHelper) != null){
                    helpers.add(lastHelper);
                }

                // Create helper and increment the depth by 1
                depth = helper.getDepth() + 1;
                lastHelper = new MesaParseHelper(depth);

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

    private MesaParseHelper checkHelper(MesaParseHelper lastHelper) throws ParseException{

        // Check for empty helper
        if(lastHelper.getName() != null || !lastHelper.getLines().isEmpty()){
            if(lastHelper.getDepth() <= MesaParseHelper.MAX_DEPTH){
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
    public Examen buildExamen(String especialidad, String aula, String examenStr, String dateStr) throws ParseException{

        // Extract hour
        Matcher m = MesaParseHelper.HOURS_REGEX.matcher(examenStr);
        String hourStr;
        if(m.find()){
            hourStr = m.group(1);
        }else{
            throw new ParseException("Hour not found in Examen: " + examenStr, 0);
        }

        // Extract materia
        String materiaName = examenStr.replace(" " + hourStr, "");
        Materia materia = new Materia(materiaName, especialidad);

        // Correct Edge Case when '.' is used instead of ':'
        hourStr = hourStr.replace(".",":");

        // Transform hourStr from String to Date
        DateFormat examenDateFormat = new SimpleDateFormat(MesasExtractor.DATE_HOUR_FORMAT);
        examenDateFormat.setTimeZone(TimeZone.getTimeZone(MesasExtractor.TIMEZONE));

        // Add a 0 to the left if there is one missing.
        // Example: 5:00:00 -> 05:00:00
        if(hourStr.length() < HOUR_STR_LENGHT ){
            hourStr = HOUR_STR_FILLER + hourStr;
        }

        hourStr = dateStr + " " + hourStr;

        Date fecha = examenDateFormat.parse(hourStr);

        return new Examen(fecha, aula, materia);
    }

    /**
     * @param dateStr - Mesa's date in string format (yyyy-MM-dd)
     */
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public MesaParseHelperLogic() {

    }

    /**
     * Constructor
     *
     * @param dateStr - Mesa's date in string format (yyyy-MM-dd)
     */
    public MesaParseHelperLogic(String dateStr) {
        this.dateStr = dateStr;
    }
}
