package com.fbr1.mesasutnfrro.controller;

import java.util.List;

import com.fbr1.mesasutnfrro.model.entity.Llamado;
import com.fbr1.mesasutnfrro.model.logic.LlamadosLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MesasRESTService {

    private final static Logger logger = LoggerFactory.getLogger(MesasRESTService.class);

    private final String baseRestUrl = "/rest";

    @RequestMapping(value= baseRestUrl,
                    method= RequestMethod.GET)
    public Llamado getLastLlamado(@RequestParam(value="esp", defaultValue="ALL") String esp){

        return llamadosLogic.getlastLlamado();
    }

    @RequestMapping(value= baseRestUrl + "/{year}",
                    method=RequestMethod.GET)
    public List<Llamado> getAllLlamadosOfYear(@PathVariable int year,
                                       @RequestParam(value="esp", defaultValue="ALL") String esp){

        return llamadosLogic.getLlamadosOfYear(year);
    }

    @RequestMapping(value= baseRestUrl + "/{year}/{numero}",
                    method=RequestMethod.GET)
    public Llamado getLlamadoOfYear(@PathVariable int year,
                                   @PathVariable int numero,
                                   @RequestParam(value="esp", defaultValue="ALL") String esp){

        return llamadosLogic.getLlamado(year,numero);
    }

    @Autowired
    private LlamadosLogic llamadosLogic;


}