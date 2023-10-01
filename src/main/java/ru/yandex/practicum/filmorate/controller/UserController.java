package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        log.info("Added new user: {}", user.getId());
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Get {} users", userService.getUsers().size());
        return userService.getUsers();
    }

    @PutMapping
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Update user: {}", user.getId());
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Get user by id = {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Friend with id = {} {} {}", friendId, " has been added to the user with id = ", id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Friend with id = {} {} {}", friendId, " has been removed from user with id = ", id);
        userService.removeFriendById(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("Get friends by the user id: {}", userService.getAllFriends(id));
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int friendId) {
        List<User> commonFriends = userService.getCommonFriends(id, friendId);
        log.info("Get common friends by user id {} and friend id {}: {}", id, friendId, commonFriends);
        return commonFriends;
    }
}
