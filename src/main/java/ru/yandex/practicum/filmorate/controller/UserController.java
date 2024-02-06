package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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

    ValidateService validateService = new ValidateService();
    private final Map<Long, User> storage = new HashMap<>();
    private long generatedId;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        initName(user);
        validateService.validate(user);
        user.setId(++generatedId);
        storage.put(user.getId(), user);
        log.info("Creating user {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateService.validate(user);
        if (user.getId() == (null)) {
            throw new ValidationException("Films id == null");
        }
        if (!storage.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Data %s not found", user));
        }
        initName(user);
        storage.put(user.getId(), user);
        log.info("Updating user {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Get all Users");
        return new ArrayList<>(storage.values());
    }


    public void initName(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
