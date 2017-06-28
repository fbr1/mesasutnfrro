package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Examen;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ExamenRepository extends CrudRepository<Examen, Long>{

    @Query("select llamado.id " +
            "from Examen examen " +
            "inner join examen.mesa mesa " +
            "inner join mesa.llamado llamado " +
            "where examen.id = :id")
    long findLlamadoIdByExamenId(@Param("id") long id);

    @Modifying
    @Transactional
    @Query("delete from Examen examen where examen.id = :id")
    void deleteById(@Param("id") long id);
}
