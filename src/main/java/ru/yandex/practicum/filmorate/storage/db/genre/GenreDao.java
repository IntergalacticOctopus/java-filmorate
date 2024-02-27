package ru.yandex.practicum.filmorate.storage.db.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    Genre getGenreById(Long id);

    List<Genre> getGenres();
}
