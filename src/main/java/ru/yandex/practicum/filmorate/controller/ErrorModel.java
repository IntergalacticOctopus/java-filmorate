package ru.yandex.practicum.filmorate.controller;

import lombok.Data;

@Data
class ErrorModel {
    private String error;
    private String stackTrace;

    protected ErrorModel(String error, String stackTrace) {
        this.error = error;
        this.stackTrace = stackTrace;
    }
}
