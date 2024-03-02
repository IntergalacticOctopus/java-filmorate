package ru.yandex.practicum.filmorate.storage.db.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcGenreStorage implements GenreStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Genre getById(Long id) {
        String sql = "SELECT genreId, genreName FROM genres WHERE genreId=:genreId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreId", id);
        List<Genre> genre = namedParameterJdbcTemplate.query(sql, params, new GenreMapper());
        if (genre.isEmpty()) {
            return null;
        } else {
            return genre.get(0);
        }
    }

    @Override
    public List<Genre> getAll() {
        List<Genre> list = namedParameterJdbcTemplate.query("SELECT genreId, genreName FROM genres ORDER BY genreId",
                new GenreMapper());
        return list;
    }
    @Override
    public List<Genre> getStorageIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = "SELECT genreId, genreName FROM genres WHERE genreId IN (:ids)";
        Map<String, Object> paramMap = Collections.singletonMap("ids", ids);
        return namedParameterJdbcTemplate.query(sql, paramMap, new BeanPropertyRowMapper<>(Genre.class));
    }
}
