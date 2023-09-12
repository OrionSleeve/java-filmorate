package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

public class UserTest extends FilmorateApplicationTests {
    private static User user;
    private static User user1;
    private static Validator validator;

    @BeforeAll
    public static void load() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void inValidEmailTest() {
        User user = User.builder()
                .email("testMail 1234@gmail.com")
                .login("TestLogin")
                .name("Andrey")
                .birthday(LocalDate.of(1995, 11, 12))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validEmailTest() {
        user = User.builder()
                .email("testMail1234@gmail.com")
                .login("TestLogin")
                .name("Andrey")
                .birthday(LocalDate.of(1995, 11, 12))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void validLoginTest() {
        user = User.builder()
                .email("testMail1234@gmail.com")
                .login("TestLogin")
                .name("Andrey")
                .birthday(LocalDate.of(1995, 11, 12))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void inValidLoginTest() {
        user = User.builder()
                .email("testMail1234@gmail.com")
                .login("Test Login")
                .name("Andrey")
                .birthday(LocalDate.of(1995, 11, 12))
                .build();
        user1 = User.builder()
                .email("sadTimeX1245@ingka.com")
                .login("")
                .name("Botaren")
                .birthday(LocalDate.of(2018, 5, 26))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Set<ConstraintViolation<User>> violations1 = validator.validate(user1);
        assertAll(() -> assertFalse(violations.isEmpty()), () -> assertFalse(violations1.isEmpty()));
    }

    @Test
    public void validBirthdayTest() {
        user1 = User.builder()
                .email("sadTimeX1245@ingka.com")
                .login("IKEAGONE")
                .name("Botaren")
                .birthday(LocalDate.of(2018, 5, 26))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void inValidBirthdayTest() {
        user = User.builder()
                .email("testMail1234@gmail.com")
                .login("TestLogin")
                .name("Andrey")
                .birthday(LocalDate.of(2024, 11, 12))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
