package ru.yandex.practicum.filmorate.storage.db.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.sql.Date;
import java.util.List;

@Slf4j
@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final MpaDao mpaDao;

    @Override
    public Film createFilm(Film film) {
        if (tableElementsExist("genres") || tableElementsExist("mpa")) {
            throw new NotFoundException("Data not found");
        }
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id)" +
                " VALUES (:name, :description, :release_date, :duration, :mpa_id)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date", Date.valueOf(film.getReleaseDate()));
        params.addValue("duration", film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());

        namedParameterJdbcTemplate.update(sql, params);

        String sql1 = "SELECT film_id FROM films WHERE name=:name AND description=:description AND" +
                " release_date=:release_date AND duration=:duration AND mpa_id=:mpa_id";
        long id = namedParameterJdbcTemplate.queryForObject(sql1, params, Integer.class);
        film.setId(id);
        addGenres(id, film.getGenres());
        film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
        film.setGenres(getGenres(id));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name=:name, description=:description, release_date=:release_date, " +
                "duration=:duration, mpa_id=:mpa_id WHERE film_id=:film_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date", Date.valueOf(film.getReleaseDate()));
        params.addValue("duration", film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());
        params.addValue("film_id", film.getId());
        namedParameterJdbcTemplate.update(sql, params);
        updateGenres(film.getId(), film.getGenres());
        film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
        film.setGenres(getGenres(film.getId()));
        return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = namedParameterJdbcTemplate.query("SELECT * FROM films", new FilmMapper());
        for (Film film : films) {
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            film.setGenres(getGenres(film.getId()));
        }
        return films;
    }


    @Override
    public List<Film> getPopularFilms(long count) {
        List<Film> sortedFilms = namedParameterJdbcTemplate.query("SELECT * FROM films AS f " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id" +
                " GROUP BY f.film_id" +
                " ORDER BY COUNT(l.user_id)" +
                " LIMIT " + count, new FilmMapper());
        for (Film film : sortedFilms) {
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            film.setGenres(getGenres(film.getId()));
        }
        return sortedFilms;
    }

    @Override
    public Film getFilmById(Long id) {
        try {
            String sql = "SELECT film_id, name, description, release_date, " +
                    "duration, mpa_id FROM films WHERE film_id=:film_id";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("film_id", id);
            Film returnedFilm = namedParameterJdbcTemplate.queryForObject(sql, params, new FilmMapper());
            returnedFilm.setMpa(mpaDao.getMpaById(returnedFilm.getMpa().getId()));
            returnedFilm.setGenres(getGenres(id));
            return returnedFilm;
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Data not found");
        }
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
    public void addGenres(Long filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            String sql = "INSERT INTO film_genres (film_id, genre_id)" +
                    " VALUES (:film_id, :genre_id)";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("film_id", filmId);
            params.addValue("genre_id", genre.getId());

            namedParameterJdbcTemplate.update(sql, params);
        }
    }

    @Override
    public void updateGenres(Long filmId, List<Genre> genres) {
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
