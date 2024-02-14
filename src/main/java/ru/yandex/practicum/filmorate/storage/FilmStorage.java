package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAll();

    Map<Long, Film> getStorage();

    Map<Long, Set<Long>> getLikesStorage();

    Film addLike(Long userId, Long filmId);

    Film removeLike(Long userId, Long filmId);

    List<Film> getMovieRatings(Long count);

    Film getFilmById(long id);
}
