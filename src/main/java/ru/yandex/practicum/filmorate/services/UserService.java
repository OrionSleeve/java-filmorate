package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User newUser(User user);


    User updateUser(User user);


    List<User> getUsers();
}
