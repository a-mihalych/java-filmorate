package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    public final static LocalDate BEGIN_OF_CINEMA_ERA = LocalDate.of(1895, 12, 28);
    private Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("!!! Запрос на создание нового фильма {}.", film);
        film = film.toBuilder().id(nextId()).build();
        validationFilm("Создание ", film);
        films.put(film.getId(), film);
        log.info("!!! Создан фильм с id={} {}.", film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("!!! Запрос на обновление фильма с id={} {}.", film.getId(), film);
        validationFilm("Обновление ", film);
        films.put(film.getId(), film);
        log.info("!!! Обновлён фильм с id={} {}.", film.getId(), film);
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    private int nextId() {
        return id++;
    }

    void validationFilm(String method, Film film) throws ValidationException {
        String error = "";
        if (film.getReleaseDate().isBefore(BEGIN_OF_CINEMA_ERA)) {
            error += (method + "фильма прервано! Дата релиза раньше появления первого фильма.\n");
        }
        for (Film filmCollection : getFilms()) {
            if ((filmCollection.getId() != film.getId()) && (filmCollection.getName().equals(film.getName()))) {
                if (method.equals("Создание ")) {
                    error += (method + "фильма прервано! Название уже существует.\n");
                } else {
                    error += (method + "фильма прервано! Неверный id.\n");
                }
                break;
            }
        }
        if (!error.isBlank()) {
            log.warn("!!! {}", error);
            throw new ValidationException(error);
        }
    }
}
