package ru.yandex.practicum.filmorate.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

@Slf4j
@Getter
@Setter
@Service
public class ValidationService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public ValidationService(UserStorage userStorage, FilmStorage filmStorage,
                             MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public User validationUser(User user) {
        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
            log.info("!! ValidationService: Не задано имя для отображения, для отображения будет использован логин");
        }
        return user;
    }

    public int validationNotFoundIdFilm(int id) {
        if (filmStorage.getFilm(id) == null) {
            throw new NotFoundException(String.format("Фильм с id = %s в базе отсутствует", id));
        }
        return id;
    }

    public int validationNotFoundIdUser(int id) {
        if (userStorage.getUser(id) == null) {
            throw new NotFoundException(String.format("Пользователь с id = %s в базе отсутствует", id));
        }
        return id;
    }

    public int validationNotFoundIdMpa(int id) {
        if (mpaStorage.getMpaForId(id) == null) {
            throw new NotFoundException(String.format("Рейтинг с id = %s в базе отсутствует", id));
        }
        return id;
    }

    public int validationNotFoundIdGenre(int id) {
        if (genreStorage.getGenreForId(id) == null) {
            throw new NotFoundException(String.format("Жанр с id = %s в базе отсутствует", id));
        }
        return id;
    }

    public int validationPositive(int arg) {
        if (arg < 1) {
            throw new NotFoundException("Ошибка, аргумент должен быть больше 0");
        }
        return arg;
    }
}
