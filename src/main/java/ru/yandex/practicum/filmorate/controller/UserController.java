package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final ValidateService validateService;
    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user {}", user);
        user = userService.create(user);
        log.info("Created user {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Updating user {}", user);
        validateService.validate(user);
        User returnUser = userService.update(user);
        log.info("Updated user {}", user);
        return returnUser;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Getting all users");
        List list = userService.getAll();
        log.info("Get user list");
        return list;
    }


    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        log.info("Getting user {}", id);
        User returnUser = userService.getById(id);
        log.info("Get user {}", returnUser);
        return returnUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("User {} adding friend {}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("User {} add friend {}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Removing friend {}", friendId);
        userService.removeFriend(id, friendId);
        log.info("Remove friend {}", friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Getting user {} friends list", id);
        List list = userService.getFriendsList(id);
        log.info("Get user {} friends list {}", id, list);
        return list;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Getting user {} common friends list", id);
        List list = userService.getCommonFriends(id, otherId);
        log.info("Getting user {} common friends list {}", id, list);
        return list;
    }


}
