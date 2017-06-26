package com.fbr1.mesasutnfrro.model.logic;

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

@Service
public class ExamenLogic {

    public Examen update(Examen examen){

        validateExamen(examen);

        Examen oldExamen = examenRepository.findOne(examen.getId());
        if (oldExamen == null) { throw new MesasUtnFrroException("No se ha podido actualizar el examen.");}

        Materia materia = materiasRepository.findByNombreAndEspecialidad(examen.getMateria().getNombre(),
                                                                            examen.getMateria().getEspecialidad());

        // If the Materia doesn't exist, create a new one
        if (materia == null){
            Materia newMateria = new Materia(examen.getMateria().getNombre(), examen.getMateria().getEspecialidad());
            materia = materiasRepository.save(newMateria);
        }

        // Load new values
        oldExamen.setAula(examen.getAula());
        oldExamen.setMateria(materia);
//        oldExamen.setFecha(examen.getFecha());


//        Mesa mesaActual = oldExamen.getMesa();
//        if((examen.getMesa() != null) && (oldExamen.getMesa().getId() != examen.getMesa().getId())) {
//            Mesa mesaNueva = mesaRepository.findOne(examen.getMesa().getId());
//            if (mesaNueva == null) {throw new MesasUtnFrroException("La mesa a la cuál quiere cambiar el examen no existe", HttpStatus.NOT_FOUND);}
//            mesaActual.getExamenes().remove(oldExamen);
//            mesaNueva.getExamenes().add(oldExamen);
//            mesaRepository.save(mesaActual);
//            mesaRepository.save(mesaNueva);
//            oldExamen.setMesa(mesaNueva);
//        }
        return examenRepository.save(oldExamen);
    }

    public void validateExamen(Examen examen){
        if(examen.getMateria() == null ||
                examen.getMateria().getNombre().isEmpty() ||
                examen.getMateria().getEspecialidad().isEmpty() ||
                examen.getAula().isEmpty() ||
                examen.getFecha() == null){

            throw new MesasUtnFrroException("Hay campos en el Examen que estan vacios");

        }
    }

    @Autowired
    private MateriasRepository materiasRepository;

    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private MesaRepository mesaRepository;
}
