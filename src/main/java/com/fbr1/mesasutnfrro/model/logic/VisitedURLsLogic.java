package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.VisitedURLsData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class VisitedURLsLogic {

    private VisitedURLsData visitedURLsData;
    private final static Logger logger = LoggerFactory.getLogger(VisitedURLsLogic.class);

    public VisitedURLsLogic(){
        visitedURLsData = new VisitedURLsData();
    }

    public Set<String> getAll(){
        Set<String> visitedURLs= null;
        try{
            visitedURLs =this.visitedURLsData.getAll();
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return visitedURLs;
    }

    public void addAll(Set<String> visitedURLs){
        try{
            this.visitedURLsData.addAll(visitedURLs);
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
    }

}
