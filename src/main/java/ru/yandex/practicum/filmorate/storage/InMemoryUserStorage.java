package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidException;
import ru.yandex.practicum.filmorate.model.User;


import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private long id;
    private final Map<Long, User> userMap = new HashMap<>();

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

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    private long generatorUserId() {
        return ++id;
    }

    public void validUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
