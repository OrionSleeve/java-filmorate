package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addedFilm(Film film);

    List<Film> getFilms();

    Film updateFilm(Film film);

    Optional<Film> getFilmById(Integer id);
}
