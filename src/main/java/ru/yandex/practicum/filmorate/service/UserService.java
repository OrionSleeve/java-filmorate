package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public User createUser(User user) {
        validUser(user);
        log.info("New user with id = {} created ", user.getId());
        return userStorage.newUser(user);
    }

    public User updateUser(User user) {
        validUser(user);
        userStorage.isUserExisted(user.getId());
        log.info("User with id = {} {} ", user.getId(), "has been updated");
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        log.info("get {} users ", userStorage.getUsers().size());
        return userStorage.getUsers();
    }

    public User getUserById(int id) {
        userStorage.isUserExisted(id);
        User user = userStorage.getUserById(id);
        log.info("get user with id = {} ", id);
        return user;
    }

    public void addFriend(int id, int friendId) {
        userStorage.isUserExisted(id);
        userStorage.isUserExisted(friendId);
        friendStorage.addAsFriend(id, friendId);
        log.info("The friend with id = {} {} {}", friendId, " has been added to the user with id = ", id);
        log.info("The friend with id = {} {} {}", id, " has been added to the user with id = ", friendId);
    }

    public void removeFriendById(int id, int friendId) {
        friendStorage.deleteFriend(id, friendId);
        log.info("Friend with id = {}{}{}", friendId, " has been removed to the user with id = ", id);
    }

    public List<User> getAllFriends(int id) {
        List<User> friends = friendStorage.getAllFriends(id);
        log.info("Get all friends for user with id: {}", id);
        return friends;
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        log.info("Get common friends for users with id: {} and {}", userId, friendId);
        return friendStorage.getCommonFriends(userId, friendId);
    }

    public void validUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
