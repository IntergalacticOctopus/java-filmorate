package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    ValidateService validateService = new ValidateService();
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage(validateService);
    private final UserService userService = new UserService(validateService, inMemoryUserStorage);


    @PostMapping
    public User create(@Valid @RequestBody User user) {
        user = userService.createUser(user);
        log.info("Creating user {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateService.validate(user);
        log.info("Updating user {}", user);
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("User {} add friend {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        User user = userService.removeFriend(id, friendId);
        log.info("Remove friend {}", friendId);
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        List list = userService.getFriendsList(id);
        log.info("Get user {} friends list {}", id, list);
        return list;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }


}
