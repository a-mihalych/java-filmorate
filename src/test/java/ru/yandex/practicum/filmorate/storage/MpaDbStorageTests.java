package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTests {

    private final MpaDbStorage mpaStorage;

    @Test
    public void  getMpaTest() {
        assertEquals(5, mpaStorage.getMpa().size(), "Количество записей в списке не совпадает");
    }

    @Test
    public void  getMpaForIdTest() {
        assertEquals("PG", mpaStorage.getMpaForId(2).getName(), "Ответ не совпадает");
    }
}
