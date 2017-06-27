package com.fbr1.mesasutnfrro.model.logic;

import com.fbr1.mesasutnfrro.model.data.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MesaLogic {

    public boolean existsById(int id){
        return mesaRepository.existsById(id);
    }

    @Autowired
    private MesaRepository mesaRepository;
}
