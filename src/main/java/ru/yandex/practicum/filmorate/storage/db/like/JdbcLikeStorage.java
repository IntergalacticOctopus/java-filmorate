package ru.yandex.practicum.filmorate.storage.db.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcLikeStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long filmId, Long userId) {
        log.debug("like({}, {})", filmId, userId);
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", filmId, userId);
        log.trace("The movie {} liked by user {}", filmId, userId);
    }

    @Override
    public void remove(Long filmId, Long userId) {
        log.debug("dislike({}, {})", filmId, userId);
        jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?", filmId, userId);
        log.trace("The user {}, disliked the movie {}", userId, filmId);
    }
}
