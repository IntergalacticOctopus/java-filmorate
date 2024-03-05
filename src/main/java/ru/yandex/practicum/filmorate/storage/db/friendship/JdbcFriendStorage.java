package ru.yandex.practicum.filmorate.storage.db.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcFriendStorage implements FriendStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void add(Long userId, Long friendId) {
        String insertQuery = "INSERT INTO friends (user_id, friend_id) VALUES (:user_id, :friend_id)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId);

        namedParameterJdbcTemplate.update(insertQuery, params);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId);

        namedParameterJdbcTemplate.update("DELETE FROM friends WHERE user_id = :user_id AND friend_id = :friend_id",
                params);

    }

    @Override
    public List<User> getFriends(Long userId) {
        String selectQuery = "SELECT u.* FROM users AS u " +
                "LEFT JOIN friends AS f ON u.user_id = f.friend_id " +
                "WHERE f.user_id = :user_id";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("user_id", userId);

        List<User> friendList = namedParameterJdbcTemplate.query(selectQuery, params, new UserMapper());

        return friendList;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        String sql = "SELECT u.* FROM users AS u " +
                "LEFT JOIN friends AS f1 ON u.user_id = f1.friend_id AND f1.user_id = :userId " +
                "LEFT JOIN friends AS f2 ON u.user_id = f2.friend_id AND f2.user_id = :otherUserId " +
                "GROUP BY u.user_id " +
                "HAVING COUNT(f1.friend_id) > 0 AND COUNT(f2.friend_id) > 0";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", id)
                .addValue("otherUserId", otherId);

        List<User> commonFriends = namedParameterJdbcTemplate.query(sql, params, new UserMapper());

        return commonFriends;
    }

}
