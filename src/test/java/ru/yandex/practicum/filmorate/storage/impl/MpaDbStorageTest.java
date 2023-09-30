package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    void getMpaById() {
        int id = 1;
        Mpa mpa = mpaDbStorage.getMpaById(id);
        assertEquals(mpa.getName(), "G");
    }

    @Test
    void getAllMpa() {
        List<Mpa> allMpa = mpaDbStorage.getAllMpa();
        assertEquals(allMpa.size(), 5);
    }

    @Test
    void isMpaExisted() {
        int id = 999;
        NotFoundException exception = assertThrows(NotFoundException.class, () -> mpaDbStorage.isMpaExisted(id));

        assertEquals("Mpa id: 999 does not exist", exception.getMessage());
    }
}