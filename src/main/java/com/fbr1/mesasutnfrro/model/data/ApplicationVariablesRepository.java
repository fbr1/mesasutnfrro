package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.ApplicationVariables;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ApplicationVariablesRepository extends CrudRepository<ApplicationVariables, Long> {

    @Query("from ApplicationVariables av where av.id = (select max(avv.id) from ApplicationVariables avv)")
    ApplicationVariables findTop();

}