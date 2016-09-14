package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.LlamadosRepository;
import com.fbr1.mesasutnfrro.model.data.MateriasRepository;
import com.fbr1.mesasutnfrro.model.entity.Examen;
import com.fbr1.mesasutnfrro.model.entity.Materia;
import com.fbr1.mesasutnfrro.model.entity.Mesa;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * Saves to DB the Llamado.
     * It replaces Materia objects from the Llamado with existing ones from the DB. If there are new
     * Materia objects, they are persisted in the DB.
     *
     * @param llamado - Llamado object to add
     */
    public void add(Llamado llamado){

        // Extract all unique materias
        Set<Materia> materiasSeen = new HashSet<>();

        for (Mesa mesa : llamado.getMesas()){
            for(Examen examen : mesa.getExamenes()){
                materiasSeen.add(examen.getMateria());
            }
        }

        //
        // Determine if there are new materias and save them.
        //
        Set<Materia> materiasStored = new HashSet<>((List<Materia>)materiasRepository.findAll());
        // materiasSeen - materiasStored
        materiasSeen.removeAll(materiasStored);

        List<Materia> materiasStoredUpdated = ((List<Materia>)materiasRepository.save(materiasSeen));
        materiasStoredUpdated.addAll(materiasStored);

        // Replace Materia in each Examen with the persisted ones from the DB.
        for (Mesa mesa : llamado.getMesas()){
            for(Examen examen : mesa.getExamenes()){
                Materia mat = examen.getMateria();
                int i = materiasStoredUpdated.indexOf(mat);
                examen.setMateria(materiasStoredUpdated.get(i));
            }
        }

        llamadosRepository.save(llamado);
        logger.info("New Llamado added | Año: "+ llamado.getAño() + " Numero: " + llamado.getNumero());
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

    @Autowired
    private MateriasRepository materiasRepository;
}
