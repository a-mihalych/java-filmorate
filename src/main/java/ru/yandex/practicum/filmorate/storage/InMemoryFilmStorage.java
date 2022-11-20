package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();
    private Map<Integer, Set<Integer>> likes = new HashMap<>();
    private int id = 1;

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film getFilm(int id) {
        return films.get(id);
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(nextId());
        likes.put(film.getId(), new HashSet<>());
        return saveFilm(film);
    }

    @Override
    public Film saveFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(int id) {
        likes.remove(id);
        return films.remove(id);
    }

    @Override
    public void addLike(int id, int userId) {
        likes.get(id).add(userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        likes.get(id).remove(userId);
    }

    @Override
    public Map<Integer, Set<Integer>> getFilmsLikes() {
        return likes;
    }

    private int nextId() {
        return id++;
    }
}
