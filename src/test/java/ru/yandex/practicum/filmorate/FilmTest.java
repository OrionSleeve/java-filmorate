package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmManage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FilmTest extends FilmorateApplicationTests {
    private static FilmController filmController;
    private static Film film;
    private static Film film1;

    @BeforeAll
    public static void load() {
        filmController = new FilmController(new FilmManage());
    }

    @BeforeEach
    void beforeEach() {
        film = Film.builder()
                .name("The Lion King")
                .description("Animated musical drama film")
                .duration(88)
                .releaseDate(LocalDate.of(1994, 6, 19))
                .build();
        film1 = Film.builder()
                .name("The Pianist")
                .description("Epic biographical war drama film")
                .duration(150)
                .releaseDate(LocalDate.of(2002, 9, 5))
                .build();

    }

    @Test
    void createFilmTest() {
        assertEquals(film, filmController.addFilm(film));
        assertNotNull(filmController.getAllFilms());
        assertTrue(filmController.getAllFilms().contains(film));
    }

    @Test
    void updateFilmTest() {
        Film oldFilm = film;
        Film newFilm = filmController.addFilm(oldFilm);
        newFilm.setId(film.getId());
        newFilm.setReleaseDate(LocalDate.of(1992, 11, 8));
        newFilm.setName("Aladdin");
        Film update = filmController.updateFilm(newFilm);
        assertNotNull(update, "Film not updated");
        assertEquals(newFilm, update, "Film not equals");
    }

    @Test
    void descriptionTest() {
        film.setDescription("Мультфильм рассказывает историю Симбы, юного льва, который должен стать преемником своего" +
                " отца Муфасы — короля Земель Прайда; однако после того как дядя Симбы Шрам убивает Муфасу, чтобы" +
                " захватить трон, Симба чувствует себя виновным в смерти отца и бежит из Земель Прайда." +
                " Повзрослев в компании своих новых друзей — суриката Тимона и бородавочника Пумбы, — Симба получает" +
                " поддержку от своей возлюбленной Налы и от королевского шамана, мандрила Рафики и возвращается" +
                " в Земли Прайда, чтобы бросить вызов Шраму, положить конец его тирании и занять место в Круге жизни" +
                " в качестве законного короля.");
        Exception exception = assertThrows(ValidException.class, () -> filmController.addFilm(film));
        assertEquals("Maximum description length 200 characters", exception.getMessage());
    }

    @Test
    void releaseDateTest() {
        film.setReleaseDate(LocalDate.of(1799, 5, 11));
        Exception exception = assertThrows(ValidException.class, () -> filmController.addFilm(film));
        assertEquals("Release date cannot be earlier 1895-12-28", exception.getMessage());
    }

    @Test
    void durationTest() {
        film.setDuration(-15);
        assertThrows(ValidException.class, () -> filmController.addFilm(film));
    }


}
