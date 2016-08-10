package com.fbr1.mesasutnfrro.controller;

import com.fbr1.mesasutnfrro.model.logic.UpdateLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@SuppressWarnings("serial")
public class MesasController {

    final Logger logger = LoggerFactory.getLogger(com.fbr1.mesasutnfrro.controller.MesasController.class);

    @RequestMapping(value = "/updatemesas")
    public String updatemesas(Model model) {
        String statusText;
        if(UpdateLogic.isTimeForUpdate()){
            statusText = "It's time for an update";
        }else{
            statusText = "It's not time for an update";
        }
        model.addAttribute("statusText", statusText);
        return "updatemesas";
    }

}

