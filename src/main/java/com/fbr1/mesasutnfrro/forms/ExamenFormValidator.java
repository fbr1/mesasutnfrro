package com.fbr1.mesasutnfrro.forms;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExamenFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ExamenForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ExamenForm examenForm = (ExamenForm)o;

        // Validate hour
        Matcher matcher = Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0]):[0-5][0-9]$").matcher(examenForm.getHora());

        if (!matcher.find()) {
            errors.rejectValue("hora", "La hora ingresada no tiene formato H:MM o HH:MM. O es mayor a 20:59");
        }

    }
}
