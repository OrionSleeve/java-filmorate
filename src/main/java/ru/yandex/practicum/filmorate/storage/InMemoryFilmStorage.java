package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addedFilm(Film film) {
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
            films.put(film.getId(), film);
        } else {
            log.error("Error: invalid id - film not found");
            throw new NotFoundException("NOT FOUND FILM ID");
        }
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        return Optional.ofNullable(films.get(id));
    }

    private int generatorId() {
        return ++id;
    }
}
