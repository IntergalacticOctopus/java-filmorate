package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.InternalServiceException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Map;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException exception) {
        log.info("Data not found {}", exception.getMessage());
        String stacktrace = ExceptionUtils.getStackTrace(exception);
        return Map.of("Data not found", exception.getMessage(), "Stacktrace: ", stacktrace);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalServiceException(final InternalServiceException exception) {
        log.info("Server error {}", exception.getMessage());
        String stacktrace = ExceptionUtils.getStackTrace(exception);
        return Map.of("Server error ", exception.getMessage(), "Stacktrace: ", stacktrace);
    }
}
