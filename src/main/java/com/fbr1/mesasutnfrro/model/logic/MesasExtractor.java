package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.entity.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MesasExtractor {

    private static final Logger logger = LoggerFactory.getLogger(MesasExtractor.class);

    private int nroLlamado = 0;
    private int añoLlamado = 0;
    private String mesaDay = null;
    private String mesaDate = null;

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
     * Extracts a Mesa from the given pdf
     *
     * @param pdd - PDDocument pdf document containing Examanes
     * @return      Mesa extracted from the pdf document
     */
    public Mesa processPDF(PDDocument pdd) throws IOException, ParseException{

        PDFTextStripper stripper = new PDFTextStripper();
        String text=cleanText(stripper.getText(pdd));
        
        pdd.close();

        return extractMesa(text, this.mesaDate);
    }

    /**
     * Extract relevant data of the Llamado and remove unnecessary lines
     *
     * @param oriText - Text stripped from the Mesa PDF
     * @return      String containing an uniform format of Examanes
     */
    public String cleanText(String oriText) throws ParseException{

        // Remove text containing 1-Mañana, 2-Tarde y 3-Noche
        oriText = Pattern.compile(".*(?:Ma\\p{L}ana*|Tarde\\p{L}*|Noche\\p{L}*)",
                Pattern.CASE_INSENSITIVE).matcher(oriText).replaceAll("");

        List<String> lines = Arrays.asList(oriText.split(System.getProperty("line.separator")));
        List<String> cleanedLines = new ArrayList<>();

        for(String line : lines){

            boolean remove = false;

            // Extract Numero de llamado

            if (line.toLowerCase().contains("LLAMADO".toLowerCase())) {

                // Regex for a number of 1 or 2 digits
                Matcher matcher = Pattern.compile("\\d{1,2}").matcher(line);

                if (matcher.find()) {
                    nroLlamado = Integer.valueOf(matcher.group(0));
                }

                remove = true;
            }

            // Extract Año de llamado and Mesa Date

            // Regex matching "<DAY> <DAYNUMBER> DE <MONTH> DE <YEAR>"
            Matcher matcher = Pattern.compile("(?<day>[a-zA-Z]+) (?<daynumber>\\d{1,2}) DE (?<month>[a-zA-Z]+) DE (?<year>\\d{4})").matcher(line);

            if(matcher.find()){
                mesaDate = parseDate(matcher.group("daynumber"), matcher.group("month"), matcher.group("year"));

                mesaDay = matcher.group("day");

                añoLlamado = Integer.valueOf(matcher.group("year"));
                remove = true;
            }

            // Remove Title of the document and Table header
            if(line.toLowerCase().contains("DISTRIBUCION DE AULAS ".toLowerCase()) ||
                    line.toLowerCase().contains("Horario".toLowerCase())){

                remove = true;
            }

            line = removeExtraSpaces(line).trim();

            // If it's not a blank line and we don't have to skip the line
            if(line.length() > 1 && !remove){
                cleanedLines.add(line);
            }
        }

        return normalizeText(cleanedLines);
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
    private String normalizeText(List<String> lines){
        StringBuilder stringBuilder = new StringBuilder();

        // Regex for especialidades
        Pattern especialidadRegex = Pattern.compile("(ISI ?|IE ?|IQ ?|IC ?|IM ?)");

        // Regex matching one or more aulas
        Pattern aulaRegex = Pattern.compile("([\\d{1,3}/]+\\d{1,3} ?|\\d{1,3} ?|SUM)");

        // Regex matching aula in line
        Pattern aulaLineRegex = Pattern.compile("(^\\d{2,3}|SUM)$");

        // Regex matching a line with only the time
        Pattern hoursPattern = Pattern.compile("(^\\d{1,2}.\\d{2}.\\d{2}$)");

        for(int i = 0; i< lines.size(); i++){

            String line = lines.get(i);

            boolean skip = false;

            // Make all the data format homogeneous
            Matcher matcher = especialidadRegex.matcher(line);
            boolean esEspecialidad = matcher.find();
            if(esEspecialidad){
                // If there is text after the especialidad, add a newline
                if(matcher.group(1).contains(" ")){
                    line = line.replace(matcher.group(1),
                            matcher.group(1).substring(0, matcher.group(1).length()-1)+System.getProperty("line.separator"));
                }
            }

            matcher = aulaRegex.matcher(line);
            boolean esAula = matcher.find();
            if(esAula) {
                // If there is text after the aula, add a newline
                if (matcher.group(1).contains(" ")) {
                    line = line.replace(matcher.group(1),
                            matcher.group(1).substring(0, matcher.group(1).length() - 1) + System.getProperty("line.separator"));
                } else {
                    // Edge case for an Aula split in two lines
                    // Example: Mesa of 12/02/2016
                    if(aulaLineRegex.matcher(line).find() && i-1 >=0 && i+1 < lines.size()){

                        // If the next line matches aulaLineRegex, the next iteration handles it
                        matcher = aulaLineRegex.matcher(lines.get(i + 1));
                        if (matcher.find()) {
                            skip = true;
                        }else{
                            // If the previous line matches aulaLineRegex, append it at the beginning of the line
                            matcher = aulaLineRegex.matcher(lines.get(i - 1));
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
             *      IE
             *      SUM
             *      Análisis Matemático I
             *      17:00:00
             *      Análisis Matemático II
             *      110/111 Química General 16:30:00
             *
             */

            if(!esEspecialidad && !esAula){
                if(i-1>=0){
                    // Check if the previous line has an Horario, if so append it after the Materia
                    matcher = hoursPattern.matcher(lines.get(i-1));
                    if(matcher.find()){
                        line += " " + matcher.group(1);
                    }else if(i+1<lines.size()){
                        // Check if the next line has an Horario, if so append it after the Materia
                        matcher = hoursPattern.matcher(lines.get(i+1));
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
     * Removes white spaces at the beginning and end of a String.
     * Also removes double white spaces.
     *
     * @param str - String to remove extra white spaces
     * @return      String with extra white spaces removed
     */
    private String removeExtraSpaces(String str){

        // Removes doubles white spaces
        str = str.replaceAll("\\s+", " ");

        if(str.length() > 1){
            // Remove extra white space at the end
            if(str.charAt(str.length()-1)==' '){
                str = str.substring(0, str.length()-1);
            }
            // Remove extra white space at the beginning
            if(str.charAt(0)==' '){
                str = str.substring(1, str.length());
            }
        }

        return str;
    }

    /**
     * Cast the normalized text into a Mesa Object
     *
     * @param text - text cleaned and normalized from the Examenes PDF
     * @param dateStr - String from the Mesa's date in the format: (yyyy-MM-dd)
     * @return      Mesa object
     */
    private Mesa extractMesa(String text, String dateStr) throws ParseException{

        // Transform date from String to Date
        DateFormat mesaDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mesaDateFormat.setTimeZone(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

        Date mesaDate = mesaDateFormat.parse(dateStr);

        // Parse text to Examenes
        ParseHelper helper = new ParseHelper();
        helper.setText(text);

        Mesa mesa = new Mesa();

        // Set WeekDay
        for(Mesa.WeekDay weekDay : Mesa.WeekDay.values()){
            if (weekDay.name().equals(mesaDay)){
                mesa.setWeekDay(weekDay);
                break;
            }
        }

        mesa.setFecha(mesaDate);
        ArrayList<Examen> examenes = new ParseHelperLogic().getExamenes(helper, dateStr);
        for(Examen examen : examenes){
            examen.setMesa(mesa);
        }
        mesa.setExamenes(examenes);

        return mesa;
    }

    public int getNroLlamado() {
        return nroLlamado;
    }

    public int getAñoLlamado() {
        return añoLlamado;
    }
}
