package com.mariakamachine.dentoice.exception.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ConstraintViolationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @SuppressWarnings("unchecked")
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        return new ResponseEntity(exception.getConstraintViolations().stream()
                .map(violation -> format("validation for field '%s' with value '%s' failed", violation.getPropertyPath().toString(), violation.getInvalidValue()))
                .collect(toList()), BAD_REQUEST);
    }

}
