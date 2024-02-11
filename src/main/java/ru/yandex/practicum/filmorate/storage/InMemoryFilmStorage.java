package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {


    public Map<Long, Film> getStorage() {
        return storage;
    }

    private final Map<Long, Film> storage = new HashMap<>();
    private long generatedId;

    @Override
    public Film createFilm(Film film) {
        film.setId(++generatedId);
        storage.put(film.getId(), film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Films id == null");
        }
        if (!storage.containsKey(film.getId())) {
            throw new NotFoundException(String.format("Data %s not found", film));
        }
        storage.put(film.getId(), film);

        return film;
    }


    @Override
    public List<Film> getAll() {

        return new ArrayList<>(storage.values());
    }
}
