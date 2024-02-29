package ru.yandex.practicum.filmorate.storage.db.like;

public interface LikeStorage {
    void like(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);
}
