package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    public void initFilmController() {
        filmController = new FilmController();
    }

    @Test
    public void releaseDate() {
        Film film = Film.builder()
                    .id(1)
                    .name("Кино")
                    .description("Интересное кино, наверное.")
                    .releaseDate(LocalDate.of(1888, 8, 8))
                    .duration(99)
                    .build();
        ValidationException exception = assertThrows(ValidationException.class,
                                        () -> filmController.validationFilm("Создание ", film));
        assertEquals("Создание фильма прервано! Дата релиза раньше появления первого фильма.\n",
                     exception.getMessage(), "Ошибка не обнаружена.");
    }

    @Test
    public void filmAlreadyExists() {
        Film film1 = Film.builder()
                .id(1)
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .build();
        try {
            filmController.createFilm(film1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        ValidationException exception = assertThrows(ValidationException.class,
                                        () -> filmController.validationFilm("Создание ", film2));
        assertEquals("Создание фильма прервано! Название уже существует.\n",
                     exception.getMessage(), "Ошибка не обнаружена.");
    }

    @Test
    public void InvalidId() {
        Film film1 = Film.builder()
                .id(1)
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .build();
        try {
            filmController.createFilm(film1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.validationFilm("Обновление ", film2));
        assertEquals("Обновление фильма прервано! Неверный id.\n",
                     exception.getMessage(), "Ошибка не обнаружена.");
    }
}
