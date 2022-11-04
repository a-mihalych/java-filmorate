package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("!!! Запрос на создание нового пользователя {}.", user);
        user = user.toBuilder().id(nextId()).build();
        user = validationUser("Создание ", user);
        users.put(user.getId(), user);
        log.info("!!! Создан пользователь с id={} {}.", user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("!!! Запрос на обновление пользователя с id={} {}.", user.getId(), user);
        user = validationUser("Обновление ", user);
        users.put(user.getId(), user);
        log.info("!!! Обновлён пользователь с id={} {}.", user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    private int nextId() {
        return id++;
    }

    User validationUser(String method, User user) throws ValidationException {
        String error = "";
        if (user.getLogin().contains(" ")) {
            error += (method + "пользователя прервано! Логин не может содержать пробелы.\n");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            error += (method + "пользователя прервано! Дата рождения не может быть в будущем.\n");
        }
        for (User userCollection : getUsers()) {
            if ((userCollection.getId() != user.getId()) && (userCollection.getLogin().equals(user.getLogin()))) {
                if (method.equals("Создание ")) {
                    error += (method + "пользователя прервано! Логин занят.\n");
                } else {
                    error += (method + "пользователя прервано! Неверный id.\n");
                }
                break;
            }
        }
        if ((user.getName() == null) || (user.getName().isBlank())) {
            user = user.toBuilder().name(user.getLogin()).build();
            log.info("!!! Не задано имя для отображения, для отображения будет использован логин");
        }
        if (!error.isBlank()) {
            log.warn("!!! {}", error);
            throw new ValidationException(error);
        }
        return user;
    }
}
