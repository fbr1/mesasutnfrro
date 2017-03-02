package com.fbr1.mesasutnfrro.controller;

import com.ecwid.maleorang.MailchimpException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import com.fbr1.mesasutnfrro.model.forms.SubscribeForm;
import com.fbr1.mesasutnfrro.model.logic.LlamadosLogic;
import com.fbr1.mesasutnfrro.model.logic.SubscribeLogic;
import com.fbr1.mesasutnfrro.model.logic.UpdateLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

@Controller
@SuppressWarnings("serial")
public class MesasController {

    final Logger logger = LoggerFactory.getLogger(com.fbr1.mesasutnfrro.controller.MesasController.class);

    @RequestMapping(value = "/updatemesas")
    public ResponseEntity updatemesas() throws IOException, ParseException, MailchimpException {
        if(updateLogic.isTimeForUpdate()){
            updateLogic.checkUpdatesAndCrawl();
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public String home(@RequestParam(value = "error", defaultValue = "false") boolean error, Model model) throws JsonProcessingException {
        model.addAttribute("loginError", error);

        Llamado llamado = llamadosLogic.getlastLlamado();

        // Convert to JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String jsonData = mapper.writeValueAsString(llamado);

        model.addAttribute("data",jsonData);
        return "index";
    }


    @PostMapping(value = "/subscribe")
    public ResponseEntity subscribe(@Valid SubscribeForm subscribeForm, BindingResult bindingResult)
        throws IOException, MailchimpException{
        // TODO change
        if(bindingResult.hasErrors()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        subscribeLogic.saveSubscriber(subscribeForm);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/login_error")
    public ResponseEntity login_error(@Valid SubscribeForm subscribeForm, BindingResult bindingResult)
            throws IOException, MailchimpException{
        // TODO change
        if(bindingResult.hasErrors()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        subscribeLogic.saveSubscriber(subscribeForm);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Autowired
    private LlamadosLogic llamadosLogic;

    @Autowired
    private  UpdateLogic updateLogic;

    @Autowired
    private SubscribeLogic subscribeLogic;

}

