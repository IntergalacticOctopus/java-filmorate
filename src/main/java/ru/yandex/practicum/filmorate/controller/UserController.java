package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> storage = new HashMap<>();
    private int generatedId;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        user.setId(++generatedId);
        storage.put(user.getId(), user);
        log.info("Creating user {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!storage.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Data %s not found", user));
        }
        validate(user);
        storage.put(user.getId(), user);
        log.info("Updating user {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Get all Users");
        return new ArrayList<>(storage.values());
    }


    public void validate(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
