package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {

    Collection<Genre> getGenre();

    Genre getGenreForId(int id);

    List<Film> getGenresForFilms(List<Film> films);

    void saveGenres(Film film);

    void deleteGenresForIdFilm(int id);
}
