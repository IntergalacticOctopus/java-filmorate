package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlreadyDoneException extends RuntimeException {
    public AlreadyDoneException(String message) {
        super(message);
    }
}
