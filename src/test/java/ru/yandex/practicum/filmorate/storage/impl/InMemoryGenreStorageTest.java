package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class InMemoryGenreStorageTest {
    private final GenreStorage genreStorage;

    @Test
    public void getGenreByIdTest() {
        assertEquals("Драма", genreStorage.getGenreById(2).getName());
    }

    @Test
    public void getAllGenresTest() {
        String genreList = "[Genre(id=1, name=Комедия), Genre(id=2, name=Драма), Genre(id=3, name=Мультфильм), " +
                "Genre(id=4, name=Триллер), Genre(id=5, name=Документальный), Genre(id=6, name=Боевик)]";
        assertEquals(genreList, genreStorage.getAllGenres().toString(), "MpaList isn't correct");
    }
}