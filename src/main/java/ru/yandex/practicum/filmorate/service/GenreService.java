package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreDao;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public Genre getById(Long id) {
        Genre genre = genreDao.getGenreById(id);
        if (genre == null) {
            throw new NotFoundException("This genre does not exist");
        }
        return genre;
    }

    public List<Genre> getGenres() {
        return genreDao.getGenres();
    }
}
