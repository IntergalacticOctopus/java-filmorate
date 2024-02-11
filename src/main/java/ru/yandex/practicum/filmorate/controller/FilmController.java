package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final ValidateService validateService = new ValidateService();
    private final InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    private final FilmService filmService = new FilmService(validateService, inMemoryFilmStorage);


    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film = filmService.createFilm (film);
        log.info("Creating film {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating film {}", film);
        return filmService.updateFilm (film);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Get all films");
        return filmService.getAll();
    }
    //http://localhost:8080/films/1/like/1
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("User {} liked film {}", userId, id);
        Film film = filmService.addLike(userId, id);
        return film;
    }
    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("User {} remove like film {}", userId, id);
        Film film = filmService.removeLike(userId, id);
        return film;
    }
    @GetMapping("/popular")
    public List<Film> getMovieRatings(@RequestParam(defaultValue = "10") Long count) {
        log.info("Get movie rating with {} count", count);
        List films = filmService.getMovieRatings(count);
        log.info(String.valueOf(films));
        return filmService.getMovieRatings(count);
    }

}
