package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class FilmManage implements FilmService {
    private static int id;
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addedFilm(Film film) {
        validFilm(film);
        film.setId(generatorId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            validFilm(film);
            films.put(film.getId(), film);
        } else {
            log.error("Error: invalid id - film not found");
            throw new ValidException("NOT FOUND FILM ID");
        }
        return film;
    }

    private int generatorId() {
        return id++;
    }

    private void validFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("ERROR: Field <name> cannot be empty");
            throw new ValidException("Name cannot be empty");
        }
        if (film.getDescription().length() > 200) {
            log.error("ERROR: Field <description> should not contain more than 200 characters");
            throw new ValidException("Maximum description length 200 characters");
        }
        if (film.getReleaseDate().isBefore(DATE)) {
            log.error("ERROR: Field <releaseDate> invalid");
            throw new ValidException("Release date cannot be earlier " + DATE);
        }
        if (film.getDuration() < 0) {
            log.error("ERROR: Field <duration> must not be negative");
            throw new ValidException("Duration must not be negative");
        }
    }
}
