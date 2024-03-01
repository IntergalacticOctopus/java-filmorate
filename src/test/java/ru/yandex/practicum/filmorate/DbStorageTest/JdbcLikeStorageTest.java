package ru.yandex.practicum.filmorate.DbStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.film.JdbcFilmStorage;
import ru.yandex.practicum.filmorate.storage.db.like.JdbcLikeStorage;
import ru.yandex.practicum.filmorate.storage.db.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.db.user.JdbcUserStorage;
import ru.yandex.practicum.filmorate.storage.db.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcLikeStorageTest {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    FilmStorage filmStorage;
    UserStorage userStorage;
    LikeStorage likeStorage;
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
    }

    @Test
    public void addAndRemoveTest() {
        userStorage.create(firstUser);
        userStorage.create(secondUser);
        userStorage.create(thirdUser);
        filmStorage.create(correctFilm);
        likeStorage.add(1L, 1L);
        likeStorage.add(1L, 2L);
        likeStorage.add(1L, 3L);
        filmStorage.create(secondCorrectFilm);
        likeStorage.add(1L, 1L);
        likeStorage.add(1L, 2L);
        List<Film> list = new ArrayList<>();

        list.add(secondCorrectFilm);
        list.add(correctFilm);
        assertEquals(list, filmStorage.getPopularFilms(2));

        likeStorage.remove(1L, 1L);
        likeStorage.remove(1L, 2L);

        assertEquals(list, filmStorage.getPopularFilms(2));
    }
}
