package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateService;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.db.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.db.user.UserStorage;
import java.util.*;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaDao mpaDao;
    private final LikeDao likeDao;
    private final ValidateService validateService;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage, MpaDao mpaDao, LikeDao likeDao, ValidateService validateService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaDao = mpaDao;
        this.likeDao = likeDao;
        this.validateService = validateService;
    }

    public Film create(Film film) {
        validateService.validate(film);
        return filmStorage.createFilm(film);
    }

    public Film update(Film film) {
        isFilmExist(film.getId());
        validateService.validate(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Long id) {
        Film film = isFilmExist(id);
        return film;
    }
    private Film isFilmExist(Long id) {
        Film film = filmStorage.getFilmById(id);
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
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new NotFoundException("Film not found");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        likeDao.like(id, userId);
        return film;
    }

    public Film removeLike(Long id, Long userId) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new NotFoundException("Film not found");
        }
        if (!userStorage.isContains(userId)) {
            throw new NotFoundException("User not found");
        }
        likeDao.removeLike(id, userId);
        return film;
    }

}
