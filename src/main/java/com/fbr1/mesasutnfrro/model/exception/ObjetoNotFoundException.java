package com.fbr1.mesasutnfrro.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Pedro on 09/12/2016.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjetoNotFoundException extends RuntimeException {
    public ObjetoNotFoundException(String message) {
        super(message);
    }
}
