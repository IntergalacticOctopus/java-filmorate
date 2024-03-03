package ru.yandex.practicum.filmorate.DbStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.genre.JdbcGenreStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcGenreStorageTest {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    GenreStorage genreStorage;


    @BeforeEach
    protected void beforeEach() {
        genreStorage = new JdbcGenreStorage(namedParameterJdbcTemplate);
    }

    @Test
    public void getByIdTest() {
        Genre genre = genreStorage.getById(1L);
        assertEquals(genre, new Genre(1L, "Комедия"));
    }

    @Test
    public void wrongGetByIdTest() {
        assertThrows(NotFoundException.class, () -> {
            Genre genre = genreStorage.getById(10000L);
            if (genre == null) {
                throw new NotFoundException("Genre does not exist");
            }
        });
    }

    @Test
    public void getAllTest() {
        List<Genre> list = new ArrayList<>();
        list.add(new Genre(1L, "Комедия"));
        list.add(new Genre(2L, "Драма"));
        list.add(new Genre(3L, "Мультфильм"));
        list.add(new Genre(4L, "Триллер"));
        list.add(new Genre(5L, "Документальный"));
        list.add(new Genre(6L, "Боевик"));
        assertEquals(list, genreStorage.getAll());
    }
}
