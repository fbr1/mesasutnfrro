package com.fbr1.mesasutnfrro.controller;

import com.ecwid.maleorang.MailchimpException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fbr1.mesasutnfrro.forms.ExamenForm;
import com.fbr1.mesasutnfrro.forms.ExamenFormValidator;
import com.fbr1.mesasutnfrro.forms.MesaPDFValidator;
import com.fbr1.mesasutnfrro.model.entity.Examen;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import com.fbr1.mesasutnfrro.forms.SubscribeForm;
import com.fbr1.mesasutnfrro.model.exception.MesasUtnFrroException;
import com.fbr1.mesasutnfrro.model.logic.ExamenLogic;
import com.fbr1.mesasutnfrro.model.logic.LlamadosLogic;
import com.fbr1.mesasutnfrro.model.logic.SubscribeLogic;
import com.fbr1.mesasutnfrro.model.logic.UpdateLogic;
import com.fbr1.mesasutnfrro.util.MesasUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
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

    @InitBinder("ExamenForm")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(new ExamenFormValidator());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/updatemesas")
    public ResponseEntity updatemesas() throws IOException, ParseException, MailchimpException {
        if(updateLogic.isTimeForUpdate()){
            updateLogic.checkUpdatesAndCrawl();
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public String home(@RequestParam(value = "error", defaultValue = "false") boolean error, Model model){
        model.addAttribute("loginError", error);

        return "index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/examen/new")
    public Llamado newExamen(@Valid ExamenForm examenForm, BindingResult bindingResult)
            throws IOException, MailchimpException{
        if(bindingResult.hasErrors()){
            logger.info("Error al crear examen: " + bindingResult.getAllErrors());
            throw new MesasUtnFrroException("Error al validar el examen",HttpStatus.BAD_REQUEST);
        }
        Examen examen = examenLogic.saveExamen(examenForm);
        return examen.getMesa().getLlamado();
    }

    @Autowired
    private ExamenLogic examenLogic;

    @Autowired
    private LlamadosLogic llamadosLogic;

    @Autowired
    private UpdateLogic updateLogic;

    @Autowired
    private SubscribeLogic subscribeLogic;

}

