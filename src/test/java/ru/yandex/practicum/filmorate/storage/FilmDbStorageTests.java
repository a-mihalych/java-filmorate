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
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

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
    public void addLikeTest() {
        User user1 = User.builder()
                .email("email1@yandex.ru")
                .login("Login1")
                .name("Name1")
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        user1 = userStorage.createUser(user1);
        int id1User = user1.getId();
        User user2 = User.builder()
                .email("email2@yandex.ru")
                .login("Login2")
                .name("Name2")
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        user2 = userStorage.createUser(user2);
        int id2User = user2.getId();
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
                .description("Другое кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film2 = filmStorage.createFilm(film2);
        int id2 = film2.getId();
        filmStorage.addLike(id1, id1User);
        assertEquals(id1, ((ArrayList<Film>) filmStorage.getFilmsLikes(10)).get(0).getId(),
                     "Индефикатор не совпадает");
        filmStorage.addLike(id2, id1User);
        filmStorage.addLike(id2, id2User);
        assertEquals(id2, ((ArrayList<Film>) filmStorage.getFilmsLikes(10)).get(0).getId(),
                     "Индефикатор не совпадает");
        filmStorage.deleteLike(id1, id1User);
        filmStorage.deleteLike(id2, id1User);
        filmStorage.deleteLike(id2, id2User);
        filmStorage.deleteFilm(id1);
        filmStorage.deleteFilm(id2);
        userStorage.deleteUser(id1User);
        userStorage.deleteUser(id2User);
    }

    @Test
    public void deleteLikeTest() {
        User user1 = User.builder()
                .email("email1@yandex.ru")
                .login("Login1")
                .name("Name1")
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        user1 = userStorage.createUser(user1);
        int id1User = user1.getId();
        User user2 = User.builder()
                .email("email2@yandex.ru")
                .login("Login2")
                .name("Name2")
                .birthday(LocalDate.of(2000, 2, 22))
                .build();
        user2 = userStorage.createUser(user2);
        int id2User = user2.getId();
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
                .description("Другое кино, наверное.")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(99)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<Genre>())
                .build();
        film2 = filmStorage.createFilm(film2);
        int id2 = film2.getId();
        filmStorage.addLike(id1, id1User);
        filmStorage.addLike(id1, id2User);
        filmStorage.addLike(id2, id1User);
        assertEquals(id1, ((ArrayList<Film>) filmStorage.getFilmsLikes(10)).get(0).getId(),
                     "Индефикатор не совпадает");
        filmStorage.deleteLike(id1, id1User);
        filmStorage.deleteLike(id1, id2User);
        assertEquals(id2, ((ArrayList<Film>) filmStorage.getFilmsLikes(10)).get(0).getId(),
                     "Индефикатор не совпадает");
        filmStorage.deleteLike(id2, id1User);
        filmStorage.deleteFilm(id1);
        filmStorage.deleteFilm(id2);
        userStorage.deleteUser(id1User);
        userStorage.deleteUser(id2User);
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
        filmStorage.addLike(id, idUser);
        assertEquals(1, filmStorage.getFilmsLikes(10).size(),
                     "Количество фильмов в списке не совпадает");
        filmStorage.deleteLike(id, idUser);
        filmStorage.deleteFilm(id);
        userStorage.deleteUser(idUser);
    }
}
