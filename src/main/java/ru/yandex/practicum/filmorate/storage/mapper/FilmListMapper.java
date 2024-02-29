package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmListMapper implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException {
        List<Film> films = new ArrayList<>();
        Map<Long, Film> filmMap = new HashMap<>();

        while (rs.next()) {
            Long filmId = rs.getLong("film_id");
            Film currentFilm = filmMap.get(filmId);

            if (currentFilm == null) {
                currentFilm = new Film();
                currentFilm.setId(filmId);
                currentFilm.setName(rs.getString("name"));
                currentFilm.setDescription(rs.getString("description"));
                currentFilm.setReleaseDate(rs.getDate("release_date").toLocalDate());
                currentFilm.setDuration(rs.getLong("duration"));
                currentFilm.setMpa(new Mpa(rs.getLong("mpaId"), rs.getString("mpaName")));
                currentFilm.setGenres(new HashSet<>());

                filmMap.put(filmId, currentFilm);
                films.add(currentFilm);
            }

            if (rs.getLong("genreId") != 0) {
                currentFilm.getGenres().add(new Genre(rs.getLong("genreId"), rs.getString("genreName")));
            }
        }

        return films;
    }
}