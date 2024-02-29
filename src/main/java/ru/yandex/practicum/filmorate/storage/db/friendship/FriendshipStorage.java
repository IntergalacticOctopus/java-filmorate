package ru.yandex.practicum.filmorate.storage.db.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {

    Boolean addFriend(Long userId, Long friendId, boolean isFriend);

    void removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long id, Long otherId);
}
