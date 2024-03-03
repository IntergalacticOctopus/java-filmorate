package ru.yandex.practicum.filmorate.storage.db.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getById(Long id);

    List<Genre> getAll();

    List<Genre> getIds(List<Long> ids);
}
