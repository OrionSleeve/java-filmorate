package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserManage implements UserService {
    private static int id;
    private final Map<Integer, User> userMap = new HashMap<>();

    @Override
    public User newUser(User user) {
        validUser(user);
        user.setId(generatorUserId());
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userMap.containsKey(user.getId())) {
            validUser(user);
            userMap.put(user.getId(), user);
            return user;
        } else {
            log.error("ERROR: invalid id - user not found");
            throw new ValidException("User not found");
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }

    private int generatorUserId() {
        return id++;
    }

    private void validUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.error("ERROR: Field <email> must not be empty");
            throw new ValidException("Email must not be empty");
        }
        if (!user.getEmail().contains("@")) {
            log.error("ERROR: Field <email> does not contain a symbol @ ");
            throw new ValidException("Email does not contain a symbol @");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            log.error("ERROR: Field <login> must not be empty");
            throw new ValidException("Login must not be empty");
        }
        if (user.getLogin().contains(" ")) {
            log.error("ERROR: Field <login> must not contain spaces");
            throw new ValidException("Login must not contain spaces");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("ERROR: Field <birthday> must not exceed the date of today");
            throw new ValidException("Birthday must not exceed the date of today");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
