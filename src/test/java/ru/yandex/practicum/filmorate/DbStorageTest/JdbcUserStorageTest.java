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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.film.JdbcFilmStorage;
import ru.yandex.practicum.filmorate.storage.db.like.JdbcLikeStorage;
import ru.yandex.practicum.filmorate.storage.db.user.JdbcUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcUserStorageTest {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;
    JdbcFilmStorage filmStorage;
    JdbcUserStorage userStorage;
    JdbcLikeStorage likeStorage;
    User firstUser;
    User secondUser;
    User thirdUser;
    User incorrectUser;

    @BeforeEach
    protected void beforeEach() {
        filmStorage = new JdbcFilmStorage(namedParameterJdbcTemplate);
        userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
        likeStorage = new JdbcLikeStorage(jdbcTemplate);

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

        incorrectUser = new User();


    }

    @Test
    public void createAndGetByIdTest() {
        User user = userStorage.create(firstUser);
        User createdUser = userStorage.getById(user.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getBirthday(), createdUser.getBirthday());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getLogin(), createdUser.getLogin());
    }


    @Test
    public void updateTest() {
        User user = userStorage.create(firstUser);

        secondUser.setId(1L);
        User updatedUser = userStorage.update(secondUser);
        User user2 = userStorage.getById(user.getId());

        assertEquals(updatedUser.getName(), user2.getName());
        assertEquals(updatedUser.getBirthday(), user2.getBirthday());
        assertEquals(updatedUser.getEmail(), user2.getEmail());
        assertEquals(updatedUser.getLogin(), user2.getLogin());
    }

    @Test
    public void getAllTest() {
        userStorage.create(firstUser);
        userStorage.create(secondUser);
        userStorage.create(thirdUser);
        List<User> list = new ArrayList<>();
        firstUser.setId(1L);
        secondUser.setId(2L);
        thirdUser.setId(3L);
        list.add(firstUser);
        list.add(secondUser);
        list.add(thirdUser);
        assertEquals(list, userStorage.getAll());
    }

    @Test
    public void incorrectCreateUser() {
        assertThrows(Exception.class, () -> {
            userStorage.create(incorrectUser);
        });
    }

    @Test
    public void incorrectUpdateUser() {
        assertThrows(Exception.class, () -> {
            userStorage.update(incorrectUser);
        });
    }

    @Test
    public void notFoundUserTest() {
        assertThrows(NotFoundException.class, () -> {
            User user = userStorage.getById(10000L);
            if (user == null) {
                throw new NotFoundException("Film does not exist");
            }
        });
    }

}