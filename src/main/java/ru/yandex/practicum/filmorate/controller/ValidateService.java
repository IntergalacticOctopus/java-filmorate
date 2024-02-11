package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@Component
public class ValidateService {

    private final LocalDate startReleaseDate = LocalDate.of(1895, 12, 28);

    public void validate(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Film name is invalid");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Film Description length > 200 characters");
        }
        if (film.getReleaseDate().isBefore(startReleaseDate)) {
            throw new ValidationException("Film release date is invalid");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Film duration is invalid");
        }
    }

    public void validate(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("User email is invalid");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("User login is invalid");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User birthday is invalid");
        }
    }

    public void validate(User firstUser, User secondUser) {
        validate(firstUser);
        validate(secondUser);
    }

    public void validate(User user, Film film) {
        validate(user);
        validate(film);
    }
}
