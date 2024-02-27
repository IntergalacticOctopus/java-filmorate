package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.Validatable;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final Validatable validateService;
    private final UserStorage userStorage;


    @Autowired
    public UserService(Validatable validateService, UserStorage userStorage) {
        this.validateService = validateService;
        this.userStorage = userStorage;
    }

    ;

    public User createUser(User user) {
        validateService.validate(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        isUserExist(user.getId());
        validateService.validate(user);
        return userStorage.updateUser(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(Long id) {
        isUserExist(id);
        return userStorage.getUserById(id);
    }

    private void isUserExist(long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("This user" + user + "does not exist ");
        }
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new ValidationException("userId == friendId");
        }

        isUserExist(userId);
        isUserExist(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriendsList(Long userId) {
        isUserExist(userId);
        return userStorage.getFriendsList(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        isUserExist(userId);
        isUserExist(friendId);
        return userStorage.getCommonFriends(userId, friendId);
    }
}
