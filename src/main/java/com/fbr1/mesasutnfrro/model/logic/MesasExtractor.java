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
        monthsMap.put("OTUBRE","10");
        monthsMap.put("NOVIEMBRE","11");
        monthsMap.put("DICIEMBRE","12");
    }

    /**
     * Extracts a Mesa from the given pdf
     *
     * @param pdd - PDDocument pdf document containing Examanes
     * @return      Mesa extracted from the pdf document
     */
    public Mesa processPDF(PDDocument pdd) throws IOException{

        PDFTextStripper stripper = new PDFTextStripper();
        String text=cleanText(stripper.getText(pdd));
        
        pdd.close();

        return extractMesa(text, this.mesaDate);
    }

    /**
     * Cleans and normalizes the text stripped from the Mesa pdf
     * The goal of this is to have a uniform format from which later it can be casted into objects
     *
     * @param oriText - Text stripped from the Mesa PDF
     * @return      String containing an uniform format of Examanes
     */
    public String cleanText(String oriText){
        StringBuilder stringBuilder = new StringBuilder();

        // Regex for 1-Mañana, 2-Tarde y 3-Noche
        String turnoRegex = "\\d-\\w\\p{L}+ ";

        // Regex for a year in the range 2000-2099
        Pattern yearRegex = Pattern.compile("\\d{1,2} .*(\\d{4})");

        oriText = oriText.replaceAll(turnoRegex, "");

        String[] lines = oriText.split(System.getProperty("line.separator"));

        for(String line : lines){

            Matcher matcher = yearRegex.matcher(line);

            // Remove unnecessary lines
            if(line.contains("LLAMADO")){
                if(nroLlamado == 0 ){
                    nroLlamado = Integer.valueOf(line.substring(0, 1));
                }

                line = "";
            }else if(matcher.find()){

                this.mesaDate = this.getDate(line);

                if(añoLlamado == 0){
                    añoLlamado = Integer.valueOf(matcher.group(1));
                }
                line = "";
            }else if(line.contains("DISTRIBUCION DE AULAS ") || line.contains("Horario")){
                line = "";
            }

            line = removeExtraSpaces(line);

            // If it's not a blank line
            if(line.trim().length() > 1){

                stringBuilder.append(line).append(System.getProperty("line.separator"));

            }

        }
        return normalizeText(stringBuilder.toString());
    }
    /**
     * Extract date from the PDF date line
     *
     * @param line - line containing a dateRegex format
     * @return      String with the Mesa's date in (yyyy-MM-dd) format
     */
    private String getDate(String line){
        String day = null;
        String month = null;
        String year = null;
        Pattern dateRegex = Pattern.compile("(?<day>\\d{1,2}) DE (?<month>.*) DE (?<year>\\d{4})");
        Matcher matcher = dateRegex.matcher(line);
        if(matcher.find()){
            day = matcher.group("day");
            month = MesasExtractor.monthsMap.get(matcher.group("month").toUpperCase());
            year = matcher.group("year");
        }
        return year + "-" + month + "-" + day;
    }

    /**
     * Normalizes an already cleaned text stripped from Mesa PDF.
     * Makes sure that every Especialidad, Aula and Examen is in its own line.
     *
     * @param text - text cleaned from the Mesa PDF
     * @return      String containing an normalized format of Examanes
     */
    private String normalizeText(String text){
        StringBuilder stringBuilder = new StringBuilder();

        // Regex for especialidades
        Pattern especialidadRegex = Pattern.compile("(ISI ?|IE ?|IQ ?|IC ?|IM ?)");

        // Regex matching one or more aulas
        Pattern aulaRegex = Pattern.compile("([\\d{1,3}\\/]+\\d{1,3} ?|\\d{1,3} ?|SUM)");

        // Regex matching aula in line
        Pattern aulaLineRegex = Pattern.compile("(^\\d{2,3}|SUM)$");

        // Regex matching a line with only the time
        Pattern hoursPattern = Pattern.compile("(^\\d{1,2}:\\d{2}:\\d{2}$)");

        String[] lines = text.split(System.getProperty("line.separator"));

        for(int i = 0; i< lines.length; i++){

            String line = lines[i];

            boolean skip = false;

            // Make all the data format homogeneous
            Matcher matcher = especialidadRegex.matcher(line);
            boolean esEspecialidad = matcher.find();
            if(esEspecialidad){
                if(matcher.group(1).contains(" ")){
                    line = line.replace(matcher.group(1),
                            matcher.group(1).substring(0, matcher.group(1).length()-1)+System.getProperty("line.separator"));
                }
            }

            matcher = aulaRegex.matcher(line);
            boolean esAula = matcher.find();
            if(esAula) {
                if (matcher.group(1).contains(" ")) {
                    line = line.replace(matcher.group(1),
                            matcher.group(1).substring(0, matcher.group(1).length() - 1) + System.getProperty("line.separator"));
                } else {
                    // Special case for two aulas in different lines
                    if(aulaLineRegex.matcher(line).find() && i-1 >=0 && i+1 < lines.length){

                        matcher = aulaLineRegex.matcher(lines[i - 1]);
                        if (matcher.find()) {
                            line = matcher.group(1) + "/" + line;
                        } else {
                            matcher = aulaLineRegex.matcher(lines[i + 1]);
                            if (matcher.find()) {
                                skip = true;
                            }
                        }
                    }

                }

            }

            // Special case for hours overlapping examenes' hour

            if(!esEspecialidad && !esAula){
                if(i-1>=0){
                    matcher = hoursPattern.matcher(lines[i-1]);
                    if(matcher.find()){
                        line += " " + matcher.group(1);
                    }else if(i+1<lines.length){
                        matcher = hoursPattern.matcher(lines[i+1]);
                        if(matcher.find()){
                            line += " " + matcher.group(1);
                            i++;
                        }else{
                            skip = true;
                        }
                    }else{
                        skip = true;
                    }
                }
            }


            if(!skip){
                stringBuilder.append(line).append(System.getProperty("line.separator"));
            }

        }

        return stringBuilder.toString();
    }

    private String removeExtraSpaces(String str){

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
    private Mesa extractMesa(String text, String dateStr){

        // Transform date from String to Date
        DateFormat mesaDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date mesaDate = new Date();
        try {
            mesaDate = mesaDateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }

        // Parse text to Examenes
        ParseHelper helper = new ParseHelper();
        helper.setText(text);

        Mesa mesa = new Mesa();
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
