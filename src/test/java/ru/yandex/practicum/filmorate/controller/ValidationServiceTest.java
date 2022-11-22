package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FilmorateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    public void initValidationService() {
        validationService = new ValidationService(new InMemoryUserStorage(), new InMemoryFilmStorage());
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
                .id(null)
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .build();
        validationService.getFilmStorage().saveFilm(film1);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validationService.validationFilm("Создание ", film2));
        assertEquals("Создание фильма прервано! Название уже существует.",
                exception.getMessage(), "Ошибка не обнаружена.");
    }

    @Test
    public void InvalidIdFilm() {
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
        validationService.getFilmStorage().saveFilm(film1);
        FilmorateException exception = assertThrows(FilmorateException.class,
                () -> validationService.validationFilm("Обновление ", film2));
        assertEquals("Обновление фильма прервано! Неверный id.",
                exception.getMessage(), "Ошибка не обнаружена.");
    }

    @Test
    public void loginBusy() {
        User user1 = User.builder()
                .id(1)
                .email("email@yandex.ru")
                .login("Login")
                .name("Name user")
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        User user2 = User.builder()
                .id(null)
                .email("email@yandex.ru")
                .login("Login")
                .name("Name user")
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        validationService.getUserStorage().saveUser(user1);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validationService.validationUser("Создание ", user2));
        assertEquals("Создание пользователя прервано! Логин занят.",
                exception.getMessage(), "Ошибка не обнаружена.");
    }

    @Test
    public void InvalidIdUser() {
        User user1 = User.builder()
                .id(1)
                .email("email@yandex.ru")
                .login("Login")
                .name("Name user")
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        User user2 = User.builder()
                .id(2)
                .email("email@yandex.ru")
                .login("Login")
                .name("Name user")
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        validationService.getUserStorage().saveUser(user1);
        FilmorateException exception = assertThrows(FilmorateException.class,
                () -> validationService.validationUser("Обновление ", user2));
        assertEquals("Обновление пользователя прервано! Неверный id.",
                exception.getMessage(), "Ошибка не обнаружена.");
    }

    @Test
    public void emptyName() throws ValidationException, FilmorateException {
        User user1 = User.builder()
                .id(1)
                .email("email@yandex.ru")
                .login("Login")
                .name(null)
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        assertNull(user1.getName());
        user1 = validationService.validationUser("Создание ", user1);
        assertEquals("Login", user1.getName(), "Имя не стало равно логину");
        User user2 = user1.toBuilder()
                .id(2)
                .name("")
                .build();
        assertEquals("", user2.getName(), "Имя не пустое");
        user2 = validationService.validationUser("Создание ", user2);
        assertEquals("Login", user2.getName(), "Имя не стало равно логину");
        User user3 = user1.toBuilder()
                .id(3)
                .name("  ")
                .build();
        assertEquals("  ", user3.getName(), "Имя не 2 пробела");
        user3 = validationService.validationUser("Создание ", user3);
        assertEquals("Login", user3.getName(), "Имя не стало равно логину");
    }

    @Test
    public void positiveId() throws NotFoundException {
        assertEquals(9, validationService.validationPositive(9, "id"),
                     "Проверка на положительное число не прошла");
        assertEquals(1, validationService.validationPositive(1, "id"),
                     "Проверка на положительное число не прошла");
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> validationService.validationPositive(0, "id"));
        assertEquals("Ошибка, аргумент id должен быть больше 0.",
                exception.getMessage(), "Ошибка не обнаружена.");
        exception = assertThrows(NotFoundException.class,
                () -> validationService.validationPositive(-1, "id"));
        assertEquals("Ошибка, аргумент id должен быть больше 0.",
                exception.getMessage(), "Ошибка не обнаружена.");
        exception = assertThrows(NotFoundException.class,
                () -> validationService.validationPositive(-9, "id"));
        assertEquals("Ошибка, аргумент id должен быть больше 0.",
                exception.getMessage(), "Ошибка не обнаружена.");
    }
}
