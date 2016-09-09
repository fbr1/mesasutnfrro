package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.LlamadosRepository;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LlamadosLogic {

    private final static Logger logger = LoggerFactory.getLogger(LlamadosLogic.class);

    public Llamado getLlamado(int year, int numero){
        Llamado llamado= null;
        try {
            llamado = llamadosRepository.findByAñoAndNumero(year, numero);
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return llamado;
    }

    public Llamado getlastLlamado(){
        Llamado llamado= null;
        try {
            llamado = llamadosRepository.findTopByOrderByDateDesc();
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return llamado;
    }

    public List<Llamado> getLlamadosOfYear(int year){
        List<Llamado> llamados= null;
        try {
            llamados = llamadosRepository.findByAño(year);
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return llamados;
    }

    public List<Llamado> getAllLlamados(){
        List<Llamado> llamados= null;
        try {
            llamados = (List<Llamado>)llamadosRepository.findAll();
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return llamados;
    }

    public void add(Llamado llamado){
        try{
            llamadosRepository.save(llamado);
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
    }

    @Autowired
    private LlamadosRepository llamadosRepository;
}
