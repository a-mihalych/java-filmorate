package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.FilmorateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    public void initValidationService() {
        validationService = new ValidationService(new UserDbStorage(new JdbcTemplate()),
                                                  new FilmDbStorage(new JdbcTemplate()),
                                                  new MpaDbStorage(new JdbcTemplate()),
                                                  new GenreDbStorage(new JdbcTemplate()));
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
        user1 = validationService.validationUser(user1);
        assertEquals("Login", user1.getName(), "Имя не стало равно логину");
        User user2 = user1.toBuilder()
                .id(2)
                .name("")
                .build();
        assertEquals("", user2.getName(), "Имя не пустое");
        user2 = validationService.validationUser(user2);
        assertEquals("Login", user2.getName(), "Имя не стало равно логину");
        User user3 = user1.toBuilder()
                .id(3)
                .name("  ")
                .build();
        assertEquals("  ", user3.getName(), "Имя не 2 пробела");
        user3 = validationService.validationUser(user3);
        assertEquals("Login", user3.getName(), "Имя не стало равно логину");
    }

    @Test
    public void positiveId() throws NotFoundException {
        assertEquals(9, validationService.validationPositive(9),
                     "Проверка на положительное число не прошла");
        assertEquals(1, validationService.validationPositive(1),
                     "Проверка на положительное число не прошла");
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> validationService.validationPositive(0));
        assertEquals("Ошибка, аргумент должен быть больше 0",
                exception.getMessage(), "Ошибка не обнаружена.");
        exception = assertThrows(NotFoundException.class,
                () -> validationService.validationPositive(-1));
        assertEquals("Ошибка, аргумент должен быть больше 0",
                exception.getMessage(), "Ошибка не обнаружена.");
        exception = assertThrows(NotFoundException.class,
                () -> validationService.validationPositive(-9));
        assertEquals("Ошибка, аргумент должен быть больше 0",
                exception.getMessage(), "Ошибка не обнаружена.");
    }
}
