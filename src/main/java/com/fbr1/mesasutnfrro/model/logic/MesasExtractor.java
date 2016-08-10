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

    private final Logger logger = LoggerFactory.getLogger(MesasExtractor.class);

    public Mesa processPDF(PDDocument pdd, String date){
        Mesa mesa = null;
        try {

            PDFTextStripper stripper = new PDFTextStripper();
            String text=cleanText(stripper.getText(pdd));

            pdd.close();
            mesa = extractMesa(text, date);
        }
        catch(IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return mesa;
    }

    public String cleanText(String oriText){
        StringBuilder stringBuilder = new StringBuilder();

        // Regex for 1-Mañana, 2-Tarde y 3-Noche
        String turnoRegex = "\\d\\-\\w\\p{L}+ ";

        // Regex for a year in the range 2000-2099
        Pattern yearRegex = Pattern.compile("20\\d{2}");

        // Regex for especialidades
        Pattern especialidadRegex = Pattern.compile("(ISI |IE |IQ |IC |IM )");

        // Regex matching one or more aulas
        Pattern aulaRegex = Pattern.compile("(\\d{3}\\S+|\\d{3}) ");

        oriText = oriText.replaceAll(turnoRegex, "");

        String[] lines = oriText.split(System.getProperty("line.separator"));

        for(String line : lines){

            Matcher matcher = yearRegex.matcher(line);

            // Remove unnecessary lines
            if(line.contains("DISTRIBUCION DE AULAS ") || line.contains("LLAMADO") ||
                    line.contains("Horario") || matcher.find()){
                line = "";
            }

            // If it's not a blank line
            if(line.trim().length() > 1){

                line = removeExtraSpaces(line);

                // Make all the data format homogeneous
                matcher = especialidadRegex.matcher(line);
                if(matcher.find()){
                    line = line.replace(matcher.group(1),matcher.group(1)+System.getProperty("line.separator"));
                }

                matcher = aulaRegex.matcher(line);
                if(matcher.find()){
                    line = line.replace(matcher.group(1)+" ",matcher.group(1)+System.getProperty("line.separator"));
                }

                stringBuilder.append(line).append(System.getProperty("line.separator"));
            }

        }
        return stringBuilder.toString();
    }

    private String removeExtraSpaces(String str){
        // Remove extra white space at the end
        if(str.charAt(str.length()-1)==' '){
            str = str.substring(0, str.length()-1);
        }
        // Remove extra white space at the beginning
        if(str.charAt(0)==' '){
            str = str.substring(1, str.length());
        }
        return str;
    }

    public Mesa extractMesa(String text, String date){

        // Transform date from String to Date
        DateFormat mesaDateFormat = new SimpleDateFormat("dd-MM-yy");
        Date mesaDate = new Date();
        try {
            mesaDate = mesaDateFormat.parse(date);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }

        // Parse text to Examenes
        ParseHelper helper = new ParseHelper();
        helper.setText(text);
        ArrayList<Examen> examenes = new ParseHelperLogic().getExamenes(helper, date);

        return new Mesa(mesaDate, examenes);
    }

}
