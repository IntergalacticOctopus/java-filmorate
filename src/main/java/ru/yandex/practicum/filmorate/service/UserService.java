package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.Validatable;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.friendship.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.db.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.db.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final Validatable validateService;
    private final UserStorage userStorage;
    private final FriendshipDao friendshioDbStorage;

    @Autowired
    public UserService(Validatable validateService, UserDbStorage userStorage, FriendshipDao friendshioDbStorage) {
        this.validateService = validateService;
        this.userStorage = userStorage;
        this.friendshioDbStorage = friendshioDbStorage;
    }

    public User createUser(User user) {
        toCorrectName(user);
        validateService.validate(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        toCorrectName(user);
        validateService.validate(user);
        return userStorage.updateUser(user);
    }

    public void toCorrectName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
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
            throw new NotFoundException("User" + user + "not exist");
        }
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new ValidationException("userId == friendId");
        }
        isUserExist(userId);
        isUserExist(friendId);
        boolean isUsersFriends = friendshioDbStorage.isFriend(userId, friendId);
        friendshioDbStorage.addFriend(userId, friendId, isUsersFriends);
    }

    public void removeFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        friendshioDbStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriendsList(Long userId) {
        isUserExist(userId);
        return friendshioDbStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        isUserExist(userId);
        isUserExist(friendId);
        return friendshioDbStorage.getCommonFriends(userId, friendId);
    }
}
