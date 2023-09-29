package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FimDbStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Film film;
    private Film film1;
    private Validator validator;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        film1 = new Film("TestOne", "DescriptionTest One", LocalDate.of(1995, 12, 5),
                2500, new Mpa(1, "TestMPA"), new LinkedHashSet<>());

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    void afterEach() {
        String sqlQuery =
                "delete from friendship;\n" +
                        "delete from films;\n" +
                        "delete from users;\n" +
                        "delete from likes;\n" +
                        "delete from film_genre;";
        jdbcTemplate.update(sqlQuery);
    }

    @Test
    void createFilmTest() {
        film = new Film("Star Wars", "Star Wars is an American epic space opera created by George Lucas",
                LocalDate.of(1977, 5, 25), 248345,
                new Mpa(3, "PG-13"), new LinkedHashSet<>());
        filmStorage.addedFilm(film);
        assertEquals("Star Wars", film.getName(), "Film's name isn't correct");
        assertEquals("Star Wars is an American epic space opera created by George Lucas",
                film.getDescription(), "Film's description isn't correct");
        assertEquals(248345, film.getDuration(), "Film's duration  isn't correct");
        assertEquals(LocalDate.of(1977, 5, 25), film.getReleaseDate(),
                "Film's release date  isn't correct");
    }

    @Test
    void shouldNotCreateFilmWithEmptyName() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            film1.setName("");
            Set<ConstraintViolation<Film>> violations = validator.validate(film1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("name: must not be blank", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithEmptyDescription() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            film1.setDescription("");
            Set<ConstraintViolation<Film>> violations = validator.validate(film1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("description: must not be blank", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithDescriptionMoreThan200symbols() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            String description = "Chicken Run is a 2000 stop-motion animation adventure comedy film produced by Paths " +
                    "and Aardman Animations in partnership with DreamWorks Animation.  The film stars the voices of " +
                    "Julia Sawalha, Mel Gibson.";
            film1.setDescription(description);
            Set<ConstraintViolation<Film>> violations = validator.validate(film1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("description: size must be between 0 and 200",
                exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithNullReleaseDate() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            film1.setReleaseDate(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("releaseDate: Release date cannot be earlier", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmNullDuration() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            film1.setDuration(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("duration: must not be null", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmNegativeDuration() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            film1.setDuration(-1);
            Set<ConstraintViolation<Film>> violations = validator.validate(film1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("duration: must be greater than 0", exception.getMessage());
    }

    @Test
    void shouldUpdateFilm() {
        filmStorage.addedFilm(film1);
        film1.setName("New name");
        film1.setDescription("New description");
        film1.setReleaseDate((LocalDate.of(2005, 12, 28)));
        film1.setDuration(24356);
        assertEquals("New name", film1.getName(), "Film's name isn't correct");
        assertEquals("New description", film1.getDescription(), "Film's description isn't correct");
        assertEquals(24356, film1.getDuration(), "Film's duration  isn't correct");
        assertEquals(LocalDate.of(2005, 12, 28), film1.getReleaseDate(), "Film's release date  isn't correct");
    }

    @Test
    void shouldNotUpdateFilmWithEmptyName() {
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmStorage.addedFilm(film1);
            film1.setName("");
            Set<ConstraintViolation<Film>> violations = validator.validate(film1);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
        Assertions.assertEquals("name: must not be blank", exception.getMessage());
    }
}
