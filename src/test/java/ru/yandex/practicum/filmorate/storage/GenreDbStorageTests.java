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
public class GenreDbStorageTests {

    private final GenreDbStorage genreStorage;

    @Test
    public void  getGenreTest() {
        assertEquals(6, genreStorage.getGenre().size(), "Количество записей в списке не совпадает");
    }

    @Test
    public void  getGenreForIdTest() {
        assertEquals("Документальный", genreStorage.getGenreForId(5).getName(), "Ответ не совпадает");
    }
}
