package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validUser(user);
        log.info("New user with id = {} created ", user.getId());
        return userStorage.newUser(user);
    }

    public User updateUser(User user) {
        validUser(user);
        log.info("User with id = {} {} ", user.getId(), "has been updated");
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        log.info("get {} users ", userStorage.getUsers().size());
        return userStorage.getUsers();
    }

    public User getUserById(long id) {
        log.info("get user with id = {} ", id);
        Optional<User> user = userStorage.getUserById(id);
        if (user.isPresent()) {
            log.info("get user with id = {} ", id);
            return user.get();
        } else {
            throw new NotFoundException("user with id = " + id + "not found");
        }
    }

    public void addFriend(Long id, Long friendId) {
        User user = getUserById(id);
        if (user.getFriends().contains(friendId)) {
            log.info("friend with id = {} {} {}", friendId, " has been friend of the user with id = {}", id);
            throw new NotFoundException("User " + id + " and the user " + friendId + " have been friends yet");
        }
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        log.info("The friend with id = {} {} {}", friendId, " has been added to the user with id = ", id);
        log.info("The friend with id = {} {} {}", id, " has been added to the user with id = ", friendId);
    }

    public void removeFriendById(long id, Long friendId) {
        User user = getUserById(id);
        log.info("friend with id = {}{}{}", friendId, " has been removed to the user with id = ", id);
        user.getFriends().remove(friendId);
    }

    public List<User> getAllFriends(long id) {
        log.info("get all friends");
        return getUserById(id).getFriends()
                .stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        log.info("get common friends");
        List<User> friends = getAllFriends(userId);
        friends.retainAll(getAllFriends(friendId));
        return friends;
    }

    public void validUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
