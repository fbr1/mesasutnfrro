package com.fbr1.mesasutnfrro.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import com.fbr1.mesasutnfrro.model.logic.LlamadosLogic;
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

    @RequestMapping(value = "/")
    public String home(Model model) throws JsonProcessingException {
        Llamado llamado = llamadosLogic.getlastLlamado();

        // Convert to JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonData = mapper.writeValueAsString(llamado);

        model.addAttribute("data",jsonData);
        return "index";
    }

    @Autowired
    private LlamadosLogic llamadosLogic;

    @Autowired
    private  UpdateLogic updateLogic;

}

