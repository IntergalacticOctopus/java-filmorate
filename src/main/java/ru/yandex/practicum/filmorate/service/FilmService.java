package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateService;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.db.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.db.user.JdbcFilmStorage;
import ru.yandex.practicum.filmorate.storage.db.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;
    private final ValidateService validateService;

    @Autowired
    public FilmService(ru.yandex.practicum.filmorate.storage.db.film.JdbcFilmStorage filmStorage, JdbcFilmStorage userStorage, MpaStorage mpaStorage, LikeStorage likeStorage, ValidateService validateService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.likeStorage = likeStorage;
        this.validateService = validateService;
    }

    public Film create(Film film) {
        validateService.validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        isFilmExist(film.getId());
        validateService.validate(film);
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        Film film = isFilmExist(id);
        return film;
    }

    private Film isFilmExist(Long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NotFoundException("User" + film + "not exist");
        }
        return film;
    }

    public List<Film> getMovieRating(Long count) {
        List<Film> films = filmStorage.getPopularFilms(count);
        return films;
    }

    public Film like(Long id, Long userId) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NotFoundException("Film not found");
        }
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        likeStorage.like(id, userId);
        return film;
    }

    public Film removeLike(Long id, Long userId) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NotFoundException("Film not found");
        }
        if (!userStorage.isContains(userId)) {
            throw new NotFoundException("User not found");
        }
        likeStorage.removeLike(id, userId);
        return film;
    }

}
