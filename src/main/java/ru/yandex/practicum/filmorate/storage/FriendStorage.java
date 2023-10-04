package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addAsFriend(int id, int idFriend);

    void deleteFriend(int id, int idFriend);

    List<User> getCommonFriends(int id, int otherId);

    List<User> getAllFriends(int id);
}
