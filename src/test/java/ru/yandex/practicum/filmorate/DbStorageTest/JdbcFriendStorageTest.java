package ru.yandex.practicum.filmorate.DbStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.friendship.JdbcFriendStorage;
import ru.yandex.practicum.filmorate.storage.db.user.JdbcUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcFriendStorageTest {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    JdbcFriendStorage friendStorage;
    JdbcUserStorage userStorage;
    User firstUser;
    User secondUser;
    User thirdUser;
    User incorrectUser;

    @BeforeEach
    protected void beforeEach() {
        userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
        friendStorage = new JdbcFriendStorage(namedParameterJdbcTemplate);

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
    public void addAndGetFriendsTest() {
        userStorage.create(firstUser);
        User user2 = userStorage.create(secondUser);
        User user3 = userStorage.create(thirdUser);
        friendStorage.add(1L, 2L, true);
        friendStorage.add(1L, 3L, true);
        List<User> list = new ArrayList<>();
        list.add(userStorage.getById(user2.getId()));
        list.add(userStorage.getById(user3.getId()));
        assertEquals(list, friendStorage.getFriends(1L));
    }

    @Test
    public void removeFriendTest() {
        userStorage.create(firstUser);
        User user2 = userStorage.create(secondUser);
        User user3 = userStorage.create(thirdUser);
        friendStorage.add(1L, 2L, true);
        friendStorage.add(1L, 3L, true);
        List<User> list = new ArrayList<>();
        list.add(userStorage.getById(user2.getId()));
        list.add(userStorage.getById(user3.getId()));
        assertEquals(list, friendStorage.getFriends(1L));
        list.remove(1);
        friendStorage.removeFriend(1L, 3L);
        assertEquals(list, friendStorage.getFriends(1L));
    }

    @Test
    public void getCommonFriends() {
        userStorage.create(firstUser);
        User user2 = userStorage.create(secondUser);
        userStorage.create(thirdUser);
        friendStorage.add(1L, 2L, true);
        friendStorage.add(3L, 2L, true);
        List<User> list = new ArrayList<>();
        list.add(user2);
        assertEquals(list, friendStorage.getCommonFriends(1L, 3L));
    }

}
