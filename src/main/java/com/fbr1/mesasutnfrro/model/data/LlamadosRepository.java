package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Llamado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface LlamadosRepository extends CrudRepository<Llamado, Integer> {

    Llamado findByAñoAndNumero(int año, int numero);

    Llamado findTopByOrderByDateDesc();

    List<Llamado> findByAño(int año);

}
