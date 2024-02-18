package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final Map<Long, Film> storage = new HashMap<>();
    private static final Map<Long, Set<Long>> likesStorage = new HashMap<>();
    private final FilmComparator comparator = new FilmComparator();

    @Override
    public Map<Long, Film> getStorage() {
        return storage;
    }

    @Override
    public Map<Long, Set<Long>> getLikesStorage() {
        return likesStorage;
    }


    private long generatedId;

    @Override
    public Film createFilm(Film film) {
        film.setId(++generatedId);
        storage.put(film.getId(), film);
        likesStorage.put(film.getId(), new HashSet<>());

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        storage.put(film.getId(), film);

        return film;
    }


    @Override
    public List<Film> getAll() {

        return new ArrayList<>(storage.values());
    }

    @Override
    public Film addLike(Long userId, Long filmId) {
        Set<Long> likeList;
        Film film = storage.get(filmId);
        film.setLikesCounter(film.getLikesCounter() + 1);
        if (likesStorage.get(filmId) == null) {
            likeList = new HashSet<>();
            likeList.add(userId);
        } else {
            likeList = likesStorage.get(filmId);
            likeList.add(userId);
        }
        likesStorage.put(filmId, likeList);
        return updateFilm(film);
    }

    @Override
    public Film removeLike(Long userId, Long filmId) {
        Set<Long> likes = likesStorage.get(filmId);
        likes.remove(userId);
        Film film = storage.get(filmId);
        film.setLikesCounter(film.getLikesCounter() - 1);
        likesStorage.put(filmId, likes);

        return updateFilm(film);
    }

    @Override
    public List<Film> getMovieRatings(long count) {
        List<Film> list = storage.values().stream()
                .sorted(comparator)
                .limit(Math.min(storage.size(), count))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public Film getFilmById(long id) {
        return storage.get(id);
    }

}
