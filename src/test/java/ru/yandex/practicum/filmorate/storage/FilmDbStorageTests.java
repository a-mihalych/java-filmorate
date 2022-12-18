package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final LikeDbStorage likeStorage;

    @Test
    public void  getFilmsTest() {
        Film film = Film.builder()
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film = filmStorage.createFilm(film);
        int id = film.getId();
        assertEquals(1, filmStorage.getFilms().size(), "Количество фильмов в списке не совпадает");
        filmStorage.deleteFilm(id);
    }

    @Test
    public void getFilmTest() {
        Film film = Film.builder()
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film = filmStorage.createFilm(film);
        int id = film.getId();
        assertEquals("Кино", filmStorage.getFilm(id).getName(), "Название не совпадает");
        filmStorage.deleteFilm(id);
    }

    @Test
    public void createFilmTest() {
        assertEquals(0, filmStorage.getFilms().size(), "Количество фильмов в списке не совпадает");
        Film film = Film.builder()
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film = filmStorage.createFilm(film);
        int id = film.getId();
        assertEquals(1, filmStorage.getFilms().size(), "Количество фильмов в списке не совпадает");
        filmStorage.deleteFilm(id);
    }

    @Test
    public void saveFilmTest() {
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
                .id(id1)
                .name("Кино2")
                .description("Другое кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film2 = filmStorage.saveFilm(film2);
        int id2 = film2.getId();
        assertEquals(id2, filmStorage.getFilm(id1).getId(), "Индефикатор не совпадает");
        assertEquals("Другое кино, наверное.", filmStorage.getFilm(id1).getDescription(),
                     "Описание не совпадает");
        filmStorage.deleteFilm(id2);
    }

    @Test
    public void deleteFilmTest() {
        Film film = Film.builder()
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film = filmStorage.createFilm(film);
        int id = film.getId();
        assertEquals(1, filmStorage.getFilms().size(), "Количество фильмов в списке не совпадает");
        filmStorage.deleteFilm(id);
        assertEquals(0, filmStorage.getFilms().size(), "Количество фильмов в списке не совпадает");
    }

    @Test
    public void getFilmsLikesTest() {
        User user = User.builder()
                .email("email@yandex.ru")
                .login("Login")
                .name("Name")
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        user = userStorage.createUser(user);
        int idUser = user.getId();
        Film film = Film.builder()
                .name("Кино")
                .description("Интересное кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film = filmStorage.createFilm(film);
        int id = film.getId();
        likeStorage.addLike(id, idUser);
        assertEquals(1, filmStorage.getFilmsLikes(10).size(),
                     "Количество фильмов в списке не совпадает");
        likeStorage.deleteLike(id, idUser);
        filmStorage.deleteFilm(id);
        userStorage.deleteUser(idUser);
    }
}
