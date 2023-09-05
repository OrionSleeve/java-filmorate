package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserManage;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UserTest extends FilmorateApplicationTests {
    private static UserController userController;
    private static User user;
    private static User user1;

    @BeforeAll
    public static void load() {
        userController = new UserController(new UserManage());
    }

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .email("testMail1234@gmail.com")
                .login("TestLogin")
                .name("Andrey")
                .birthday(LocalDate.of(1995, 11, 12))
                .build();
        user1 = User.builder()
                .email("sadTimeX1245@ingka.com")
                .login("IkeaDrill")
                .name("Botaren")
                .birthday(LocalDate.of(2018, 5, 26))
                .build();
    }

    @Test
    void createNewUser() {
        assertEquals(user, userController.addNewUser(user));
        assertNotNull(userController.getAllUsers());
        assertTrue(userController.getAllUsers().contains(user));
    }

    @Test
    void updateUserTest() {
        User oldUser = userController.addNewUser(user);
        oldUser.setId(user.getId());
        oldUser.setEmail("pokalrazbilsya@ingka.com");
        oldUser.setName("Hellvi");
        User userUpdate = userController.updateUser(oldUser);
        assertNotNull(userUpdate, "ERROR: user not updated");
        assertEquals(user, userUpdate, "ERROR: users not equals");
    }

    @Test
    void idTest() {
        User testUser = userController.addNewUser(user);
        testUser.setId(0);
        Exception exception = assertThrows(ValidException.class, () -> userController.updateUser(user));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void emailTest() {
        user.setEmail("");
        Exception exception = assertThrows(ValidException.class, () -> userController.addNewUser(user));
        String expected = "Email must not be empty";
        String message = exception.getMessage();
        assertEquals(expected, message);
    }

    @Test
    void emailNoSymbolsTest() {
        user.setEmail("Kyrangmail.com");
        Exception exception = assertThrows(ValidException.class, () -> userController.addNewUser(user));
        String expected = "Email does not contain a symbol @";
        String message = exception.getMessage();
        assertEquals(expected, message);
    }

    @Test
    void loginEmptyTest() {
        user.setLogin("");
        Exception exception = assertThrows(ValidException.class, () -> userController.addNewUser(user));
        String expected = "Login must not be empty";
        String message = exception.getMessage();
        assertEquals(expected, message);
    }

    @Test
    void loginWithSpaceTest() {
        user.setLogin("IKEA IM DONE");
        Exception exception = assertThrows(ValidException.class, () -> userController.addNewUser(user));
        String expected = "Login must not contain spaces";
        String message = exception.getMessage();
        assertEquals(expected, message);
    }

    @Test
    void birthdayTest() {
        user.setBirthday(LocalDate.of(2024, 6, 19));
        Exception exception = assertThrows(ValidException.class, () -> userController.addNewUser(user));
        String expected = "Birthday must not exceed the date of today";
        String message = exception.getMessage();
        assertEquals(expected, message);
    }
}
