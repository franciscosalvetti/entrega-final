/*
    Este archivo es creado para manejar las excepciones de una manera mas adecuada según lo que se necesite
    en cada caso de validación
*/

package com.example.entregafinal.Model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handlerIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>("{\"error\":\""+ex.getMessage()+"\"}", HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<String> handlerHttpMessageNotReadableException(Exception ex) {
        return new ResponseEntity<>("{\"error\":\""+ex.getMessage()+"\"}", HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handlerDateTimeParseException(DateTimeParseException ex){
        return new ResponseEntity<>("{\"error\":\"Error en el formato de fechas. Revisar que la fecha tenga el siguiente formato: AAAA-MM-DD, el mes tenga 2 dígitos y esté entre 01 y 12 y que el día esté entre 01 y 31.\"}", HttpStatus.BAD_REQUEST);
    }
}
