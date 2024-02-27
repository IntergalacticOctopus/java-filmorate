package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    private final String error;
    private final String stacktrace;

    public ErrorResponse(String error, String stacktrace) {
        this.error = error;
        this.stacktrace = stacktrace;
    }

    public String getError() {
        return error;
    }
}
