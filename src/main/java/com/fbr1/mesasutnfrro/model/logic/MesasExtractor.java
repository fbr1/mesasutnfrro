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

    private String nroLlamado = null;
    private String año = null;

    public Mesa processPDF(PDDocument pdd, String dateStr){
        Mesa mesa = null;
        try {

            PDFTextStripper stripper = new PDFTextStripper();
            String text=cleanText(stripper.getText(pdd));

            pdd.close();
            mesa = extractMesa(text, dateStr);
        }
        catch(IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return mesa;
    }

    public String cleanText(String oriText){
        StringBuilder stringBuilder = new StringBuilder();

        // Regex for 1-Mañana, 2-Tarde y 3-Noche
        String turnoRegex = "\\d-\\w\\p{L}+ ";

        // Regex for a year in the range 2000-2099
        Pattern yearRegex = Pattern.compile("(20\\d{2})");

        oriText = oriText.replaceAll(turnoRegex, "");

        String[] lines = oriText.split(System.getProperty("line.separator"));

        for(String line : lines){

            Matcher matcher = yearRegex.matcher(line);

            // Remove unnecessary lines
            if(line.contains("LLAMADO")){
                if(nroLlamado == null){
                    nroLlamado = line.substring(0,1);
                }

                line = "";
            }else if(matcher.find()){
                if(año == null){
                    año = matcher.group(1);
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
                            String asd = matcher.group(1);
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

    private Mesa extractMesa(String text, String dateStr){

        // Transform date from String to Date
        DateFormat mesaDateFormat = new SimpleDateFormat("dd-MM-yy");
        Date mesaDate = new Date();
        try {
            mesaDate = mesaDateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }

        // Parse text to Examenes
        ParseHelper helper = new ParseHelper();
        helper.setText(text);
        ArrayList<Examen> examenes = new ParseHelperLogic().getExamenes(helper, dateStr);

        return new Mesa(mesaDate, examenes);
    }

    public String getNroLlamado() {
        return nroLlamado;
    }

}
