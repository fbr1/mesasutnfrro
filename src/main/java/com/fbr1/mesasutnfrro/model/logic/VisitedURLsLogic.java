package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.VisitedURLsRepository;
import com.fbr1.mesasutnfrro.model.entity.VisitedURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class VisitedURLsLogic {

    private final static Logger logger = LoggerFactory.getLogger(VisitedURLsLogic.class);

    public Set<String> getAll(){
        Set<String> urlsStr = null;
        try{
            urlsStr = new HashSet<>();
            for(VisitedURL visitedURL : visitedURLsRepository.findAll()){
                urlsStr.add(visitedURL.getUrl());
            }
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return urlsStr;
    }

    public void addAll(Set<String> urlsStr){
        Set<VisitedURL> visitedURLs= new HashSet<>();
        for(String url : urlsStr){
            visitedURLs.add(new VisitedURL(url));
        }
        try{
            visitedURLsRepository.save(visitedURLs);
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
    }

    @Autowired
    private VisitedURLsRepository visitedURLsRepository;

}
