package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.ApplicationVariablesData;
import com.fbr1.mesasutnfrro.model.entity.ApplicationVariables;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

public class UpdateLogic {

    private static final long UPDATE_INTERVAL = 2160000; // An hour in milliseconds
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static final String URL_SELECTOR = "a:containsOwn(ver listado)";
    private static final String CRAWLING_PAGE = "http://www.frro.utn.edu.ar/contenido.php?cont=528&subc=5";
    private Set<String> urls = null;

    private final static Logger logger = LoggerFactory.getLogger(UpdateLogic.class);

    public void setURLs(Set<String> urls){
        this.urls = urls;
    }

    public boolean isContentNew(){
        VisitedURLsLogic visitedURLsLogic = new VisitedURLsLogic();

        Set<String> processedURLs = visitedURLsLogic.getAll();

        if(!processedURLs.containsAll(this.urls)){
            visitedURLsLogic.addAll(this.urls);
            logger.info("Add URLs: ", this.urls);
            return true;
        }else{
            logger.info("URLs already seen");
        }
        return false;
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

    /**
     * Returns true if enough time has passed for a new update to be allowed.
     * The time required is set on the constant UPDATE_INTERVAL
     *
     * @return      whether is time or not for an update
     */
    static public boolean isTimeForUpdate(){

        Long currentTime = System.currentTimeMillis();
        ApplicationVariablesData appVarsData = new ApplicationVariablesData();

        ApplicationVariables appVars = appVarsData.get();

        Long lastUpdate = appVars.getLastupdate();

        if (lastUpdate == null) {

            // If it's the first execution of the webapp instance
            appVars.setLastupdate(currentTime);
            appVarsData.update(appVars);
            return true;

        } else {

            long timeSinceLastUpdate = currentTime - lastUpdate;

            if (timeSinceLastUpdate > UPDATE_INTERVAL) {
                // Update last update time
                appVars.setLastupdate(currentTime);
                appVarsData.update(appVars);
                return true;
            }
        }
        return false;

    }
}
