package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
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

    List<Film> getMovieRatings(long count);

    Film getFilmById(long id);

    public static class FilmComparator implements Comparator<Film> {
        @Override
        public int compare(Film film1, Film film2) {
            return Long.compare(film1.getLikesCounter(), film2.getLikesCounter());
        }
    }
}
