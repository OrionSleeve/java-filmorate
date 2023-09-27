package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class InMemoryUserStorageTest {
    private final UserStorage userStorage;
    private User user;
    private User user1;
    private Validator validator;
    private Set<Long> friends = new HashSet<>();
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        user1 = new User("qwertyu2@gmail.com", "NickkyName", "dol",
                LocalDate.of(2006, 2, 2));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void afterEach() {
        String sqlQuery =
                "delete from rating_mpa;\n" +
                        "delete from friendship;\n" +
                        "delete from films;\n" +
                        "delete from users;\n" +
                        "delete from likes;\n" +
                        "delete from genres;\n" +
                        "delete from film_genre;";
        jdbcTemplate.update(sqlQuery);
    }

    @Test
    void createUser() {
        user = new User("dolore", "Nick Name", "qwertyui@gmail.com",
                LocalDate.of(1996, 12, 26));
        userStorage.newUser(user);
        assertEquals("Nick Name", user.getLogin(), "User's login isn't correct");
        assertEquals("qwertyui@gmail.com", user.getName(), "User's name isn't correct");
        assertEquals("dolore", user.getEmail(), "User's email isn't correct");
        assertEquals(LocalDate.of(1996, 12, 26), user.getBirthday(), "User's name isn't correct");
    }

    @Test
    void userWithNullEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            user1.setEmail(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: must not be blank", exception.getMessage());
    }

    @Test
    void userInvalidEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            user1.setEmail("mail.ru");
            Set<ConstraintViolation<User>> violations = validator.validate(user1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: must be a well-formed email address", exception.getMessage());
    }

    @Test
    void userNullBirthday() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            user1.setBirthday(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("birthday: must not be null", exception.getMessage());
    }

    @Test
    void userInvalidBirthday() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            user1.setBirthday(LocalDate.of(2895, 12, 28));
            Set<ConstraintViolation<User>> violations = validator.validate(user1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("birthday: must be a date in the past or in the present", exception.getMessage());
    }

    @Test
    void updateUser() {
        userStorage.newUser(user1);
        user1.setName("Anna");
        user1.setEmail("anna456@mail.ru");
        user1.setBirthday(LocalDate.of(1995, 12, 28));
        user1.setLogin("ant");
        userStorage.updateUser(user1);
        List<User> listUser = userStorage.getUsers();
        assertEquals(1, listUser.size(), "Number of users isn't correct");
        assertEquals(user1.getEmail(), listUser.get(0).getEmail(), "User's email isn't correct");
        assertEquals(user1.getName(), listUser.get(0).getName(), "User's name isn't correct");
        assertEquals(user1.getLogin(), listUser.get(0).getLogin(), "User's login isn't correct");
        assertEquals(user1.getBirthday(), listUser.get(0).getBirthday(), "User's name isn't correct");
    }

    @Test
    void updateUserWithNullEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userStorage.newUser(user1);
            user1.setEmail(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: must not be blank", exception.getMessage());
    }

    @Test
    void updateWithInvalidEmail() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userStorage.newUser(user1);
            user1.setEmail("@mail.ru");
            Set<ConstraintViolation<User>> violations = validator.validate(user1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("email: must be a well-formed email address", exception.getMessage());
    }

    @Test
    void updateInvalidLogin() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userStorage.newUser(user1);
            user1.setLogin(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("login: must not be blank",
                exception.getMessage());
    }

    @Test
    void updateEmptyNameTest() {
        userStorage.newUser(user1);
        user1.setName("");

        assertEquals("NickkyName", user1.getLogin(), "User's name isn't correct");
    }

    @Test
    void updateInvalidBirthdayTest() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userStorage.newUser(user1);
            user1.setBirthday(LocalDate.of(2895, 12, 28));
            Set<ConstraintViolation<User>> violations = validator.validate(user1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("birthday: must be a date in the past or in the present", exception.getMessage());
    }
}