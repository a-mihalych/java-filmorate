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
public class LikeDbStorageTests {

    private final LikeDbStorage likeStorage;
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

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
        likeStorage.addLike(id1, id1User);
        assertEquals(id1, ((ArrayList<Film>) filmStorage.getFilmsLikes(10)).get(0).getId(),
                     "Индефикатор не совпадает");
        likeStorage.addLike(id2, id1User);
        likeStorage.addLike(id2, id2User);
        assertEquals(id2, ((ArrayList<Film>) filmStorage.getFilmsLikes(10)).get(0).getId(),
                     "Индефикатор не совпадает");
        likeStorage.deleteLike(id1, id1User);
        likeStorage.deleteLike(id2, id1User);
        likeStorage.deleteLike(id2, id2User);
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
        likeStorage.addLike(id1, id1User);
        likeStorage.addLike(id1, id2User);
        likeStorage.addLike(id2, id1User);
        assertEquals(id1, ((ArrayList<Film>) filmStorage.getFilmsLikes(10)).get(0).getId(),
                     "Индефикатор не совпадает");
        likeStorage.deleteLike(id1, id1User);
        likeStorage.deleteLike(id1, id2User);
        assertEquals(id2, ((ArrayList<Film>) filmStorage.getFilmsLikes(10)).get(0).getId(),
                     "Индефикатор не совпадает");
        likeStorage.deleteLike(id2, id1User);
        filmStorage.deleteFilm(id1);
        filmStorage.deleteFilm(id2);
        userStorage.deleteUser(id1User);
        userStorage.deleteUser(id2User);
    }
}
