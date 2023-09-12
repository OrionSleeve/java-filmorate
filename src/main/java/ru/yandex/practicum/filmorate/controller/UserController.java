package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        log.info("Added new user: {}", user);
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @PutMapping
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Update user: {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("get user by id = {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("friend with id = {} {} {}", friendId, " has been added to the user with id = ", id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("friend with id = {} {} {}", friendId, " has been removed from user with id = ", id);
        userService.removeFriendById(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public  List<User> getFriends(@PathVariable long id) {
        log.info("get friends vy the user id");
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long friendId) {
        log.info("get common friends by the user id");
        return userService.getCommonFriends(id, friendId);
    }
}
