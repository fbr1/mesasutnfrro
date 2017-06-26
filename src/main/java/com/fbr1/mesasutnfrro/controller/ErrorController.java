package com.fbr1.mesasutnfrro.controller;


import com.fbr1.mesasutnfrro.model.exception.MesasUtnFrroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorController {

    private static Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity exception(final Throwable throwable) {
        logger.error("Exception during execution of the application", throwable);

        if (throwable instanceof MesasUtnFrroException){
            return new ResponseEntity<>(throwable.getMessage(), ((MesasUtnFrroException) throwable).getHttpStatus());
        }else{
            return new ResponseEntity<>("Hubo un error en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
