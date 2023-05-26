package ru.yandex.practicum.exception;

public class RequestDeniedException extends RuntimeException {

    public RequestDeniedException(String message) {
        super(message);
    }
}
