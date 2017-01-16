package com.fbr1.mesasutnfrro.model.entity;

/**
 * Created by Pedro on 11/12/2016.
 */
public class Error {

    private int code;

    private String message;

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
