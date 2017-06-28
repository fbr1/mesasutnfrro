package com.fbr1.mesasutnfrro.controller;

import java.util.List;

import com.fbr1.mesasutnfrro.model.data.ExamenRepository;
import com.fbr1.mesasutnfrro.model.data.MesaRepository;
import com.fbr1.mesasutnfrro.model.entity.Error;
import com.fbr1.mesasutnfrro.model.entity.Examen;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import com.fbr1.mesasutnfrro.model.entity.Mesa;
import com.fbr1.mesasutnfrro.model.exception.MesasUtnFrroException;
import com.fbr1.mesasutnfrro.model.logic.ExamenLogic;
import com.fbr1.mesasutnfrro.model.logic.LlamadosLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
public class MesasRESTService {

    private final static Logger logger = LoggerFactory.getLogger(MesasRESTService.class);


    @RequestMapping(value= "/llamado/last",
                    method= RequestMethod.GET)
    public Llamado getLastLlamado(@RequestParam(value="esp", defaultValue="ALL") String esp){

        return llamadosLogic.getlastLlamado();
    }

    @RequestMapping(value= "/llamado/list",
            method= RequestMethod.GET)
    public Page<Llamado> getLlamadosPaginado(Pageable pageable){

        return llamadosLogic.getAllLlamadosByPage(pageable);
    }

    @RequestMapping(value= "/llamado/list/{year}",
                    method=RequestMethod.GET)
    public List<Llamado> getAllLlamadosOfYear(@PathVariable int year,
                                       @RequestParam(value="esp", defaultValue="ALL") String esp){

        return llamadosLogic.getLlamadosOfYear(year);
    }

    @RequestMapping(value=  "/llamado/list/{year}/{numero}",
                    method=RequestMethod.GET)
    public Llamado getLlamadoOfYear(@PathVariable int year,
                                   @PathVariable int numero,
                                   @RequestParam(value="esp", defaultValue="ALL") String esp){

        return llamadosLogic.getLlamado(year,numero);
    }

    @RequestMapping(value= "/examen/{id}", method = RequestMethod.PUT)
    public Llamado updateExamen(@PathVariable long id, @RequestBody Examen examen) {
        Examen savedExamen = examenLogic.update(examen);
        return savedExamen.getMesa().getLlamado();
    }

    @RequestMapping(value= "/examen/{id}", method = RequestMethod.DELETE)
    public Llamado deleteExamen(@PathVariable long id) {

        long llamadoId = examenLogic.findLlamadoIdByExamenId(id);

        examenLogic.delete(id);

        return llamadosLogic.findOneById(llamadoId);
    }

    @Autowired
    private LlamadosLogic llamadosLogic;

    @Autowired
    private ExamenLogic examenLogic;

    @Autowired
    private MesaRepository mesaRepository;


}