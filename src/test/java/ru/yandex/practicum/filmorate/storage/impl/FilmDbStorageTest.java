package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class FilmDbStorageTest {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    @Test
    public void testAddFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);
        film.setMpa(new Mpa(3, "PG-13"));
        film.setGenres(new LinkedHashSet<>());

        Film addedFilm = filmStorage.addedFilm(film);

        assertNotNull(addedFilm.getId());
        assertEquals(film.getName(), addedFilm.getName());
        assertEquals(film.getDescription(), addedFilm.getDescription());
        assertEquals(film.getReleaseDate(), addedFilm.getReleaseDate());
        assertEquals(film.getDuration(), addedFilm.getDuration());
        assertEquals(film.getMpa().getId(), addedFilm.getMpa().getId());
        assertEquals(film.getMpa().getName(), addedFilm.getMpa().getName());
    }

    @Test
    public void testGetFilms() {
        List<Film> films = filmStorage.getFilms();

        assertTrue(films.isEmpty());
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2008, 5, 1));
        film.setDuration(120);
        film.setMpa(new Mpa(3, "PG-13"));
        Film addedFilm = filmStorage.addedFilm(film);

        addedFilm.setName("Updated Test Film");

        Film updatedFilm = filmStorage.updateFilm(addedFilm);

        assertEquals(addedFilm.getId(), updatedFilm.getId());
        assertEquals("Updated Test Film", updatedFilm.getName());
    }

    @Test
    public void testGetFilmById() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);
        film.setMpa(new Mpa(3, "PG-13"));
        Film addedFilm = filmStorage.addedFilm(film);

        Film retrievedFilm = filmStorage.getFilmById(addedFilm.getId());

        assertEquals(addedFilm.getId(), retrievedFilm.getId());
        assertEquals(addedFilm.getName(), retrievedFilm.getName());
    }

    @Test
    public void testIsFilmExisted() {
        Film film = new Film();
        film.setName("Test Film Exited");
        film.setDescription("Test Description2");
        film.setReleaseDate(LocalDate.of(1992, 6, 8));
        film.setDuration(360);
        film.setMpa(new Mpa(3, "PG-13"));
        Film addedFilm = filmStorage.addedFilm(film);

        assertDoesNotThrow(() -> filmStorage.isFilmExisted(addedFilm.getId()));

        assertThrows(NotFoundException.class, () -> filmStorage.isFilmExisted(-1));
    }

    @Test
    public void testAddLike() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2014, 10, 6));
        film.setDuration(120);
        film.setMpa(new Mpa(3, "PG-13"));
        filmStorage.addedFilm(film);

        User user23 = new User();
        user23.setName("Barby");
        user23.setEmail("mojoDOJO@gmail.com");
        user23.setLogin("GirlMaster");
        user23.setBirthday(LocalDate.of(2001, 4, 15));
        userStorage.newUser(user23);

        likeStorage.addLike(film.getId(), user23.getId());

        assertEquals(filmStorage.getFavoriteFilms(film.getId()), filmStorage.getFavoriteFilms(1));

    }
}