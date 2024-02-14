package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.Validatable;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;

@Service
public class FilmService {
    private final Validatable validateService;
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    Map<Long, Film> filmStorage;
    Map<Long, User> userStorage;
    Map<Long, Set<Long>> likesStorage;


    @Autowired
    public FilmService(Validatable validateService,
                       FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.validateService = validateService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmStorage = inMemoryFilmStorage.getStorage();
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.likesStorage = inMemoryFilmStorage.getLikesStorage();
        this.userStorage = inMemoryUserStorage.getStorage();
    }

    ;

    public Film createFilm(Film film) {
        validateService.validate(film);
        return inMemoryFilmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        Long filmId = film.getId();
        if (filmStorage.get(film.getId()) == null) {
            throw new NotFoundException(String.format("Data %s not found", film));
        }
        validateService.validate(film);
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Film addLike(Long userId, Long filmId) {
        Film film = filmStorage.get(filmId);
        if (userStorage.get(userId) == null || film == null) {
            throw new NotFoundException("User or film does not exist");
        }
        if (likesStorage.get(filmId).contains(userId)) {
            return film;
        }
        return inMemoryFilmStorage.addLike(userId, filmId);
    }

    public Film removeLike(Long userId, Long filmId) {
        if (userStorage.get(userId) == null || filmStorage.get(filmId) == null) {
            throw new NotFoundException("User or film does not exist");
        }
        Film film = filmStorage.get(filmId);
        if (!likesStorage.get(filmId).contains(userId)) {
            return film;
        }
        return inMemoryFilmStorage.removeLike(userId, filmId);
    }

    public List<Film> getMovieRatings(Long count) {
        return inMemoryFilmStorage.getMovieRatings(count);
    }

    public List<Film> getAll() {
        return inMemoryFilmStorage.getAll();
    }

    public Film getFilmById(long id) {
        if (filmStorage.get(id) == null) {
            throw new NotFoundException("This film does not exist");
        }
        return inMemoryFilmStorage.getFilmById(id);
    }

}
