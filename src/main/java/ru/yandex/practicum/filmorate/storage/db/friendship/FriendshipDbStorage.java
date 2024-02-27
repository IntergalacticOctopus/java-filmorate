package ru.yandex.practicum.filmorate.storage.db.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.FriendshipMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId, boolean isFriend) {
        log.debug("addFriend({}, {}, {})", userId, friendId, isFriend);
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id, is_friend) VALUES(?, ?, ?)",
                userId, friendId, isFriend);
        Friendship friendship = getFriend(userId, friendId);
        log.trace("These users are friends now: {}", friendship);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        log.debug("deleteFriend({}, {})", userId, friendId);
        Friendship friendship = Objects.requireNonNull(getFriend(userId, friendId));
        jdbcTemplate.update("DELETE FROM friends WHERE user_id=? AND friend_id=?", userId, friendId);
        if (friendship.isFriend()) {
            jdbcTemplate.update("UPDATE friends SET is_friend=false WHERE user_id=? AND friend_id=?",
                    userId, friendId);
            log.debug("The friendship between {} and {} is over", userId, friendId);
        }
        log.trace("They are not friends now: {}", friendship);
    }

    @Override
    public List<User> getFriends(Long userId) {
        List<User> list = jdbcTemplate.query("SELECT u.* FROM users AS u " +
                        "LEFT OUTER JOIN friends AS f ON u.user_id=f.friend_id " +
                        "WHERE f.user_id=?",
                new UserMapper(), userId);
        return list;
    }

    @Override
    public Friendship getFriend(Long userId, Long friendId) {
        log.debug("getFriend({}, {})", userId, friendId);
        return jdbcTemplate.queryForObject(
                "SELECT user_id, friend_id, is_friend FROM friends WHERE user_id=? AND friend_id=?",
                new FriendshipMapper(), userId, friendId);
    }

    @Override
    public boolean isFriend(Long userId, Long friendId) {
        log.debug("isFriend({}, {})", userId, friendId);
        try {
            getFriend(userId, friendId);
            log.trace("Found friendship between {} and {}", userId, friendId);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            log.trace("No friendship were found between {} and {}", userId, friendId);
            return false;
        }
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> firstUserFriends = jdbcTemplate.query("SELECT u.* FROM users AS u " +
                        "LEFT OUTER JOIN friends AS f ON u.user_id=f.friend_id " +
                        "WHERE f.user_id=? AND f.friend_id",
                new UserMapper(), id);
        List<User> secondUserFriends = jdbcTemplate.query("SELECT u.* FROM users AS u " +
                        "LEFT OUTER JOIN friends AS f ON u.user_id=f.friend_id " +
                        "WHERE f.user_id=?",
                new UserMapper(), otherId);
        List<User> resultList = secondUserFriends.stream().filter(firstUserFriends::contains)
                .filter(secondUserFriends::contains)
                .collect(Collectors.toList());
        return resultList;
    }
}
