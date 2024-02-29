package ru.yandex.practicum.filmorate.storage.db.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JdbcMpaStorage implements MpaStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public @NotNull Mpa getById(Long id) {
        try {
            String sql = "SELECT * FROM mpa WHERE mpaId=:mpaId";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("mpaId", id);
            Mpa mpa = namedParameterJdbcTemplate.queryForObject(sql, params, new MpaMapper());
            return mpa;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("not found");
        }


    }

    @Override
    public List<Mpa> getAll() {
        List<Mpa> allMpa = namedParameterJdbcTemplate.query("SELECT * FROM mpa ORDER BY mpaId",
                new MpaMapper());
        return allMpa;
    }
}
