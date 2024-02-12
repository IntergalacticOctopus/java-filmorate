package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateService;
import ru.yandex.practicum.filmorate.exception.AlreadyDoneException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;

@Slf4j
@Component("FilmService")
@Service
public class FilmService {
    private final ValidateService validateService;
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmComparator comparator;
    Map<Long, Film> filmStorage;


    @Autowired
    public FilmService(ValidateService validateService,
                       InMemoryFilmStorage inMemoryFilmStorage) {
        this.validateService = validateService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmStorage = inMemoryFilmStorage.getStorage();
        this.comparator = new FilmComparator();
        ;
    }

    ;

    public Film createFilm(Film film) {
        validateService.validate(film);
        return inMemoryFilmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validateService.validate(film);
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Film addLike(Long userId, Long filmId) {

        Film film = filmStorage.get(filmId);

        if (film.getLikes().contains(userId)) {
            throw new AlreadyDoneException("Film is already liked by this user");
        }

        Set<Long> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);

        film.setLikesCounter(film.getLikesCounter() + 1);

        inMemoryFilmStorage.updateFilm(film);
        return film;
    }

    public Film removeLike(Long userId, Long filmId) {

        Film film = filmStorage.get(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new AlreadyDoneException("Film is not liked by this user");
        }


        Set<Long> likes = film.getLikes();
        likes.remove(userId);
        film.setLikes(likes);

        film.setLikesCounter(film.getLikesCounter() - 1);

        inMemoryFilmStorage.updateFilm(film);
        return film;
    }

    public List<Film> getMovieRatings(Long count) {
        if (count == null) {
            count = 10L;
        }
        List<Film> list = new ArrayList<>((Collection) filmStorage.values());
        Collections.sort(list, comparator);
        Collections.reverse(list);
        if (list.size() < count) {
            count = (long) list.size();
        }
        List<Film> returnList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            returnList.add(list.get(i));
        }
        return returnList;
    }

    public List<Film> getAll() {
        return inMemoryFilmStorage.getAll();
    }

    public Film getFilmById (Long id) {
        if (id == null || !filmStorage.containsKey(id)) {
            throw new NotFoundException("This film does not exist");
        }
        return filmStorage.get(id);
    }

}
