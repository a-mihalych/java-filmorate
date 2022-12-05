package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.FilmorateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class ValidationService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public User validationUser(String method, User user) {
        String error = "";
        for (User userCollection : userStorage.getUsers()) {
            if (!userCollection.getId().equals(user.getId()) && userCollection.getLogin().equals(user.getLogin())) {
                if (method.equals("Создание ")) {
                    error += (method + "пользователя прервано! Логин занят.");
                } else {
                    error += (method + "пользователя прервано! Неверный id.");
                    log.warn("!!! ValidationService: {}", error);
                    throw new FilmorateException(error);
                }
                break;
            }
        }
        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
            log.info("!!! ValidationService: Не задано имя для отображения, для отображения будет использован логин");
        }
        if (!error.isBlank()) {
            log.warn("!!! ValidationService: {}", error);
            throw new ValidationException(error);
        }
        return user;
    }

    public Film validationFilm(String method, Film film) {
        String error = "";
        for (Film filmCollection : filmStorage.getFilms()) {
            if (!filmCollection.getId().equals(film.getId()) && filmCollection.getName().equals(film.getName())) {
                if (method.equals("Создание ")) {
                    error += (method + "фильма прервано! Название уже существует.");
                } else {
                    error += (method + "фильма прервано! Неверный id.");
                    log.warn("!!! ValidationService: {}", error);
                    throw new FilmorateException(error);
                }
                break;
            }
        }
        if (!error.isBlank()) {
            log.warn("!!! ValidationService: {}", error);
            throw new ValidationException(error);
        }
        return film;
    }

    public int validationPositive(int id, String name) {
        if (id < 1) {
            String error = "Ошибка, аргумент " + name + " должен быть больше 0.";
            log.warn("!!! ValidationService: {}", error);
            throw new NotFoundException(error);
        }
        return id;
    }
}
