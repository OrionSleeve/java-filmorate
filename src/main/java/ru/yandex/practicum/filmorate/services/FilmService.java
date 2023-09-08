package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addedFilm(Film film);


    List<Film> getFilms();


    Film updateFilm(Film film);
}
