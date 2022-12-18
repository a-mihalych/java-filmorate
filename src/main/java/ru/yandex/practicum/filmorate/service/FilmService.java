package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final ValidationService validationService;

    public Film createFilm(Film film) {
        filmStorage.createFilm(film);
        if ((film.getGenres() != null) && (!(film.getGenres().isEmpty()))) {
            genreStorage.saveGenres(film);
        }
        return film;
    }

    public Film updateFilm(Film film) {
        validationService.validationPositive(film.getId());
        validationService.validationNotFoundIdFilm(film.getId());
        genreStorage.deleteGenresForIdFilm(film.getId());
        filmStorage.saveFilm(film);
        if ((film.getGenres() != null) && (!(film.getGenres().isEmpty()))) {
            genreStorage.saveGenres(film);
        }
        return film;
    }

    public Collection<Film> getFilms() {
        List<Film> films = (List<Film>) filmStorage.getFilms();
        if (films != null) {
            films = genreStorage.getGenresForFilms(films);
        }
        return films;
    }

    public Film getFilm(int id) {
        id = validationService.validationPositive(id);
        Film film = filmStorage.getFilm(id);
        return genreStorage.getGenresForFilms(List.of(film)).get(0);
    }

    public Collection<Film> getFilmsLikes(int count) {
        count = validationService.validationPositive(count);
        List<Film> films = (List<Film>) filmStorage.getFilmsLikes(count);
        if (films != null) {
            films = genreStorage.getGenresForFilms(films);
        }
        return films;
    }

    public void addLike(int id, int userId) {
        id = validationService.validationPositive(id);
        userId = validationService.validationPositive(userId);
        validationService.validationNotFoundIdFilm(id);
        validationService.validationNotFoundIdUser(userId);
        likeStorage.addLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        id = validationService.validationPositive(id);
        userId = validationService.validationPositive(userId);
        validationService.validationNotFoundIdFilm(id);
        validationService.validationNotFoundIdUser(userId);
        likeStorage.addLike(id, userId);
    }
}
