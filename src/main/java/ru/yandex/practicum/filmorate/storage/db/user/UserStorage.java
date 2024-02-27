package ru.yandex.practicum.filmorate.storage.db.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    List<User> getAll();

    boolean isContains(Long id);

    void addFriend(Long userId, Long friendId, boolean isUsersFriends);

    void removeFriend(Long userId, Long friendId);

    List<User> getFriendsList(Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);

}