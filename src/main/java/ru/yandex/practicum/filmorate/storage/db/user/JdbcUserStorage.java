package ru.yandex.practicum.filmorate.storage.db.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.Date;
import java.util.List;

@Slf4j
@Component("UserDbStorage")
@RequiredArgsConstructor
public class JdbcUserStorage implements UserStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User create(User user) {
        String insertQuery = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (:email, :login, :name, :birthday)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", Date.valueOf(user.getBirthday()));

        namedParameterJdbcTemplate.update(insertQuery, params, keyHolder, new String[]{"user_id"});

        Number generatedUserId = (Number) keyHolder.getKeys().get("user_id");
        user.setId(generatedUserId != null ? generatedUserId.longValue() : null);

        return user;
    }

    @Override
    public User update(User user) {
        getById(user.getId());

        String updateQuery = "UPDATE users SET email=:email, login=:login, name=:name, birthday=:birthday WHERE user_id=:id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", Date.valueOf(user.getBirthday()))
                .addValue("id", user.getId());

        namedParameterJdbcTemplate.update(updateQuery, params);

        return getById(user.getId());
    }

    @Override
    public User getById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id=:user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        List<User> returnedUser = namedParameterJdbcTemplate.query(sql, params, new UserMapper());

        if (returnedUser.isEmpty()) {
            return null;
        } else {
            return returnedUser.get(0);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = namedParameterJdbcTemplate.query(
                "SELECT user_id, email, login, name, birthday FROM users",
                new UserMapper());
        return users;
    }
}