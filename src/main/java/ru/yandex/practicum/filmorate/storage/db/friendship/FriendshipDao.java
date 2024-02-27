package ru.yandex.practicum.filmorate.storage.db.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipDao {

    void addFriend(Long userId, Long friendId, boolean isFriend);

    void removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    Friendship getFriend(Long userId, Long friendId);

    boolean isFriend(Long userId, Long friendId);

    List<User> getCommonFriends(Long id, Long otherId);
}
