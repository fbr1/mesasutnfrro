package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.LlamadosRepository;
import com.fbr1.mesasutnfrro.model.entity.Mesa;
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

    /**
     * Builds and adds a Llamado from a list of Mesa objects
     *
     * @param mesas - List containing Mesa objects
     * @param añoLlamado - Int
     * @param numeroLlamado - Int
     */
    public void buildAndAdd(List<Mesa> mesas,int añoLlamado, int numeroLlamado){

        Llamado llamado = new Llamado(añoLlamado, numeroLlamado, mesas.get(0).getFecha());

        for(Mesa mesa : mesas){
            mesa.setLlamado(llamado);
        }

        llamado.setMesas(mesas);

        this.add(llamado);
    }

    @Autowired
    private LlamadosRepository llamadosRepository;
}
