package com.fbr1.mesasutnfrro.controller;

import com.fbr1.mesasutnfrro.model.logic.UpdateLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.ParseException;

@Controller
@SuppressWarnings("serial")
public class MesasController {

    final Logger logger = LoggerFactory.getLogger(com.fbr1.mesasutnfrro.controller.MesasController.class);

    @RequestMapping(value = "/updatemesas")
    public String updatemesas(Model model) throws IOException, ParseException{
        String statusText = "no update";
        if(updateLogic.isTimeForUpdate()){
            updateLogic.checkUpdates();
            statusText = "update";
        }
        model.addAttribute("statusText", statusText);
        return "updatemesas";
    }

    @Autowired
    private  UpdateLogic updateLogic;

}

