package ru.yandex.practicum.filmorate.storage.db.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.mapper.FilmListMapper;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final MpaDao mpaDao;

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id)" +
                " VALUES (:name, :description, :release_date, :duration, :mpa_id)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date", Date.valueOf(film.getReleaseDate()));
        params.addValue("duration", film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);

        long id = keyHolder.getKey().longValue();
        addGenres(id, film.getGenres());
        film.setId(id);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name=:name, description=:description, release_date=:release_date, " +
                "duration=:duration, mpa_id=:mpa_id WHERE film_id=:film_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date",Date.valueOf(film.getReleaseDate()));
        params.addValue("duration",  film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());
        params.addValue("film_id", film.getId());
        namedParameterJdbcTemplate.update(sql, params);
        updateGenres(film.getId(), film.getGenres());
        film = getFilmById(film.getId());
        return film;
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration," +
                " m.mpaId, m.mpaName, g.genreId, g.genreName FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id=fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id=g.genreId " +
                "lEFT JOIN mpa AS m ON f.mpa_id=m.mpaId " +
                "GROUP BY f.film_id ORDER BY f.film_id";
        List<Film> films = namedParameterJdbcTemplate.query(sql, new FilmListMapper());

        return films;
    }


    @Override
    public List<Film> getPopularFilms(long count) {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration," +
                " m.mpaId, m.mpaName, g.genreId, g.genreName FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id=fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id=g.genreId " +
                "lEFT JOIN mpa AS m ON f.mpa_id=m.mpaId " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT :count ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("count", count);

        List<Film> films = namedParameterJdbcTemplate.query(sql, params, new FilmListMapper());
        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration," +
                " m.mpaId, m.mpaName, g.genreId, g.genreName FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id=fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id=g.genreId " +
                "lEFT JOIN mpa AS m ON f.mpa_id=m.mpaId " +
                "WHERE f.film_id=:f.film_id " +
                "ORDER BY f.film_id, g.genreId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("f.film_id", id);
        List<Film> films = namedParameterJdbcTemplate.query(sql, params, new FilmMapper());
        return films.get(0);
    }

    @Override
    public List<Genre> getGenres(Long filmId) {
        String sql = "SELECT DISTINCT g.id, g.name FROM film_genres AS f " +
                "LEFT JOIN genres AS g ON f.genre_id = g.id" +
                " WHERE f.film_id=:film_id ORDER BY g.id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);

        List<Genre> genres = namedParameterJdbcTemplate.query(sql, params, new GenreMapper());
        return genres;
    }
    @Override
    public void addGenres(Long filmId, Set<Genre> genres) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (:film_id, :genre_id)";

        List<SqlParameterSource> batch = new ArrayList<>();
        for (Genre genre : genres) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("film_id", filmId);
            params.addValue("genre_id", genre.getId());
            batch.add(params);
        }
        namedParameterJdbcTemplate.batchUpdate(sql, batch.toArray(new SqlParameterSource[0]));
    }

    @Override
    public void updateGenres(Long filmId, Set<Genre> genres) {
        deleteGenres(filmId);
        addGenres(filmId, genres);
    }

    @Override
    public void deleteGenres(Long filmId) {
        String sql = "DELETE FROM film_genres WHERE film_id=:film_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public boolean tableElementsExist(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        return count == 0;
    }

}
