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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LlamadosLogic {

    private final static Logger logger = LoggerFactory.getLogger(LlamadosLogic.class);

    public Llamado getLlamado(int year, int numero){
        return llamadosRepository.findByAñoAndNumero(year, numero);
    }

    public Llamado getlastLlamado(){
        return llamadosRepository.findTopByOrderByDateDesc();
    }

    public List<Llamado> getLlamadosOfYear(int year){
        return llamadosRepository.findByAño(year);
    }

    public List<Llamado> getAllLlamados(){
        return (List<Llamado>)llamadosRepository.findAll();
    }

    public Llamado findOneById(long id){
        return llamadosRepository.findById(id);
    }

    public Page<Llamado> getAllLlamadosByPage(Pageable pageable){
        return llamadosRepository.findByOrderByDateDesc(pageable);
    }

    public List<Llamado> getAllByWeekType(int weekType){

        // Validate that weekType is within range
        validateWeekType(weekType);

        return llamadosRepository.findByWeekType(weekType);
    }

    /**
     * Saves to DB the Llamado.
     * It replaces Materia objects from the Llamado with existing ones from the DB. If there are new
     * Materia objects, they are persisted in the DB.
     *
     * @param llamado - Llamado object to add
     */
    public Llamado add(Llamado llamado){

        // Check if the Llamado already exists
        if(llamadosRepository.existsByAñoAndNumber(llamado.getAño(), llamado.getNumero())){
            logger.info("The Llamado | Año: "+ llamado.getAño() + " Numero: " + llamado.getNumero() + " already exists");
            return null;
        }

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

        Llamado savedLlamado = llamadosRepository.save(llamado);
        logger.info("New Llamado added | Año: "+ llamado.getAño() + " Numero: " + llamado.getNumero());
        return savedLlamado;
    }

    /**
     * Builds and adds a Llamado from a list of Mesa objects
     *
     * @param mesas - List containing Mesa objects
     * @param añoLlamado - Int
     * @param numeroLlamado - Int
     */
    public Llamado buildAndAdd(List<Mesa> mesas,int añoLlamado, int numeroLlamado){

        Llamado llamado = new Llamado(añoLlamado, numeroLlamado, mesas.get(0).getFecha());

        int weekType = 0;

        for(Mesa mesa : mesas){
            weekType += mesa.getWeekDay().getWeekDayValue();
            mesa.setLlamado(llamado);
        }

        llamado.setWeekType(weekType);
        llamado.setMesas(mesas);

        return this.add(llamado);
    }
    /**
     * Validate that the weekType is within range: [1, 2^n], where n is the number of weekdays(7)
     *
     * @param weekType - int
     */
    public static void validateWeekType(int weekType){

        int min = 1;
        double max = Math.pow(2, Mesa.WeekDay.values().length);

        if(weekType < min || weekType > max){
            throw new IllegalArgumentException("WeekType must be between " + min + " and " + max);
        }

    }

    @Autowired
    private LlamadosRepository llamadosRepository;

    @Autowired
    private MateriasRepository materiasRepository;
}
