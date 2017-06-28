package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Mesa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MesaRepository extends CrudRepository<Mesa, Long> {

    @Query("select case when (count(mesa) > 0)  then true else false end " +
            "from Mesa mesa where mesa.id= :id")
    boolean existsById(@Param("id") int id);
}
