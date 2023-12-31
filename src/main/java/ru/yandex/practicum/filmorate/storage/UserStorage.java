package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User newUser(User user);


    User updateUser(User user);


    List<User> getUsers();

    User getUserById(int id);

    void isUserExisted(int id);
}
