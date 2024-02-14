package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Validatable validateService = new ValidateService();
    private final FilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    private final UserStorage inMemoryUserStorage = new InMemoryUserStorage(validateService);
    private final FilmService filmService = new FilmService(validateService, inMemoryFilmStorage, inMemoryUserStorage);


    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Creating film {}", film);
        film = filmService.createFilm(film);
        log.info("Film {} created", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating film {}", film);
        Film returnFilm = filmService.updateFilm(film);
        log.info("Film {} updated", film);
        return returnFilm;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Getting all films");
        List<Film> returnlist = filmService.getAll();
        log.info("Get all films {}", returnlist);
        return returnlist;
    }


    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Getting films by id {}", id);
        Film returnfilm = filmService.getFilmById(id);
        log.info("Get films by id {}", id);
        return returnfilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Liking film {} by user {}", id, userId);
        Film film = filmService.addLike(userId, id);
        log.info("Liked film {} by user {}", id, userId);
        return film;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("User {} removing like film {}", userId, id);
        Film film = filmService.removeLike(userId, id);
        log.info("User {} remove like film {}", userId, id);
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getMovieRatings(@RequestParam(defaultValue = "10") Long count) {
        log.info("Getting movie rating with {} count", count);
        List films = filmService.getMovieRatings(count);
        log.info("Get movie rating {} with {} count", films, count);
        return films;
    }

}
