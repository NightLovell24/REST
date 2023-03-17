package com.n0rth.rest.exceptionhandling;

import com.n0rth.rest.exceptionhandling.exception.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PersonGlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<PersonHandler> handleException(PersonNotFoundException exception) {

        PersonHandler PersonHandler = new PersonHandler();
        PersonHandler.setInfo(exception.getMessage());

        return new ResponseEntity<>(PersonHandler, HttpStatus.NOT_FOUND);
    }
}
