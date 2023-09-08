package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidException;
import ru.yandex.practicum.filmorate.model.User;


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
        return ++id;
    }

    private void validUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
