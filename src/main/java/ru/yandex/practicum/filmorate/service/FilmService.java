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

import static java.lang.System.in;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final ValidateService validateService;

    @Autowired
    public FilmService(ru.yandex.practicum.filmorate.storage.db.film.JdbcFilmStorage filmStorage, JdbcUserStorage userStorage, MpaStorage mpaStorage, LikeStorage likeStorage, ValidateService validateService, JdbcGenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.validateService = validateService;
        this.genreStorage = genreStorage;
    }

    public Film create(Film film) {
        List<Genre> genres = genreStorage.getStorageIds(film.getGenres().stream().map(g -> g.getId()).collect(Collectors.toList()));
        if (genres.size() != film.getGenres().size()) {
            throw new NotFoundException("Genres not found");
        }
        validateService.validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        List<Genre> genres = genreStorage.getStorageIds(film.getGenres().stream().map(g -> g.getId()).collect(Collectors.toList()));
        if (genres.size() != film.getGenres().size()) {
            throw new NotFoundException("Genres not found");
        }

        isFilmExist(film.getId());

        validateService.validate(film);
        return filmStorage.update(film);
    }
    private List getStorageGenre(Set<Genre> genreSet) {
        List <Genre> returnList = new ArrayList<>();
        List<Genre> list = genreStorage.getAll();

        for (Genre genre : genreSet) {
            if (list.contains(genre)) {
                returnList.add(genre);
            }
        }
        return returnList;
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
