package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Date;

@AllArgsConstructor
@Value
public
class ErrorResponse {
    Date timestamp;
    int status;
    String error;
    String path;
}