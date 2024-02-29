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
public class MpaDbStorage implements MpaDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public @NotNull Mpa getMpaById(Long id) {
        String sql = "SELECT * FROM mpa WHERE mpaId=:mpaId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mpaId", id);

        List<Mpa> mpas = namedParameterJdbcTemplate.query(sql, params, new MpaMapper());
        return mpas.get(0);
    }

    @Override
    public List<Mpa> getAll() {
        List<Mpa> allMpa = namedParameterJdbcTemplate.query("SELECT * FROM mpa ORDER BY mpaId",
                new MpaMapper());
        return allMpa;
    }
}
