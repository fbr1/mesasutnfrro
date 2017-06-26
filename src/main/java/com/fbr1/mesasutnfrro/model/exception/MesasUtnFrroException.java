package com.fbr1.mesasutnfrro.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MesasUtnFrroException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public MesasUtnFrroException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public MesasUtnFrroException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
