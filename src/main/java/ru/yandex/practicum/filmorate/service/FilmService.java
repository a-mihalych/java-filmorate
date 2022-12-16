package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final ValidationService validationService;

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validationService.validationNotFoundIdFilm(film.getId());
        return filmStorage.saveFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(int id) {
        id = validationService.validationPositive(id);
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            log.warn("!!! FilmService: Не найден фильм id={}", id);
            throw new NotFoundException("Не найден фильм");
        }
        return film;
    }

    public Collection<Film> getFilmsLikes(int count) {
        count = validationService.validationPositive(count);
        return filmStorage.getFilmsLikes(count);
    }

    public void addLike(int id, int userId) {
        id = validationService.validationPositive(id);
        userId = validationService.validationPositive(userId);
        validationService.validationNotFoundIdFilm(id);
        validationService.validationNotFoundIdUser(userId);
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        id = validationService.validationPositive(id);
        userId = validationService.validationPositive(userId);
        validationService.validationNotFoundIdFilm(id);
        validationService.validationNotFoundIdUser(userId);
        filmStorage.addLike(id, userId);
    }
}
