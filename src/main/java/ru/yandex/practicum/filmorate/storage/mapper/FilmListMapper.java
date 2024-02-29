package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmListMapper implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException {
        List<Film> orderDetailsList = new ArrayList<>();
        Film currentFilm = null;
        while (rs.next()) {
            Long filmId = rs.getLong("film_id");
            if (currentFilm == null || !filmId.equals(currentFilm.getId())) {
                currentFilm = new Film();
                currentFilm.setId(rs.getLong("film_id"));
                currentFilm.setName(rs.getString("name"));
                currentFilm.setDescription(rs.getString("description"));
                currentFilm.setReleaseDate(rs.getDate("release_date").toLocalDate());
                currentFilm.setDuration(rs.getLong("duration"));
                currentFilm.setMpa(new Mpa(rs.getLong("mpaId"), rs.getString("mpaName")));
                orderDetailsList.add(currentFilm);
            }
            if (rs.getLong("genreId") != 0) {
                currentFilm.getGenres().add(new Genre(rs.getLong("genreId"), rs.getString("genreName")));
            }
        }
        return orderDetailsList;
    }
}