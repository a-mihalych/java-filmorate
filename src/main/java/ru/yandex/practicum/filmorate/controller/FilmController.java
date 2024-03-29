package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("! Запрос на добавление нового фильма {}.", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("! Запрос на обновление фильма с id={} {}.", film.getId(), film);
        return filmService.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("! Запрос на получение списка фильмов.");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("! Запрос на получение фильма по id={}.", id);
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getFilmsLikes(@RequestParam(defaultValue = "10") int count) {
        log.info("! Запрос на получение списка лучших фильмов в количестве {} штук.", count);
        return filmService.getFilmsLikes(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("! Запрос на добавление лайка фильму id={}, пользователем id={}.", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("! Запрос на удаление лайка фильму id={}, пользователем id={}.", id, userId);
        filmService.deleteLike(id, userId);
    }
}
