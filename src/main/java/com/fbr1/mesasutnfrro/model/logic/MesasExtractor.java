package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.entity.*;
import com.fbr1.mesasutnfrro.util.MesasUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.ZoneOffset.UTC;

public class MesasExtractor {

    private static final Logger logger = LoggerFactory.getLogger(MesasExtractor.class);
    static private final String DATE_FORMAT = "yyyy-MM-dd";
    static final String DATE_HOUR_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String TIMEZONE = "America/Argentina/Buenos_Aires";

    private int nroLlamado = 0;
    private int añoLlamado = 0;
    private String mesaDay = null;
    private String mesaDateStr = null;

    private static final Map<String, String> monthsMap = new HashMap<>();
    static{
        monthsMap.put("ENERO","01");
        monthsMap.put("FEBRERO","02");
        monthsMap.put("MARZO","03");
        monthsMap.put("ABRIL","04");
        monthsMap.put("MAYO","05");
        monthsMap.put("JUNIO","06");
        monthsMap.put("JULIO","07");
        monthsMap.put("AGOSTO","08");
        monthsMap.put("SEPTIEMBRE","09");
        monthsMap.put("SETIEMBRE","09");
        monthsMap.put("OCTUBRE","10");
        monthsMap.put("NOVIEMBRE","11");
        monthsMap.put("DICIEMBRE","12");
    }

    /**
     * Builds a Mesa from the given pdf
     *
     * @param pdd - PDDocument pdf document containing Examanes
     * @return      Mesa extracted from the pdf document
     */
    public Mesa buildMesaFromPDF(PDDocument pdd) throws IOException, ParseException{

        PDFTextStripper stripper = new PDFTextStripper();
        String text=normalizeText(cleanText(stripper.getText(pdd)));
        
        pdd.close();

        return extractMesa(text);
    }

    /**
     * Extract relevant data of the Llamado and remove unnecessary lines
     *
     * @param oriText - Text stripped from the Mesa PDF
     * @return      List<String>
     */
    public List<String> cleanText(String oriText) throws ParseException{

        // Remove text containing 1-Mañana, 2-Tarde y 3-Noche
        oriText = MesaParseHelper.TURNO_REGEX.matcher(oriText).replaceAll("");

        List<String> lines = Arrays.asList(oriText.split(System.getProperty("line.separator")));
        List<String> cleanedLines = new ArrayList<>();

        boolean llamadoFound = false;
        boolean dateFound = false;
        boolean titleFound = false;
        boolean headerFound = false;

        for(String line : lines){

            // If the line is blank, skip it
            line = MesasUtil.removeExtraSpaces(line).trim();

            if (line.length() <=1){
                continue;
            }

            // Remove Title
            if (!titleFound && line.toLowerCase().contains("DISTRIBUCION DE AULAS".toLowerCase()) ){
                titleFound = true;
                continue;
            }

            // Extract Numero de llamado
            if (!llamadoFound && line.toLowerCase().contains("LLAMADO".toLowerCase())) {

                // Regex for a number of 1 or 2 digits
                Matcher matcher = Pattern.compile("\\d{1,2}").matcher(line);

                if (matcher.find()) {
                    nroLlamado = Integer.valueOf(matcher.group(0));
                }

                llamadoFound = true;

                continue;
            }

            // Extract Año de llamado and Mesa Date
            if (!dateFound){
                Matcher matcher = MesaParseHelper.DATE_REGEX.matcher(line);

                if(matcher.find()){
                    mesaDateStr = parseDate(matcher.group("daynumber"), matcher.group("month"), matcher.group("year"));

                    mesaDay = matcher.group("day");

                    añoLlamado = Integer.valueOf(matcher.group("year"));

                    dateFound = true;
                    continue;
                }
            }

            // Remove Table header
            if (!headerFound && line.toLowerCase().contains("Horario".toLowerCase()) ){
                headerFound = true;
                continue;
            }

            cleanedLines.add(line);

        }

        return cleanedLines;
    }

    /**
     * Parse date from the Mesas' PDF date line
     *
     * @return      String with the Mesa's date in (yyyy-MM-dd) format
     */
    public String parseDate(String dayNumber, String month, String year) throws ParseException{

        month = MesasExtractor.monthsMap.get(month.toUpperCase());
        if (month == null){
            throw new ParseException("The month parsed doesn't correspond with a real month in spanish", 0);
        }

        return year + "-" + month + "-" + dayNumber;
    }

