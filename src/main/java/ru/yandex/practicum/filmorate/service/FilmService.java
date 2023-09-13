package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        log.info("Added film with id = {} ", film.getId());
        return filmStorage.addedFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Update film with id = {} ", film.getId());
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        log.info("GET {} films", filmStorage.getFilms().size());
        return filmStorage.getFilms();
    }

    public Film getFilmById(int filmId) {
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isPresent()) {
            log.info("get films with id = {} ", filmId);
            return film.get();
        } else {
            throw new NotFoundException("Film with id = " + filmId + " not found");
        }
    }

    public void addLikes(int filmId, int userId) {
        Film film = getFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            film.getLikes().add(userId);
            log.info("Like added to film with id = {} ", filmId);
        } else {
            log.debug("User not found");
            throw new NotFoundException("User not found");
        }
    }

    public void removeLikes(int filmId, int userId) {
        if (filmId < 0 || userId < 0) {
            throw new NotFoundException("negative value is not allowed");
        }
        Film film = getFilmById(filmId);
        film.getLikes().remove(userId);
        log.info("User with id = {} remove likes from the film id = {}", userId, filmId);
    }

    public List<Film> favoriteFilms(Integer number) {
        return filmStorage.getFilms().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Film::getCountLikes)))
                .limit(number)
                .collect(Collectors.toList());
    }
}
