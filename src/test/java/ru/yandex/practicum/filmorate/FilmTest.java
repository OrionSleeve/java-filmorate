package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

public class FilmTest extends FilmorateApplicationTests {
    private static Validator validator;

    @BeforeAll
    public static void load() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void fieldNameIsEmptyTest() {
        Film film = Film.builder()
                .name("")
                .description("Animated musical drama film")
                .duration(88)
                .releaseDate(LocalDate.of(1994, 6, 19))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void fieldNameTest() {
        Film film = Film.builder()
                .name("The Lion King")
                .description("Animated musical drama film")
                .duration(88)
                .releaseDate(LocalDate.of(1994, 6, 19))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void noDescriptionTest() {
        Film film1 = Film.builder()
                .name("The Pianist")
                .duration(150)
                .releaseDate(LocalDate.of(2002, 9, 5))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void sizeDescriptionTest() {
        Film film = Film.builder()
                .name("The Lion King")
                .description("Animated musical drama film")
                .duration(88)
                .releaseDate(LocalDate.of(1994, 6, 19))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void overSizeDescriptionTest() {
        Film film1 = Film.builder()
                .name("The Pianist")
                .description("In September 1939, Władysław Szpilman, a Polish-Jewish pianist, is playing live on the" +
                        " radio in Warsaw when the station is bombed during Nazi Germany's invasion of Poland. Hoping" +
                        " for a quick victory, Szpilman rejoices with his family at home when he learns that Britain" +
                        " and France have declared war on Germany, but the promised aid does not come.\n" +
                        "\n" +
                        "Fighting lasts for just over a month, with both the German and Soviet armies invading Poland" +
                        " at the same time on different fronts. Warsaw becomes part of the Nazi-controlled General" +
                        " Government. Jews are soon prevented from working or owning businesses, and are also made to" +
                        " wear blue Star of David armbands.\n" +
                        "\n" +
                        "By November 1940, Szpilman and his family are forced from their home into the isolated and" +
                        " overcrowded Warsaw Ghetto, where conditions only get worse. People starve, the SS guards are" +
                        " brutal, starving children are abandoned and dead bodies are everywhere. On one occasion, the" +
                        " Szpilmans witness the SS kill an entire family in an apartment across the street during" +
                        " a round-up, including throwing a wheelchair-bound man from a window. On another" +
                        " occasion, Szpilman witnesses an SS guard torturing a young boy to death behind a wall while" +
                        " walking around the ghetto.")
                .duration(150)
                .releaseDate(LocalDate.of(2002, 9, 5))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void releaseDateInvalidTest() {
        Film film = Film.builder()
                .name("The Lion King")
                .description("Animated musical drama film")
                .duration(88)
                .releaseDate(LocalDate.of(1799, 6, 19))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void releaseDateValidTest() {
        Film film = Film.builder()
                .name("The Lion King")
                .description("Animated musical drama film")
                .duration(88)
                .releaseDate(LocalDate.of(1994, 6, 19))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void durationValidTest() {
        Film film = Film.builder()
                .name("The Lion King")
                .description("Animated musical drama film")
                .duration(88)
                .releaseDate(LocalDate.of(1994, 6, 19))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void durationInValidTest() {
        Film film = Film.builder()
                .name("The Lion King")
                .description("Animated musical drama film")
                .duration(-254)
                .releaseDate(LocalDate.of(1994, 6, 19))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}