    /**
     * Normalizes text format and handle edge cases.
     * Makes sure that every Especialidad, Aula and Examen is in its own line.
     *
     * @param lines - lines cleaned from the Mesa PDF
     * @return      String containing an normalized format of Examanes
     */
    public String normalizeText(List<String> lines){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i< lines.size(); i++){

            String line = lines.get(i);

            boolean skip = false;

            Matcher matcher = MesaParseHelper.ESPECIALIDAD_REGEX.matcher(line);
            boolean esEspecialidad = matcher.find();
            if(esEspecialidad){

                /* If there is text before or after the especialidad, add a newline
                 * Example:
                 *      "210 REDES 8:00:00 ISI" > "210 REDES 8:00:00\nISI"
                 *      "ISI 308 Matemática Discreta 8:00:00" > "ISI\n308 Matemática Discreta 8:00:00"
                 */

                String match = matcher.group(1);

                if(match.charAt(match.length()-1)==' '){
                    line = line.replace(match, match.substring(0, match.length()-1) + System.getProperty("line.separator"));
                }else if(match.charAt(0)==' '){
                    line = line.replace(match, System.getProperty("line.separator") + match.substring(1, match.length()));
                }
            }

            matcher = MesaParseHelper.AULA_REGEX.matcher(line);
            boolean esAula = matcher.find();
            if(esAula) {

                // If there is text after the aula, add a newline
                // Example: "309 Analisis Matematico II" > "309\nAnalisis Matematico II"
                if (matcher.group(1).contains(" ")) {
                    line = line.replace(matcher.group(1),
                            matcher.group(1).substring(0, matcher.group(1).length() - 1) + System.getProperty("line.separator"));
                } else {

                    /* Edge case for an Aula split in two lines
                     * Example(Mesa of 12/02/2016) :
                     *      308
                     *      309 Analisis Matematico II
                     */
                    if(MesaParseHelper.AULA_LINE_REGEX.matcher(line).find() && i-1 >=0 && i+1 < lines.size()){

                        // If the next line matches aulaLineRegex, the next iteration handles it
                        matcher = MesaParseHelper.AULA_LINE_REGEX.matcher(lines.get(i + 1));
                        if (matcher.find()) {
                            skip = true;
                        }else{
                            // If the previous line matches aulaLineRegex, append it at the beginning of the line
                            matcher = MesaParseHelper.AULA_LINE_REGEX.matcher(lines.get(i - 1));
                            if (matcher.find()) {
                                line = matcher.group(1) + "/" + line;
                            }
                        }
                    }

                }

            }
            /*
             *  Edge case for hours using two rows
             *  Example taken from Mesa of 12/02/2016:
             *
             *      IE                                      IE
             *      SUM                                     SUM
             *      Análisis Matemático I               >   Análisis Matemático I 17:00:00
             *      17:00:00                                Análisis Matemático II 17:00:00
             *      Análisis Matemático II                  110/111 Química General 16:30:00
             *      110/111 Química General 16:30:00
             *
             */

            if(!esEspecialidad && !esAula){
                if(i-1>=0){
                    // Check if the previous line has an Horario, if so append it after the Materia
                    matcher = MesaParseHelper.HOURS_LINE_REGEX.matcher(lines.get(i-1));
                    if(matcher.find()){
                        line += " " + matcher.group(1);
                    }else if(i+1<lines.size()){
                        // Check if the next line has an Horario, if so append it after the Materia
                        matcher = MesaParseHelper.HOURS_LINE_REGEX.matcher(lines.get(i+1));
                        if(matcher.find()){
                            line += " " + matcher.group(1);
                            i++;
                        }
                    }
                }
            }


            if(!skip){
                stringBuilder.append(line).append(System.getProperty("line.separator"));
            }

        }

        return stringBuilder.toString();
    }

    /**
     * Cast the normalized text into a Mesa Object
     *
     * @param text - text cleaned and normalized from the Examenes PDF
     * @return      Mesa object
     */
    public Mesa extractMesa(String text) throws ParseException{

        // Transform date from String to Date
        ZoneId argZone = ZoneId.of(MesasExtractor.TIMEZONE);
        DateTimeFormatter argTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(argZone);
        LocalDate mesaDate = ZonedDateTime.parse(mesaDateStr, argTimeFormatter).withZoneSameInstant(UTC).toLocalDate();

        // Parse text to Examenes
        Mesa mesa = new MesaParseHelperLogic(mesaDateStr).buildAndGetMesa(text);

        for(Examen examen : mesa.getExamenes()){
            examen.setMesa(mesa);
        }

        mesa.setFecha(mesaDate);

        // Set WeekDay
        mesa.setWeekDay(parseWeekDay(mesaDay));

        return mesa;
    }
    /**
     * Parse the corresponding WeekDay from the given String
     *
     * @param mesaDay - String containing the Mesa's day of the week
     * @return      WeekDay object
     */
    public Mesa.WeekDay parseWeekDay(String mesaDay) throws ParseException {

        for(Mesa.WeekDay weekDay : Mesa.WeekDay.values()){
            if (MesasUtil.equalsStringIgnoringAccents(weekDay.name(), mesaDay)){
                return weekDay;
            }
        }

        // If doesn't return a WeekDay throw exception
        throw new ParseException("The mesaDay given: " + mesaDay + "doesn't correspond to an existing weekDay", 0);

    }

    public int getNroLlamado() {
        return nroLlamado;
    }

    public int getAñoLlamado() {
        return añoLlamado;
    }
}
