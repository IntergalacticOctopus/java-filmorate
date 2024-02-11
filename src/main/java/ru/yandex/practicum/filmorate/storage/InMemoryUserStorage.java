package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidateService;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final ValidateService validateService;

    @Autowired
    public InMemoryUserStorage(ValidateService validateService) {
        this.validateService = validateService;
    }


    private final Map<Long, User> storage = new HashMap<>();
    private long generatedId;

    @Override
    public User createUser(User user) {
        initName(user);
        user.setId(++generatedId);
        storage.put(user.getId(), user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == (null)) {
            throw new ValidationException("Films id == null");
        }
        if (!storage.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Data %s not found", user));
        }
        storage.put(user.getId(), user);
        return user;
    }

    public void initName(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public List<User> getAll() {
        log.info("Get all Users");
        return new ArrayList<>(storage.values());
    }

    public Map<Long, User> getStorage() {
        return storage;
    }
}
