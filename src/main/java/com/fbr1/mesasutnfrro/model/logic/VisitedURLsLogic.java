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
        Set<String> urlsStr;

        urlsStr = new HashSet<>();
        for(VisitedURL visitedURL : visitedURLsRepository.findAll()){
            urlsStr.add(visitedURL.getUrl());
        }

        return urlsStr;
    }

    public void addAll(Set<String> urlsStr){
        Set<VisitedURL> visitedURLs= new HashSet<>();
        for(String url : urlsStr){
            visitedURLs.add(new VisitedURL(url));
        }

        visitedURLsRepository.save(visitedURLs);
    }

    @Autowired
    private VisitedURLsRepository visitedURLsRepository;

}
