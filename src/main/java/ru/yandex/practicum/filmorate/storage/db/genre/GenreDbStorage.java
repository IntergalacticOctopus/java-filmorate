package ru.yandex.practicum.filmorate.storage.db.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
        try {
            String sql = "SELECT id, name FROM genres WHERE id=:id";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);
            Genre genre = namedParameterJdbcTemplate.queryForObject(sql, params, new GenreMapper());
            return genre;
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Data not found");
        }
    }

    @Override
    public List<Genre> getGenres() {
        List<Genre> list = namedParameterJdbcTemplate.query("SELECT id, name FROM genres ORDER BY id",
                new GenreMapper());
        return list;
    }
}
