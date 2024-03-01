package ru.yandex.practicum.filmorate.DbStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.film.JdbcFilmStorage;
import ru.yandex.practicum.filmorate.storage.db.like.JdbcLikeStorage;
import ru.yandex.practicum.filmorate.storage.db.user.JdbcUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcFilmStorageTest {


    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;
    JdbcFilmStorage filmStorage;
    JdbcUserStorage userStorage;
    JdbcLikeStorage likeStorage;
    Film correctFilm;
    Film secondCorrectFilm;
    Film incorrectFilm;
    User firstUser;
    User secondUser;
    User thirdUser;

    @BeforeEach
    protected void beforeEach() {
        filmStorage = new JdbcFilmStorage(namedParameterJdbcTemplate);
        userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
        likeStorage = new JdbcLikeStorage(jdbcTemplate);

        correctFilm = new Film();
        correctFilm.setName("name");
        correctFilm.setDuration(100L);
        correctFilm.setMpa(new Mpa(3L, "PG-13"));
        correctFilm.setReleaseDate(LocalDate.of(2000, 10, 10));
        correctFilm.setDescription("Description");

        secondCorrectFilm = new Film();
        secondCorrectFilm.setName("name2");
        secondCorrectFilm.setDuration(1002L);
        secondCorrectFilm.setMpa(new Mpa(3L, "PG-13"));
        secondCorrectFilm.setReleaseDate(LocalDate.of(2000, 10, 10));
        secondCorrectFilm.setDescription("Description2");


        incorrectFilm = new Film();
        incorrectFilm.setName("123");
        incorrectFilm.setDuration(-100L);
        incorrectFilm.setMpa(new Mpa(100000L, "1934-PG-13"));
        incorrectFilm.setReleaseDate(LocalDate.of(3000, 10, 10));
        //300 символов
        incorrectFilm.setDescription("qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop");
    }

    @Test
    public void createAndGetByIdTest() {
        Film film = filmStorage.create(correctFilm);
        Film createdFilm = filmStorage.getById(film.getId());
        assertNotNull(createdFilm, "Film does not exist");
        assertEquals(film.getId(), 1);
        assertEquals(film.getName(), "name");
        assertEquals(film.getDuration(), 100L);
        assertEquals(film.getMpa(), new Mpa(3L, "PG-13"));
        assertEquals(film.getReleaseDate(), LocalDate.of(2000, 10, 10));
        assertEquals(film.getDescription(), "Description");
    }

    @Test
    public void incorrectCreateTest() {
        assertThrows(Exception.class, () -> {
            filmStorage.create(incorrectFilm);
        });
    }


    @Test
    public void notFoundFilmTest() {
        assertThrows(NotFoundException.class, () -> {
            Film film = filmStorage.getById(10000L);
            if (film == null) {
                throw new NotFoundException("Film does not exist");
            }
        });
    }

    @Test
    public void testUpdate() {
        Film film = filmStorage.create(correctFilm);

        Film newFilm = new Film();

        newFilm.setId(1L);
        newFilm.setName("name2");
        newFilm.setDuration(50L);
        newFilm.setMpa(new Mpa(1L, "G"));
        newFilm.setReleaseDate(LocalDate.of(1950, 10, 10));
        newFilm.setDescription("123Description123");

        filmStorage.update(newFilm);

        Film updatedFilm = filmStorage.getById(film.getId());

        assertEquals(newFilm.getId(), updatedFilm.getId());
        assertEquals(newFilm.getName(), updatedFilm.getName());
        assertEquals(newFilm.getDuration(), updatedFilm.getDuration());
        assertEquals(newFilm.getMpa(), updatedFilm.getMpa());
        assertEquals(newFilm.getReleaseDate(), updatedFilm.getReleaseDate());
        assertEquals(newFilm.getDescription(), updatedFilm.getDescription());
    }

    @Test
    public void incorrectUpdate() {
        filmStorage.create(correctFilm);
        Film newFilm = new Film();
        newFilm.setId(1L);
        newFilm.setName("newName");
        newFilm.setDuration(-50L);
        newFilm.setMpa(new Mpa(00000111111L, "123-G-123"));
        newFilm.setReleaseDate(LocalDate.of(30000, 10, 10));
        assertThrows(Exception.class, () -> {
            filmStorage.update(newFilm);
            filmStorage.update(newFilm);
        });
    }


    @Test
    public void getAllTest() {
        Film film1 = filmStorage.create(correctFilm);
        Film film2 = filmStorage.create(secondCorrectFilm);
        List<Film> list = new ArrayList<>();
        list.add(film1);
        list.add(film2);
        assertEquals(list, filmStorage.getAll());
    }

    @Test
    public void getPopularFilmsTest() {
        firstUser = new User();
        firstUser.setName("name1");
        firstUser.setBirthday(LocalDate.of(1990, 1, 1));
        firstUser.setEmail("abc@yandex.ru");
        firstUser.setLogin("login1");

        secondUser = new User();
        secondUser.setName("name2");
        secondUser.setBirthday(LocalDate.of(2000, 1, 1));
        secondUser.setEmail("abc2@yandex.ru");
        secondUser.setLogin("login2");

        thirdUser = new User();
        thirdUser.setName("name3");
        thirdUser.setBirthday(LocalDate.of(2000, 1, 1));
        thirdUser.setEmail("abc3@yandex.ru");
        thirdUser.setLogin("login3");

        userStorage.create(firstUser);
        userStorage.create(secondUser);
        userStorage.create(thirdUser);

        filmStorage.create(correctFilm);
        filmStorage.create(secondCorrectFilm);

        likeStorage.add(1L, 1L);
        likeStorage.add(1L, 2L);
        likeStorage.add(1L, 3L);

        likeStorage.add(2L, 3L);
        likeStorage.add(2L, 2L);
        List<Film> list = new ArrayList<>();
        list.add(secondCorrectFilm);
        list.add(correctFilm);
        assertEquals(list, filmStorage.getPopularFilms(2));
    }
}
