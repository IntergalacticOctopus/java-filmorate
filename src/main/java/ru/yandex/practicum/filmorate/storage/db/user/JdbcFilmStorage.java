package ru.yandex.practicum.filmorate.storage.db.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.Date;
import java.util.List;

@Slf4j
@Component("UserDbStorage")
@RequiredArgsConstructor
public class JdbcFilmStorage implements UserStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User create(User user) {
        log.debug("createUser({})", user);

        String insertQuery = "INSERT INTO users (email, login, name, birthday) VALUES (:email, :login, :name, :birthday)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", Date.valueOf(user.getBirthday()));

        namedParameterJdbcTemplate.update(insertQuery, params);

        String selectQuery = "SELECT user_id, email, login, name, birthday FROM users WHERE email = :email";

        User thisUser = namedParameterJdbcTemplate.queryForObject(selectQuery, params, new UserMapper());

        log.trace("{} user was added to the data base", thisUser);

        return thisUser;
    }

    @Override
    public User update(User user) {
        getById(user.getId());
        log.debug("updateUser({})", user);

        String updateQuery = "UPDATE users SET email=:email, login=:login, name=:name, birthday=:birthday WHERE user_id=:id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", Date.valueOf(user.getBirthday()))
                .addValue("id", user.getId());

        namedParameterJdbcTemplate.update(updateQuery, params);

        User thisUser = getById(user.getId());

        log.trace("The user {} was updated in the database", thisUser);

        return thisUser;
    }

    @Override
    public User getById(Long id) {
        try {
            String sql = "SELECT * FROM users WHERE user_id=:user_id";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("user_id", id);
            User returnedUser = namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
            return returnedUser;
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Data not found");
        }
    }

    @Override
    public List<User> getAll() {
        log.debug("getUsers()");

        List<User> users = namedParameterJdbcTemplate.query(
                "SELECT user_id, email, login, name, birthday FROM users",
                new UserMapper());

        log.trace("These are users in the database: {}", users);

        return users;
    }

    @Override
    public boolean isContains(Long id) {
        log.debug("isContains({})", id);
        try {
            getById(id);
            log.trace("The user with id {} was found", id);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            log.trace("No information was found for user with id {}", id);
            return false;
        }
    }


    @Override
    public void addFriend(Long userId, Long friendId, boolean isUsersFriends) {

    }

    @Override
    public void removeFriend(Long userId, Long friendId) {

    }

    @Override
    public List<User> getFriendsList(Long userId) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return null;
    }


}
