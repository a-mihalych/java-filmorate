package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTests {

    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;

    @Test
    public void  getGenreTest() {
        assertEquals(6, genreStorage.getGenre().size(), "Количество записей в списке не совпадает");
    }

    @Test
    public void  getGenreForIdTest() {
        assertEquals("Документальный", genreStorage.getGenreForId(5).getName(), "Ответ не совпадает");
    }

    @Test
    public void getGenresForFilmsTest() {
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(3, "Мультфильм");
        Genre genre3 = new Genre(4, "Триллер");
        Film film1 = Film.builder()
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film1 = filmStorage.createFilm(film1);
        int id1 = film1.getId();
        Film film2 = Film.builder()
                .name("Кино2")
                .description("Другое кино.")
                .releaseDate(LocalDate.of(1992, 5, 18))
                .duration(88)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film2 = filmStorage.createFilm(film2);
        int id2 = film2.getId();
        genreStorage.deleteGenresForIdFilm(id1);
        genreStorage.deleteGenresForIdFilm(id2);
        List<Film> films = new ArrayList<>();
        films.add(film1);
        films.add(film2);
        assertEquals(0, films.get(0).getGenres().size(), "Количество записей в списке не совпадает");
        assertEquals(0, films.get(1).getGenres().size(), "Количество записей в списке не совпадает");
        film1.getGenres().add(genre1);
        film1.getGenres().add(genre2);
        film2.getGenres().add(genre3);
        films = genreStorage.getGenresForFilms(films);
        assertEquals(2, films.get(0).getGenres().size(), "Количество записей в списке не совпадает");
        assertEquals(1, films.get(1).getGenres().size(), "Количество записей в списке не совпадает");
        genreStorage.deleteGenresForIdFilm(id1);
        genreStorage.deleteGenresForIdFilm(id2);
        filmStorage.deleteFilm(id1);
        filmStorage.deleteFilm(id2);
    }

    @Test
    public void saveGenresTest() {
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(3, "Мультфильм");
        Film film = Film.builder()
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film = filmStorage.createFilm(film);
        genreStorage.saveGenres(film);
        int id = film.getId();
        List<Film> films = new ArrayList<>();
        films.add(film);
        films = genreStorage.getGenresForFilms(films);
        assertEquals(0, films.get(0).getGenres().size(), "Количество записей в списке не совпадает");
        film.getGenres().add(genre1);
        film.getGenres().add(genre2);
        genreStorage.saveGenres(film);
        films = genreStorage.getGenresForFilms(films);
        assertEquals(2, films.get(0).getGenres().size(), "Количество записей в списке не совпадает");
        genreStorage.deleteGenresForIdFilm(id);
        filmStorage.deleteFilm(id);
    }

    @Test
    public void deleteGenresForIdFilmTest() {
        Genre genre = new Genre(1, "Комедия");
        Film film = Film.builder()
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film = filmStorage.createFilm(film);
        film.getGenres().add(genre);
        genreStorage.saveGenres(film);
        int id = film.getId();
        List<Film> films = new ArrayList<>();
        films.add(film);
        film = genreStorage.getGenresForFilms(films).get(0);
        film.setGenres(new LinkedHashSet<>());
        List<Film> filmsNew = new ArrayList<>();
        filmsNew.add(film);
        film = genreStorage.getGenresForFilms(filmsNew).get(0);
        assertEquals(1, film.getGenres().size(), "Количество записей в списке не совпадает");
        genreStorage.deleteGenresForIdFilm(id);
        film.setGenres(new LinkedHashSet<>());
        films = new ArrayList<>();
        films.add(film);
        film = genreStorage.getGenresForFilms(films).get(0);
        assertEquals(0, film.getGenres().size(), "Количество записей в списке не совпадает");
        filmStorage.deleteFilm(id);
    }
}
