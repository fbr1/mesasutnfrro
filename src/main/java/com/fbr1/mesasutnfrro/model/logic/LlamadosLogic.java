package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.LlamadosData;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LlamadosLogic {
    private LlamadosData llamadosData;
    private final static Logger logger = LoggerFactory.getLogger(VisitedURLsLogic.class);

    public LlamadosLogic(){
        llamadosData = new LlamadosData();
    }


    public Llamado getLlamado(int year, int numero){
        Llamado llamado= null;
        try {
            llamado = this.llamadosData.getLlamado(year,numero);
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return llamado;
    }

    public Llamado getlastLlamado(){
        Llamado llamado= null;
        try {
            llamado = this.llamadosData.getlastLlamado();
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return llamado;
    }

    public List<Llamado> getLlamadosOfYear(int year){
        List<Llamado> llamados= null;
        try {
            llamados = this.llamadosData.getLlamadosOfYear(year);
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return llamados;
    }

    public List<Llamado> getAllLlamados(){
        List<Llamado> llamados= null;
        try {
            llamados = this.llamadosData.getAll();
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
        return llamados;
    }

    public void add(Llamado llamado){
        try{
            this.llamadosData.add(llamado);
        }catch(Exception Ex){
            logger.error(Ex.getMessage(), Ex);
        }
    }
}
