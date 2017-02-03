package com.fbr1.mesasutnfrro.model.logic;

import com.ecwid.maleorang.MailchimpException;
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
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UpdateLogic {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static final String URL_SELECTOR = "a:contains(ver listado)";
    private static final String CRAWLING_PAGE = "http://www.frro.utn.edu.ar/contenido.php?cont=528&subc=5";
    private Set<String> urls = null;
    private final static Logger logger = LoggerFactory.getLogger(UpdateLogic.class);


    /**
     * If the urls hasn't already been seen,
     * extracts the Llamado from the raw PDFs in the URLs and saves it.
     *
     */
    public void checkUpdatesAndCrawl() throws IOException, ParseException, MailchimpException {
        if(isContentNew()){

            updateLlamadosFromURLs(this.urls);

            // If all went well, store used URLs
            visitedURLsLogic.addAll(this.urls);

            // Create Campaign
            String campaign_id = subscribeLogic.createNewCampaign();

            // Notify subscription users of new content
            subscribeLogic.sendCampaign(campaign_id);

            logger.info("Add URLs: ", this.urls);
        }
    }

    /**
     * Downloads PDFs from the urls, extracts mesa, builds a Llamado and add it to permanent storage
     *
     * @param urls - Iterable of urls in Strings
     */
    public void updateLlamadosFromURLs(Iterable<String> urls) throws IOException,ParseException {
        List<Mesa> mesas = new ArrayList<>();
        MesasExtractor mesasExtractor = new MesasExtractor();

        for(String urlS : urls) {
            // Get and open pdf
            URL url = new URL(urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try(InputStream is = connection.getInputStream()){

                PDDocument pdd = PDDocument.load(is);

                // Extract mesa
                Mesa mesa = mesasExtractor.buildMesaFromPDF(pdd);
                mesas.add(mesa);
            }
        }

        llamadosLogic.buildAndAdd(mesas, mesasExtractor.getAñoLlamado(), mesasExtractor.getNroLlamado());
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
     * The time required is set on a ApplicationVariables object
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

            if (timeSinceLastUpdate > appVars.getUpdateInteval()) {
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

    @Autowired
    private SubscribeLogic subscribeLogic;
}
