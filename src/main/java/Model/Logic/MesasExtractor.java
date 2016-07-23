package Model.Logic;

import Model.Entity.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static final String URL_SELECTOR = "a:containsOwn(ver listado)";
    private static final String CRAWLING_PAGE = "http://www.frro.utn.edu.ar/contenido.php?cont=528&subc=5";
    private final Logger logger = LoggerFactory.getLogger(MesasExtractor.class);
    private Set<String> urls = null;

    public void setURLs(Set<String> urls){
        this.urls = urls;
    }

    public boolean isContentNew(){
        VisitedURLsLogic visitedURLsLogic = new VisitedURLsLogic();

        Set<String> processedURLs = visitedURLsLogic.getAll();

        if(!processedURLs.containsAll(this.urls)){
            visitedURLsLogic.addAll(this.urls);
            return true;
        }else{
            logger.info("URLs already seen");
        }
        return false;
    }

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



    /**
     * Returns true if crawling was successful.
     *
     * @param url - The URL to visit
     * @return      whether the crawling was successful
     */
    public boolean crawl(String url){
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            if(connection.response().statusCode() == 200)
            {
                logger.info("\n**Visiting** Received web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html"))
            {
                logger.info("**Failure** Retrieved something other than HTML");
                return false;
            }
            Elements urlsOnPage = htmlDocument.select(URL_SELECTOR);
            logger.info("Found (" + urlsOnPage.size() + ") urls");
            for(Element link : urlsOnPage)
            {
                String urle = link.absUrl("href");
                this.urls.add(urle);
                logger.info("Found url: " + urle);
            }
            return true;
        }
        catch(IOException ioe)
        {
            // We were not successful in our HTTP request
            logger.error(ioe.getMessage());
            return false;
        }
    }
}
