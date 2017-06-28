package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.forms.ExamenForm;
import com.fbr1.mesasutnfrro.model.data.ExamenRepository;
import com.fbr1.mesasutnfrro.model.data.MateriasRepository;
import com.fbr1.mesasutnfrro.model.data.MesaRepository;
import com.fbr1.mesasutnfrro.model.entity.Examen;
import com.fbr1.mesasutnfrro.model.entity.Llamado;
import com.fbr1.mesasutnfrro.model.entity.Materia;
import com.fbr1.mesasutnfrro.model.entity.Mesa;
import com.fbr1.mesasutnfrro.model.exception.MesasUtnFrroException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class ExamenLogic {

    public Examen update(Examen examen){

        Examen oldExamen = examenRepository.findOne(examen.getId());
        if (oldExamen == null) { throw new MesasUtnFrroException("No se ha podido actualizar el examen.");}

        validateExamen(oldExamen, examen);

        Materia materia = getMateria(examen.getMateria().getNombre(), examen.getMateria().getEspecialidad());

        // Load new values
        oldExamen.setAula(examen.getAula());
        oldExamen.setMateria(materia);
        oldExamen.setFecha(examen.getFecha());

        return examenRepository.save(oldExamen);
    }

    public void validateExamen(Examen oldExamen,Examen examen){
        validateExamen(examen);

        if(examen.getFecha().getDayOfWeek() != oldExamen.getMesa().getFecha().getDayOfWeek()){
            throw new MesasUtnFrroException("La fecha ingresada pertenece a otra mesa");
        }
    }

    public void validateExamen(Examen examen){
        if(examen.getMateria() == null ||
                examen.getMateria().getNombre().isEmpty() ||
                examen.getMateria().getEspecialidad().isEmpty() ||
                examen.getAula().isEmpty() ||
                examen.getFecha() == null){

            throw new MesasUtnFrroException("Hay campos en el Examen que estan vacios");
        }

        validateEspecialidad(examen.getMateria().getEspecialidad());
    }

    public void validateEspecialidad(String especialidad){
        if(!materiasRepository.findAllEspecialidades().contains(especialidad)){
            throw new MesasUtnFrroException("La especialidad ingresada no es permitida");
        }
    }

    public Examen saveExamen(ExamenForm examenForm){

        Examen examen = new Examen();

        validateEspecialidad(examenForm.getEspecialidad());

        Mesa mesa = mesaRepository.findOne(Long.valueOf(examenForm.getSelected_mesa()));
        if(mesa == null){
            throw new MesasUtnFrroException("El numero de la mesa no se corresponde con una Mesa existente");
        }

        // Parse date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        LocalTime localTime = LocalTime.parse(examenForm.getHora(), formatter);
        LocalDateTime fecha = LocalDateTime.of(mesa.getFecha(), localTime.plusHours(3));

        examen.setMateria(getMateria(examenForm.getMateria(), examenForm.getEspecialidad()));
        examen.setFecha(fecha);
        examen.setAula(examenForm.getAula());
        examen.setMesa(mesa);

        validateExamen(examen);

        return examenRepository.save(examen);
    }

    private Materia getMateria(String materia, String especialidad){
        Materia materiaObj = materiasRepository.findByNombreAndEspecialidad(materia,
                especialidad);

        // If the Materia doesn't exist, create a new one
        if (materiaObj == null){
            Materia newMateria = new Materia(materia, especialidad);
            materiaObj = materiasRepository.save(newMateria);
        }

        return materiaObj;
    }

    public void delete(long id){
        examenRepository.deleteById(id);
    }

    public long findLlamadoIdByExamenId(long id){
        return examenRepository.findLlamadoIdByExamenId(id);
    }

    @Autowired
    private MateriasRepository materiasRepository;

    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private MesaRepository mesaRepository;
}
