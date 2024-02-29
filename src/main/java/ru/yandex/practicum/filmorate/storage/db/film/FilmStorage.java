package ru.yandex.practicum.filmorate.storage.db.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAll();

    List<Film> getPopularFilms(long count);

    Film getFilmById(Long id);

    List<Genre> getGenres(Long filmId);

    void addGenres(Long filmId, Set<Genre> genres);

    void updateGenres(Long filmId, Set<Genre> genres);

    void deleteGenres(Long filmId);

    public static class FilmComparator implements Comparator<Film> {
        @Override
        public int compare(Film film1, Film film2) {
            return Long.compare(film2.getLikesCounter(), film1.getLikesCounter());
        }
    }
}
