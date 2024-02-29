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
public class JdbcGenreStorage implements GenreStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Genre getById(Long id) {
        try {
            String sql = "SELECT genreId, genreName FROM genres WHERE genreId=:genreId";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("genreId", id);
            Genre genre = namedParameterJdbcTemplate.queryForObject(sql, params, new GenreMapper());
            return genre;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("not found");
        }
    }

    @Override
    public List<Genre> getAll() {
        List<Genre> list = namedParameterJdbcTemplate.query("SELECT genreId, genreName FROM genres ORDER BY genreId",
                new GenreMapper());
        return list;
    }
}