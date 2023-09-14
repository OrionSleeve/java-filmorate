package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Added film: {}", film);
       return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Update film: {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("GET {} films", filmService.getFilms().size());
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("get a film by id = {} ", id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikes(@PathVariable int id, @PathVariable int userId) {
        log.info("user with id = {} {} {} ", userId, " added like for the film with id = ", id);
        filmService.addLikes(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikes(@PathVariable int id, @PathVariable int userId) {
        log.info("user with id = {} {} {} ", userId, " has been removed like for the film with id = ", id);
        filmService.removeLikes(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@Positive @RequestParam(value = "count", defaultValue = "10") int count) {
        log.debug("get popular films");
        return filmService.favoriteFilms(count);
    }
}
