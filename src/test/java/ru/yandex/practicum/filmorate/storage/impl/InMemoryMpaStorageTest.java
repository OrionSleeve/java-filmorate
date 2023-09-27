package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class InMemoryMpaStorageTest {
    private final MpaStorage mpaStorage;

    @Test
    public void getMpaByIdTest() {
        assertEquals("PG-13", mpaStorage.getMpaById(3).getName());
    }

    @Test
    public void getAllMpaTest() {
        String mpaList = "[Mpa(id=1, name=G), Mpa(id=2, name=PG), Mpa(id=3, name=PG-13), Mpa(id=4, name=R), Mpa(id=5, name=NC-17)]";
        assertEquals(mpaList, mpaStorage.getAllMpa().toString(), "MpaList isn't correct");
    }
}