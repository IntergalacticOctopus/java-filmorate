package ru.yandex.practicum.filmorate.storage.db.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {
    Mpa getMpaById(Long id);

    List<Mpa> getAll();
}
