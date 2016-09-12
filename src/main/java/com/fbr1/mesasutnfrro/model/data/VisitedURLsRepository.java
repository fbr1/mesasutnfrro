package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.VisitedURL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface VisitedURLsRepository extends CrudRepository<VisitedURL, String> {

}
