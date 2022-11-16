package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.service.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    public void initUserController() {
        userController = new UserController();
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
                .id(2)
                .email("email@yandex.ru")
                .login("Login")
                .name("Name user")
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        try {
            userController.createUser(user1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        ValidationException exception = assertThrows(ValidationException.class,
                                        () -> userController.validationUser("Создание ", user2));
        assertEquals("Создание пользователя прервано! Логин занят.",
                     exception.getMessage(), "Ошибка не обнаружена.");
    }

    @Test
    public void InvalidId() {
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
        try {
            userController.createUser(user1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        ValidationException exception = assertThrows(ValidationException.class,
                                        () -> userController.validationUser("Обновление ", user2));
        assertEquals("Обновление пользователя прервано! Неверный id.",
                     exception.getMessage(), "Ошибка не обнаружена.");
    }

    @Test
    public void emptyName() {
        User user1 = User.builder()
                .id(1)
                .email("email@yandex.ru")
                .login("Login")
                .name(null)
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        assertNull(user1.getName());
        try {
            user1 = userController.validationUser("Создание ", user1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals("Login", user1.getName(), "Имя не стало равно логину");
        User user2 = user1.toBuilder()
                .id(2)
                .name("")
                .build();
        assertEquals("", user2.getName(), "Имя не пустое");
        try {
            user2 = userController.validationUser("Создание ", user2);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals("Login", user2.getName(), "Имя не стало равно логину");
        User user3 = user1.toBuilder()
                .id(2)
                .name("  ")
                .build();
        assertEquals("  ", user3.getName(), "Имя не 2 пробела");
        try {
            user3 = userController.validationUser("Создание ", user3);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals("Login", user3.getName(), "Имя не стало равно логину");
    }
}
