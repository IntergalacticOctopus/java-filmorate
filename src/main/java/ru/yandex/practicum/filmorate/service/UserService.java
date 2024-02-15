package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.Validatable;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {
    private final Validatable validateService;
    private final UserStorage inMemoryUserStorage;
    Map<Long, User> storage;
    Map<Long, Set<Long>> friendsStorage;


    @Autowired
    public UserService(Validatable validateService, UserStorage inMemoryUserStorage) {
        this.validateService = validateService;
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.storage = inMemoryUserStorage.getStorage();
        this.friendsStorage = inMemoryUserStorage.getFriendsStorage();
    }

    ;

    public User createUser(User user) {
        validateService.validate(user);
        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateService.validate(user);
        return inMemoryUserStorage.updateUser(user);
    }

    public List<User> getAll() {
        return inMemoryUserStorage.getAll();
    }

    public User getUserById(Long id) {
        User user = storage.get(id);
        if (user == null) {
            throw new NotFoundException("This user does not exist");
        }
        return user;
    }

    public User addFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new ValidationException("userId == friendId");
        }
        if (storage.get(userId) == null || storage.get(friendId) == null) {
            throw new NotFoundException("User or friend does not exist");
        }
        return inMemoryUserStorage.addFriend(userId, friendId);
    }

    public User removeFriend(Long userId, Long friendId) {
        User firstUser = storage.get(userId);
        User secondUser = storage.get(friendId);
        validateService.validate(firstUser, secondUser);
        return inMemoryUserStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriendsList(Long userId) {
        User user = storage.get(userId);
        if (user == null) {
            throw new NotFoundException("User does not exist");
        }
        return inMemoryUserStorage.getFriendsList(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        if (storage.get(userId) == null || storage.get(friendId) == null) {
            throw new NotFoundException("User or friend does not exist");
        }
        return inMemoryUserStorage.getCommonFriends(userId, friendId);
    }
}
