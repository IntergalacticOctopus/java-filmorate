package ru.yandex.practicum.filmorate.storage.db.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Genre getGenreById(Long id) {
        String sql = "SELECT genreId, genreName FROM genres WHERE genreId=:genreId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreId", id);
        List<Genre> genres = namedParameterJdbcTemplate.query(sql, params, new GenreMapper());
        return genres.get(0);
    }

    @Override
    public List<Genre> getGenres() {
        List<Genre> list = namedParameterJdbcTemplate.query("SELECT genreId, genreName FROM genres ORDER BY genreId",
                new GenreMapper());
        return list;
    }
}
