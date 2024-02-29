package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre getById(Long id) {
        Genre genre = genreStorage.getById(id);
        if (genre == null) {
            throw new NotFoundException("This genre does not exist");
        }
        return genre;
    }

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }
}
