package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Materia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface MateriasRepository extends CrudRepository<Materia, String> {

    Materia findByNombreAndEspecialidad(String nombre, String especialidad);

    @Query("select distinct materia.especialidad from Materia materia")
    List<String> findAllEspecialidades();

}
