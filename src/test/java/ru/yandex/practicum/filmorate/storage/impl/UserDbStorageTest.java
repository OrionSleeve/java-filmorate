package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserStorage userStorage;

    @Test
    void newUser() {
        User user = new User();
        user.setName("Mojo");
        user.setEmail("mojo@gmail.com");
        user.setLogin("Glam");
        user.setBirthday(LocalDate.of(1998, 2, 25));
        userStorage.newUser(user);

        assertNotNull(user.getId());
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setName("Mojo");
        user.setEmail("mojo@gmail.com");
        user.setLogin("Glam");
        user.setBirthday(LocalDate.of(1998, 2, 25));
        userStorage.newUser(user);
        user.setName("Casa");
        userStorage.updateUser(user);

        assertEquals("Casa", user.getName());
    }

    @Test
    void getUsers() {
        User user = new User();
        user.setName("Mojo");
        user.setEmail("mojo@gmail.com");
        user.setLogin("Glam");
        user.setBirthday(LocalDate.of(1998, 2, 25));
        userStorage.newUser(user);

        User user23 = new User();
        user23.setName("Barby");
        user23.setEmail("mojoDOJO@gmail.com");
        user23.setLogin("GirlMaster");
        user23.setBirthday(LocalDate.of(2001, 4, 15));
        userStorage.newUser(user23);

        assertEquals(userStorage.getUsers().size(), 2);
    }

    @Test
    void getUserById() {
        User user23 = new User();
        user23.setName("Barby");
        user23.setEmail("mojoDOJO@gmail.com");
        user23.setLogin("GirlMaster");
        user23.setBirthday(LocalDate.of(2001, 4, 15));
        userStorage.newUser(user23);

        User user = new User();
        user.setName("Mojo");
        user.setEmail("mojo@gmail.com");
        user.setLogin("Glam");
        user.setBirthday(LocalDate.of(1998, 2, 25));
        userStorage.newUser(user);

        assertEquals(user23.getId(), 1);
        assertEquals(user.getId(), 2);
    }

    @Test
    void isUserExisted() {
        User user23 = new User();
        user23.setName("Barby");
        user23.setEmail("mojoDOJO@gmail.com");
        user23.setLogin("GirlMaster");
        user23.setBirthday(LocalDate.of(2001, 4, 15));
        userStorage.newUser(user23);

        User user = new User();
        user.setName("Mojo");
        user.setEmail("mojo@gmail.com");
        user.setLogin("Glam");
        user.setBirthday(LocalDate.of(1998, 2, 25));
        userStorage.newUser(user);

        assertThrows(NotFoundException.class, () -> userStorage.isUserExisted(-1));
    }
}