package ru.yandex.practicum.filmorate.storage.local;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.Validatable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.user.UserStorage;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Validatable validateService;

    @Autowired
    public InMemoryUserStorage(Validatable validateService) {
        this.validateService = validateService;
    }


    private static final Map<Long, User> storage = new HashMap<>();
    private static final Map<Long, Set<Long>> friendsStorage = new HashMap<>();


    private long generatedId;

    @Override
    public User createUser(User user) {
        initName(user);
        user.setId(++generatedId);
        storage.put(user.getId(), user);
        friendsStorage.put(user.getId(), new HashSet<>());
        return user;
    }

    @Override
    public User updateUser(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    public void initName(User user) {
        if (user.getName() == null || user.getName() == "") {
            user.setName(user.getLogin());
        }
    }

    @Override
    public List<User> getAll() {
        log.info("Get all Users");
        return new ArrayList<>(storage.values());
    }


    @Override
    public void addFriend(Long userId, Long friendId, boolean isUsersFriends) {
        Set firstUserFriends = friendsStorage.get(userId);
        if (firstUserFriends != null) {
            firstUserFriends.add(friendId);
        } else {
            Set<Long> friends = new HashSet<>();
            friends.add(friendId);
            friendsStorage.put(userId, friends);
        }

        Set secondUserFriends = friendsStorage.get(friendId);
        if (secondUserFriends != null) {
            secondUserFriends.add(userId);
        } else {
            Set<Long> friends = new HashSet<>();
            friends.add(userId);
            friendsStorage.put(friendId, friends);
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        Set firstUserFriends = friendsStorage.get(userId);
        Set secondUserFriends = friendsStorage.get(friendId);
        User friend = getUserById(friendId);
        // Если у первого юзера нет в друзьях второго, значит в друзья второго не добавлялся первый
        if (firstUserFriends == null || secondUserFriends == null) {
            return;
        } else {
            // Вызов remove уберает друзей из мап друг друга
            firstUserFriends.remove(friendId);
            secondUserFriends.remove(userId);
        }
    }

    @Override
    public List<User> getFriendsList(Long userId) {


        Set<Long> friendsId = friendsStorage.get(userId);
        if (friendsId == null) {
            List<User> returnList = new ArrayList<>();
            return returnList;
        }

        List<User> friendsList = new ArrayList<>();
        if (friendsId.size() == 0) {
            return friendsList;
        }

        for (Long id : friendsId) {
            friendsList.add(storage.get(id));
        }
        return friendsList;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        List<User> userFriends = getFriendsList(userId);
        List<User> friendFriends = getFriendsList(friendId);
        userFriends.retainAll(friendFriends);
        return userFriends;
    }

    @Override
    public User getUserById(Long id) {
        return storage.get(id);
    }

    @Override
    public boolean isContains(Long id) {
        return false;
    }
}