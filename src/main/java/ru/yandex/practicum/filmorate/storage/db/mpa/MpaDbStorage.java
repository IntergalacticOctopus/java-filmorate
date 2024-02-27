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
        try {
            String sql = "SELECT * FROM mpa WHERE id=:id";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", id);
            Mpa mpa = namedParameterJdbcTemplate.queryForObject(sql, params, new MpaMapper());
            return mpa;
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Data not found");
        }
    }

    @Override
    public List<Mpa> getAll() {
        List<Mpa> allMpa = namedParameterJdbcTemplate.query("SELECT * FROM mpa ORDER BY id",
                new MpaMapper());
        return allMpa;
    }
}
