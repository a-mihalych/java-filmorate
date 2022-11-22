package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final ValidationService validationService;

    public Film createFilm(Film film) {
        film = validationService.validationFilm("Создание ", film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        film = validationService.validationFilm("Обновление ", film);
        return filmStorage.saveFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(int id) {
        id = validationService.validationPositive(id, "id");
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            log.warn("!!! FilmService: Не найден фильм id={}", id);
            throw new NotFoundException("Не найден фильм");
        }
        return film;
    }

    public Collection<Film> getFilmsLikes(int count) {
        count = validationService.validationPositive(count, "count");
        Map<Integer, Set<Integer>> likesSort = filmStorage.getFilmsLikes().entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue().size()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError("Ошибка преобразования."); },
                        LinkedHashMap::new
                ));
        List<Film> rating = new ArrayList<>();
        for (Integer id : likesSort.keySet()) {
            rating.add(filmStorage.getFilm(id));
            count--;
            if (count == 0) {
                break;
            }
        }
        return rating;
    }

    public void addLike(int id, int userId) {
        id = validationService.validationPositive(id, "id");
        userId = validationService.validationPositive(userId, "userId");
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        id = validationService.validationPositive(id, "id");
        userId = validationService.validationPositive(userId, "userId");
        filmStorage.addLike(id, userId);
    }
}
