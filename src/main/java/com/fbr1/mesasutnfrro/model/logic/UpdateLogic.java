package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.entity.ApplicationVariables;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import com.fbr1.mesasutnfrro.model.entity.Mesa;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UpdateLogic {

    private static final long UPDATE_INTERVAL = 2160000; // An hour in milliseconds
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static final String URL_SELECTOR = "a:containsOwn(ver listado)";
    private static final String CRAWLING_PAGE = "http://www.frro.utn.edu.ar/contenido.php?cont=528&subc=5";
    private Set<String> urls = null;
    private final static Logger logger = LoggerFactory.getLogger(UpdateLogic.class);


    /**
     * If the urls hadn't already been seen,
     * extracts the Llamado from the raw PDFs in the urls and saves it.
     *
     */
    public void checkUpdates(){
        if(isContentNew()){

            try{
                List<Mesa> mesas = new ArrayList<>();
                MesasExtractor mesasExtractor = new MesasExtractor();
                Llamado llamado = new Llamado();

                for(String urlS : this.urls) {

                    // Get date
                    String date = getDateFromUrl(urlS);

                    // Get and open pdf
                    URL url = new URL(urlS);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream is = connection.getInputStream();
                    PDDocument pdd = PDDocument.load(is);
                    connection.disconnect();

                    // Extract mesa
                    Mesa mesa = mesasExtractor.processPDF(pdd, date);
                    mesa.setLlamado(llamado);
                    mesas.add(mesa);

                }

                Date dateLlamado = mesas.get(0).getFecha();
                int añoLlamado = mesasExtractor.getAño();
                int numeroLlamado = mesasExtractor.getNroLlamado();

                llamado.setAño(añoLlamado);
                llamado.setNumero(numeroLlamado);
                llamado.setDate(dateLlamado);
                llamado.setMesas(mesas);

                llamadosLogic.add(llamado);
                logger.info("New Llamado added | Año: "+ añoLlamado + " Numero: " + numeroLlamado);

            }catch (MalformedURLException urle){
                logger.error(urle.getMessage(), urle);
            }catch (IOException ioe){
                logger.error(ioe.getMessage(), ioe);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }

        }
    }

    /**
     * Returns date String in the format:(dd-MM-yy) extracted from 'url'
     *
     * @param url - Url string to extract date
     * @return      date extracted from url string
     */
    private String getDateFromUrl (String url) throws Exception{
        Pattern p = Pattern.compile("(\\d{1,2}-\\d{1,2}-\\d{1,2})");
        Matcher m = p.matcher(url);

        String dateStr;

        if (m.find()) {
            dateStr = m.group(1);
            System.out.println(m.group(1));
        }else{
            throw new Exception("The url: " + url + "doesn't have a date");
        }

        return dateStr;
    }

    /**
     * Checks for urls in CRAWLING_PAGE and compares them to urls already seen
     *
     * @return      whether the current URLs are new to the system or not
     */
    private boolean isContentNew(){

        // If crawling is successful
        if( crawl(CRAWLING_PAGE) ){

            Set<String> processedURLs = visitedURLsLogic.getAll();

            if(!this.urls.isEmpty() && !processedURLs.containsAll(this.urls)){
                visitedURLsLogic.addAll(this.urls);
                logger.info("Add URLs: ", this.urls);
                return true;
            }else{
                logger.info("URLs already seen");
            }
        }
        return false;
    }

    /**
     * Tries to crawl the String 'url'
     * If crawling was successful, stores the Set of urls inside URL_SELECTOR in the
     * instance variable urls and returns true
     *
     * 'Crawl' is the action of extracting html content from a given url
     * Crawling is successful if a page returns status 200 and has HTML code inside
     *
     * @param url - The URL to visit
     * @return      Whether the crawling was successful
     */
    private boolean crawl(String url){
        try
        {
            this.urls = new HashSet<>();
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
            logger.error(ioe.getMessage(), ioe);
            return false;
        }
    }

    /**
     * Returns true if enough time has passed for a new update to be allowed.
     * The time required is set on the constant UPDATE_INTERVAL
     *
     * @return      whether is time or not for an update
     */
    public boolean isTimeForUpdate(){

        Long currentTime = System.currentTimeMillis();

        ApplicationVariables appVars = applicationVariablesLogic.get();

        Long lastUpdate = appVars.getLastupdate();

        if (lastUpdate == null) {

            // If it's the first execution of the webapp instance
            appVars.setLastupdate(currentTime);
            applicationVariablesLogic.update(appVars);
            return true;

        } else {

            long timeSinceLastUpdate = currentTime - lastUpdate;

            if (timeSinceLastUpdate > UPDATE_INTERVAL) {
                // Update last update time
                appVars.setLastupdate(currentTime);
                applicationVariablesLogic.update(appVars);
                return true;
            }
        }
        return false;

    }

    @Autowired
    private ApplicationVariablesLogic applicationVariablesLogic;

    @Autowired
    private LlamadosLogic llamadosLogic;

    @Autowired
    private VisitedURLsLogic visitedURLsLogic;
}
