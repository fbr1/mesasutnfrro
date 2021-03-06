package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Llamado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface LlamadosRepository extends PagingAndSortingRepository<Llamado, Long> {
    @Query("from Llamado llamado where llamado.id = :id")
    Llamado findById(@Param("id") long id);

    Llamado findByAñoAndNumero(int año, int numero);

    Llamado findTopByOrderByDateDesc();

    List<Llamado> findByAño(int año);

    List<Llamado> findByWeekType(int weekType);

    @Query("select case when (count(llamado) > 0)  then true else false end " +
            "from Llamado llamado where llamado.año = :año and llamado.numero = :numero")
    boolean existsByAñoAndNumber(@Param("año") int año, @Param("numero") int numero);

    Page<Llamado> findByOrderByDateDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from Llamado llamado where llamado.id = :id")
    void deleteById(@Param("id") long id);
}
