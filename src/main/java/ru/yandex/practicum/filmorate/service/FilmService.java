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
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    @Autowired
    public FilmService(Validatable validateService,
                       FilmStorage filmStorage, UserStorage userStorage) {
        this.validateService = validateService;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    ;

    public Film createFilm(Film film) {
        validateService.validate(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        Long filmId = film.getId();
        isFilmExist(filmId);
        validateService.validate(film);
        return filmStorage.updateFilm(film);
    }

    public Film addLike(Long userId, Long filmId) {
        Film film = filmStorage.getFilmById(filmId);
        if (userStorage.getUserById(userId) == null || film == null) {
            throw new NotFoundException("User or film does not exist");
        }
        if (filmStorage.getLikesStorage().get(filmId).contains(userId)) {
            return film;
        }
        return filmStorage.addLike(userId, filmId);
    }

    public Film removeLike(Long userId, Long filmId) {
        isFilmExist(userId);
        //Проверка на наличие юзера больше нигде не производится
        isUserExist(userId);
        return filmStorage.removeLike(userId, filmId);
    }

    public List<Film> getMovieRatings(Long count) {
        return filmStorage.getMovieRatings(count);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }
    private void isFilmExist(Long id) {
        Film film = getFilmById(id);
        if (film == null) {
            throw new NotFoundException("This film" + id + "does not exist " );
        }
    }
    private void isUserExist(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("This user" + id + "does not exist " );
        }
    }

}
