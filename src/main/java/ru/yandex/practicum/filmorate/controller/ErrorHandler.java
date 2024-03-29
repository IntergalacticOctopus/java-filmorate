package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException exception) {
        log.error("Data not found ", exception);
        String stacktrace = ExceptionUtils.getStackTrace(exception);
        String errorMessage = "Data not found error: " + exception.getMessage() + stacktrace;
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException exception) {
        log.error("Validation error ", exception);
        String stacktrace = ExceptionUtils.getStackTrace(exception);
        String errorMessage = "Validation found error: " + exception.getMessage() + stacktrace;
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServiceException(final Exception exception) {
        log.error("Server error ", exception);
        String stacktrace = ExceptionUtils.getStackTrace(exception);
        String errorMessage = "InternalService error: " + exception.getMessage() + stacktrace;
        return new ErrorResponse(errorMessage);
    }
}

