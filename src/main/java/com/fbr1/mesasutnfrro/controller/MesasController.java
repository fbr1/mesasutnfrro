package com.fbr1.mesasutnfrro.controller;

import com.ecwid.maleorang.MailchimpException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fbr1.mesasutnfrro.forms.MesaPDFValidator;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import com.fbr1.mesasutnfrro.forms.SubscribeForm;
import com.fbr1.mesasutnfrro.model.logic.LlamadosLogic;
import com.fbr1.mesasutnfrro.model.logic.SubscribeLogic;
import com.fbr1.mesasutnfrro.model.logic.UpdateLogic;
import com.fbr1.mesasutnfrro.util.MesasUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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

    @PostMapping(value = "/uploadmesas")
    public ResponseEntity uploadMesas(@RequestParam("file") MultipartFile[] files) throws IOException, ParseException {

        MesaPDFValidator mesaPDFValidator = new MesaPDFValidator(files);

        if(!mesaPDFValidator.isValid()){
            logger.info("Error al subir llamados: " + mesaPDFValidator.getMessage());
            return new ResponseEntity<>(mesaPDFValidator.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List<String> filePaths = new ArrayList<>();

        Files.createDirectories(Paths.get("mesas"));
        for(MultipartFile uploadedFile : files) {

            // Get the filename and build the local file path
            String filename = uploadedFile.getOriginalFilename();
            String filepath = Paths.get("mesas", filename).toString();

            // Save the file
            byte [] byteArr=uploadedFile.getBytes();
            InputStream inputStream = new ByteArrayInputStream(byteArr);
            MesasUtil.saveToDisk(inputStream,filepath);

            filePaths.add(filepath);
        }

        updateLogic.updateLlamadosFromFiles(filePaths);

        return new ResponseEntity(HttpStatus.OK);
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

