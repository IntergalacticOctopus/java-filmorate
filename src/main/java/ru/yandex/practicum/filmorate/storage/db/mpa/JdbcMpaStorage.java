package ru.yandex.practicum.filmorate.storage.db.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
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
        String sql = "SELECT * FROM mpa WHERE mpaId=:mpaId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mpaId", id);
        List<Mpa> mpa = namedParameterJdbcTemplate.query(sql, params, new MpaMapper());
        if (mpa.isEmpty()) {
            return null;
        }
        return mpa.get(0);
    }

    @Override
    public List<Mpa> getAll() {
        List<Mpa> allMpa = namedParameterJdbcTemplate.query("SELECT * FROM mpa ORDER BY mpaId",
                new MpaMapper());
        return allMpa;
    }
}
