package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateService;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.genre.JdbcGenreStorage;
import ru.yandex.practicum.filmorate.storage.db.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.db.user.JdbcUserStorage;
import ru.yandex.practicum.filmorate.storage.db.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final ValidateService validateService;
    private final MpaStorage mpaStorage;

    @Autowired
    public FilmService(ru.yandex.practicum.filmorate.storage.db.film.JdbcFilmStorage filmStorage, JdbcUserStorage userStorage, MpaStorage mpaStorage, LikeStorage likeStorage, ValidateService validateService, JdbcGenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.validateService = validateService;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public Film create(Film film) {
        validateService.validate(film);
        List<Genre> genres = genreStorage.getIds(film.getGenres().stream().map(g -> g.getId()).collect(Collectors.toList()));
        if (genres.size() != film.getGenres().size()) {
            throw new NotFoundException("Genres not found");
        }
        if (mpaStorage.getById(film.getMpa().getId()) == null) {
            throw new NotFoundException("mpa not found");
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateService.validate(film);
        List<Genre> genres = genreStorage.getIds(film.getGenres().stream().map(g -> g.getId()).collect(Collectors.toList()));
        if (genres.size() != film.getGenres().size()) {
            throw new NotFoundException("Genres not found");
        }

        isFilmExist(film.getId());
        if (mpaStorage.getById(film.getMpa().getId()) == null) {
            throw new NotFoundException("mpa not found");
        }
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

    public List<Film> getPopular(Long count) {
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
        likeStorage.add(id, userId);
        return film;
    }

    public Film removeLike(Long id, Long userId) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NotFoundException("Film not found");
        }
        if (filmStorage.getById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        likeStorage.remove(id, userId);
        return film;
    }

}
