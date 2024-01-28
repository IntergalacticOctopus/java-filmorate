package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmorateValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> storage = new HashMap<>();
    private int generatedId;
    private final LocalDate startReleaseDate = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(++generatedId);
        storage.put(film.getId(), film);
        log.info("Creating film {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!storage.containsKey(film.getId())) {
            throw new NotFoundException(String.format("Data %s not found", film));
        }
        validate(film);
        storage.put(film.getId(), film);
        log.info("Updating film {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Get all films");
        return new ArrayList<>(storage.values());
    }


    public void validate(Film data) {
        if (data.getReleaseDate().isBefore(startReleaseDate)) {
            throw new FilmorateValidationException("Film release date is invalid");
        }
    }

}
