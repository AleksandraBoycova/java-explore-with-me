package ru.practicum.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.exception.ErrorResponse;
import ru.practicum.server.exception.ValidationException;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleRequestDeniedException(ValidationException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
        log.warn(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
