package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAll();

    Map<Long, User> getStorage();

    Map<Long, Set<Long>> getFriendsStorage();

    User addFriend(Long userId, Long friendId);

    User removeFriend(Long userId, Long friendId);

    List<User> getFriendsList(Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);

}
