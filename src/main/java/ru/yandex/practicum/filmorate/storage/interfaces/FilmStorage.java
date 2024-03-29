package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getFilms();

    Film getFilm(int id);

    Film createFilm(Film film);

    Film saveFilm(Film film);

    Integer deleteFilm(int id);

    Collection<Film> getFilmsLikes(int count);
}
