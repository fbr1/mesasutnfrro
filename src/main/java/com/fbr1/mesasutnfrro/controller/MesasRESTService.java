package com.fbr1.mesasutnfrro.controller;

import java.util.List;

import com.fbr1.mesasutnfrro.model.data.LlamadosData;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import org.springframework.web.bind.annotation.*;


@RestController
public class MesasRESTService {

    private final String baseRestUrl = "/rest";

    @RequestMapping(value= baseRestUrl,
                    method= RequestMethod.GET)
    public Llamado getLastLlamado(@RequestParam(value="esp", defaultValue="ALL") String esp){

        return new LlamadosData().getlastLlamado();
    }

    @RequestMapping(value= baseRestUrl + "/{year}",
                    method=RequestMethod.GET)
    public List<Llamado> getAllLlamadosOfYear(@PathVariable int year,
                                       @RequestParam(value="esp", defaultValue="ALL") String esp){

        return new LlamadosData().getLlamadosOfYear(year);
    }

    @RequestMapping(value= baseRestUrl + "/{year}/{numero}",
                    method=RequestMethod.GET)
    public Llamado getLlamadoOfYear(@PathVariable int year,
                                   @PathVariable int numero,
                                   @RequestParam(value="esp", defaultValue="ALL") String esp){

        return new LlamadosData().getLlamado(year,numero);
    }

}