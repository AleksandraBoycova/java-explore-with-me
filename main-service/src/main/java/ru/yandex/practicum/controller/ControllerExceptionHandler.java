package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.exception.AlreadyExistsException;
import ru.yandex.practicum.exception.ErrorResponse;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.RequestDeniedException;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Map;


@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(NotFoundException exception) {
        Map<String, String> result = Map.of("Ошибка:", exception.getMessage());
        log.warn(String.valueOf(result), exception, exception.getMessage());
        return result;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleRequestDeniedException(RequestDeniedException exception) {
        Map<String, String> result = Map.of("Ошибка:", exception.getMessage());
        log.warn(String.valueOf(result), exception, exception.getMessage());
        return result;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyExistsException(AlreadyExistsException exception) {
        Map<String, String> result = Map.of("Ошибка:", exception.getMessage());
        log.warn(String.valueOf(result), exception, exception.getMessage());
        return result;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAlreadyExistException(DataIntegrityViolationException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintException(ConstraintViolationException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                new Date(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()
        );
    }
}
