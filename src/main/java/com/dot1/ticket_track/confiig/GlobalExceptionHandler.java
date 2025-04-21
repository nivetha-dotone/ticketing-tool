package com.dot1.ticket_track.confiig;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<String> handleJsonWriteException(HttpMessageNotWritableException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Serialization error: " + ex.getMessage());
    }
}
