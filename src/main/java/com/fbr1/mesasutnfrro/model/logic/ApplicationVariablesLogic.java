package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.ApplicationVariablesData;
import com.fbr1.mesasutnfrro.model.entity.ApplicationVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationVariablesLogic {

    private ApplicationVariablesData applicationVariablesData;
    private final static Logger logger = LoggerFactory.getLogger(ApplicationVariablesLogic.class);

    public ApplicationVariablesLogic(){
        applicationVariablesData = new ApplicationVariablesData();
    }

    public ApplicationVariables get(){
        ApplicationVariables applicationVariables= null;
        try{
            applicationVariables =this.applicationVariablesData.get();
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return applicationVariables;
    }

    public void update(ApplicationVariables applicationVariables){
        try{

            this.applicationVariablesData.update(applicationVariables);

        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
    }

}
