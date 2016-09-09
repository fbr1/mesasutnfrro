package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.ApplicationVariablesRepository;
import com.fbr1.mesasutnfrro.model.entity.ApplicationVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationVariablesLogic {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationVariablesLogic.class);

    public ApplicationVariables get(){
        ApplicationVariables applicationVariables= null;
        try{
            applicationVariables = applicationVariablesRepository.findTop();
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return applicationVariables;
    }

    public void update(ApplicationVariables applicationVariables){
        try{

            applicationVariablesRepository.save(applicationVariables);

        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
    }

    @Autowired
    private ApplicationVariablesRepository applicationVariablesRepository;

}
