package ru.yandex.practicum.filmorate.storage.db.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.FilmListMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("FilmDbStorage")
@RequiredArgsConstructor
public class JdbcFilmStorage implements FilmStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Film create(Film film) {
        String insertQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (:name, :description, :release_date, :duration, :mpa_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(insertQuery, params, keyHolder);

        long generatedId = keyHolder.getKey().longValue();

        addGenres(generatedId, film.getGenres());
        film.setId(generatedId);

        return film;
    }

    @Override
    public Film update(Film film) {
        String updateQuery = "UPDATE films SET name=:name, description=:description, release_date=:release_date, " +
                "duration=:duration, mpa_id=:mpa_id WHERE film_id=:film_id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId())
                .addValue("film_id", film.getId());

        namedParameterJdbcTemplate.update(updateQuery, params);

        updateGenres(film.getId(), film.getGenres());

        return getById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        String selectQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
                "m.mpaId, m.mpaName, g.genreId, g.genreName FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id=fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id=g.genreId " +
                "LEFT JOIN mpa AS m ON f.mpa_id=m.mpaId " +
                "GROUP BY f.film_id ORDER BY f.film_id";

        List<Film> films = namedParameterJdbcTemplate.query(selectQuery, new FilmListMapper());

        return films;
    }


    @Override
    public List<Film> getPopularFilms(long count) {
        String selectQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
                "m.mpaId, m.mpaName, g.genreId, g.genreName FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genreId " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpaId " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id ORDER BY COUNT(l.user_id) " +
                "LIMIT :count ";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("count", count);
        List<Film> films = namedParameterJdbcTemplate.query(selectQuery, params, new FilmListMapper());
        return films;
    }

    @Override
    public Film getById(Long id) {
        String selectQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
                "m.mpaId, m.mpaName, g.genreId, g.genreName FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id=fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id=g.genreId " +
                "LEFT JOIN mpa AS m ON f.mpa_id=m.mpaId " +
                "WHERE f.film_id=:film_id " +
                "ORDER BY f.film_id, g.genreId";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("film_id", id);

        List<Film> films = namedParameterJdbcTemplate.query(selectQuery, params, new FilmListMapper());

        if (films.isEmpty()) {
            return null;
        } else {
            return films.get(0);
        }
    }

    private void addGenres(Long filmId, Set<Genre> genres) {
        String insertQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (:film_id, :genre_id)";

        List<MapSqlParameterSource> batchParams = new ArrayList<>();
        for (Genre genre : genres) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("film_id", filmId)
                    .addValue("genre_id", genre.getId());
            batchParams.add(params);
        }

        namedParameterJdbcTemplate.batchUpdate(insertQuery, batchParams.toArray(new MapSqlParameterSource[0]));
    }

    private void deleteGenres(Long filmId) {
        String deleteQuery = "DELETE FROM film_genres WHERE film_id = :film_id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("film_id", filmId);
        namedParameterJdbcTemplate.update(deleteQuery, params);
    }

    private void updateGenres(Long filmId, Set<Genre> genres) {
        deleteGenres(filmId);
        addGenres(filmId, genres);
    }


}
