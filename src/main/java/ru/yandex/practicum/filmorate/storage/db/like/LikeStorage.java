package ru.yandex.practicum.filmorate.storage.db.like;

public interface LikeStorage {
    void add(Long filmId, Long userId);

    void remove(Long filmId, Long userId);
}
