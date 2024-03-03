package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.Validatable;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.friendship.FriendStorage;
import ru.yandex.practicum.filmorate.storage.db.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final Validatable validateService;
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(Validatable validateService, UserStorage userStorage, FriendStorage friendStorage) {
        this.validateService = validateService;
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User create(User user) {
        validateService.validate(user);
        toCorrectName(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validateService.validate(user);
        getExistingUser(user.getId());
        toCorrectName(user);
        return userStorage.update(user);
    }

    public void toCorrectName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        User user = getExistingUser(id);
        return user;
    }

    private User getExistingUser(long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new NotFoundException("User" + user + "not exist");
        }
        return user;
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new ValidationException("userId == friendId");
        }
        getExistingUser(userId);
        getExistingUser(friendId);
        friendStorage.add(userId, friendId, true);
    }

    public void removeFriend(Long userId, Long friendId) {
        getById(userId);
        getById(friendId);
        friendStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriendsList(Long userId) {
        getExistingUser(userId);
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        getExistingUser(userId);
        getExistingUser(friendId);
        return friendStorage.getCommonFriends(userId, friendId);
    }
}
