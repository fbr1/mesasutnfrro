package com.fbr1.mesasutnfrro.controller;

import java.util.List;

import com.fbr1.mesasutnfrro.model.data.ExamenRepository;
import com.fbr1.mesasutnfrro.model.data.MesaRepository;
import com.fbr1.mesasutnfrro.model.entity.Error;
import com.fbr1.mesasutnfrro.model.entity.Examen;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import com.fbr1.mesasutnfrro.model.entity.Mesa;
import com.fbr1.mesasutnfrro.model.exception.ObjetoNotFoundException;
import com.fbr1.mesasutnfrro.model.logic.LlamadosLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
public class MesasRESTService {

    private final static Logger logger = LoggerFactory.getLogger(MesasRESTService.class);

    @RequestMapping(value= "/",
                    method= RequestMethod.GET)
    public Llamado getLastLlamado(@RequestParam(value="esp", defaultValue="ALL") String esp){

        return llamadosLogic.getlastLlamado();
    }

    @RequestMapping(value= "/{year}",
                    method=RequestMethod.GET)
    public List<Llamado> getAllLlamadosOfYear(@PathVariable int year,
                                       @RequestParam(value="esp", defaultValue="ALL") String esp){

        return llamadosLogic.getLlamadosOfYear(year);
    }

    @RequestMapping(value=  "/{year}/{numero}",
                    method=RequestMethod.GET)
    public Llamado getLlamadoOfYear(@PathVariable int year,
                                   @PathVariable int numero,
                                   @RequestParam(value="esp", defaultValue="ALL") String esp){

        return llamadosLogic.getLlamado(year,numero);
    }

    @RequestMapping(value= "/examen/{id}", method = RequestMethod.PUT)
    public Llamado updateExamen(@PathVariable int id, @RequestBody Examen examen) {
        Examen oldExamen = examenRepository.findOne(id);
        if (oldExamen == null) { throw new ObjetoNotFoundException("No se ha podido actualizar el examen.");}
        oldExamen.setAula(examen.getAula());
        oldExamen.setFecha(examen.getFecha());
        Mesa mesaActual = oldExamen.getMesa();
        if((examen.getMesa() != null) && (oldExamen.getMesa().getID() != examen.getMesa().getID())) {
            Mesa mesaNueva = mesaRepository.findOne(examen.getMesa().getID());
            if (mesaNueva == null) {throw new ObjetoNotFoundException("La mesa a la cuál quiere cambiar el examen no existe");};
            mesaActual.getExamenes().remove(oldExamen);
            mesaNueva.getExamenes().add(oldExamen);
            mesaRepository.save(mesaActual);
            mesaRepository.save(mesaNueva);
            oldExamen.setMesa(mesaNueva);
        }
        examenRepository.save(oldExamen);
        Llamado llamado = mesaActual.getLlamado();
        return llamadosLogic.getLlamado(llamado.getAño(),llamado.getNumero());
    }

    @ExceptionHandler(ObjetoNotFoundException.class)
    public ResponseEntity<Error> objetoNotFound(ObjetoNotFoundException e) {
        String message = e.getMessage();
        Error error = new Error(1, message);
        return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
    }

    @Autowired
    private LlamadosLogic llamadosLogic;

    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private MesaRepository mesaRepository;


}