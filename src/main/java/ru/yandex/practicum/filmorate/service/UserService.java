package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateService;
import ru.yandex.practicum.filmorate.exception.AlreadyDoneException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component("UserService")
@Service
public class UserService {
    private final ValidateService validateService;
    private final InMemoryUserStorage inMemoryUserStorage;
    Map<Long, User> storage;

    @Autowired
    public UserService(ValidateService validateService, InMemoryUserStorage inMemoryUserStorage) {
        this.validateService = validateService;
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.storage = inMemoryUserStorage.getStorage();
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
        if (id == null || !storage.containsKey(id)) {
            throw new NotFoundException("This user does not exist");
        }
        return storage.get(id);
    }

    public User addFriend(Long userId, Long friendId) {
        if (!storage.containsKey(userId) || !storage.containsKey(userId)) {
            throw new NotFoundException("User or friend does not exist");
        }
        User firstUser = storage.get(userId);
        User secondUser = storage.get(friendId);
        validateService.validate(firstUser, secondUser);
        if (firstUser.getFriends().contains(friendId)) {
            throw new AlreadyDoneException("User and new_friend are already friends");
        }
        Set<Long> friends = firstUser.getFriends();
        friends.add(friendId);
        firstUser.setFriends(friends);

        Set<Long> secondFriends = secondUser.getFriends();
        secondFriends.add(userId);
        secondUser.setFriends(secondFriends);

        inMemoryUserStorage.updateUser(firstUser);
        inMemoryUserStorage.updateUser(secondUser);
        return secondUser;
    }

    public User removeFriend(Long userId, Long friendId) {

        User firstUser = storage.get(userId);
        User secondUser = storage.get(friendId);
        validateService.validate(firstUser, secondUser);
        if (!firstUser.getFriends().contains(friendId)) {
            throw new AlreadyDoneException("User and new_friend are not friends");
        }
        Set<Long> friends = firstUser.getFriends();
        friends.remove(friendId);
        firstUser.setFriends(friends);

        Set<Long> secondFriends = secondUser.getFriends();
        secondFriends.remove(userId);
        secondUser.setFriends(secondFriends);

        inMemoryUserStorage.updateUser(firstUser);
        inMemoryUserStorage.updateUser(secondUser);
        return storage.get(friendId);
    }

    public List<User> getFriendsList(Long userId) {
        User user = storage.get(userId);

        Set<Long> friendsId = user.getFriends();
        List<User> friendsList = new ArrayList<>();
        if (friendsId.size() == 0) {
            return friendsList;
        }
        for (Long id : user.getFriends()) {
            friendsList.add(storage.get(id));
        }

        return friendsList;
    }

    public List<User> getMutualFriends(Long userId, Long friendId) {
        List<User> userFriends = getFriendsList(userId);
        List<User> friendFriends = getFriendsList(friendId);
        userFriends.retainAll(friendFriends);
        return userFriends;
    }
}
