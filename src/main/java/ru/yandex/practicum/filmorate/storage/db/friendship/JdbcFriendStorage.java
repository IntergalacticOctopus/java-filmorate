package ru.yandex.practicum.filmorate.storage.db.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcFriendStorage implements FriendStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void add(Long userId, Long friendId, boolean isFriend) {
        String insertQuery = "INSERT INTO friends (user_id, friend_id, is_friend) VALUES (:user_id, :friend_id, :is_friend)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId)
                .addValue("is_friend", isFriend);

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
                "LEFT JOIN friends AS f ON u.user_id=f.friend_id " +
                "WHERE f.user_id=:user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        List<User> firstUserFriends = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
        params.addValue("user_id", otherId);
        List<User> secondUserFriends = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
        List<User> resultList = secondUserFriends.stream().filter(firstUserFriends::contains)
                .filter(secondUserFriends::contains)
                .collect(Collectors.toList());
        return resultList;
    }

}
