package ru.yandex.practicum.filmorate.DbStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.mpa.JdbcMpaStorage;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcMpaStorageTest {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    MpaStorage mpaStorage;

    @BeforeEach
    public void beforeEach() {
        mpaStorage = new JdbcMpaStorage(namedParameterJdbcTemplate);
    }

    @Test
    public void getByIdTest() {
        Mpa mpa = mpaStorage.getById(1L);
        assertEquals(mpa, new Mpa(1L, "G"));
    }

    @Test
    public void wrongGetByIdTest() {
        assertThrows(NotFoundException.class, () -> {
            Mpa mpa = mpaStorage.getById(10000L);
            if (mpa == null) {
                throw new NotFoundException("Genre does not exist");
            }
        });
    }

    @Test
    public void getAllTest() {
        List<Mpa> list = new ArrayList<>();
        list.add(new Mpa(1L, "G"));
        list.add(new Mpa(2L, "PG"));
        list.add(new Mpa(3L, "PG-13"));
        list.add(new Mpa(4L, "R"));
        list.add(new Mpa(5L, "NC-17"));
        assertEquals(list, mpaStorage.getAll());
    }
}
