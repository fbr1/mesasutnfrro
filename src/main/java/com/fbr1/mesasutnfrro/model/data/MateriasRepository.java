package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Materia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MateriasRepository extends CrudRepository<Materia, String> {

    Materia findByNombreAndEspecialidad(String nombre, String especialidad);

}
