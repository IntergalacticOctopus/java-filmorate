package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface Validatable {
    void validate(Film film);

    void validate(User user);

    void validate(User firstUser, User secondUser);
}
