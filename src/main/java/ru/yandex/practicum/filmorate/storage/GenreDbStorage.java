package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getGenre() {
        String sql = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sql, this::makeGenre);
    }

    @Override
    public Genre getGenreForId(int id) {
        String sql = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sql, this::makeGenre, id);
        if (genres.size() != 1) {
            throw new NotFoundException("Не найден жанр");
        }
        return genres.get(0);
    }

    @Override
    public List<Film> getGenresForFilms(List<Film> films) {
        List<Integer> ids = films.stream()
                                 .map(Film::getId)
                                 .collect(Collectors.toList());
        Map<Integer, Film> idsFilms = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film));
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("ids", ids);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql = "SELECT * " +
                     "FROM GENRES AS GS " +
                     "INNER JOIN GENRE G ON GS.GENRE_ID = G.GENRE_ID " +
                     "WHERE FILM_ID IN (:ids)";
        namedParameterJdbcTemplate.query(sql, sqlParameterSource,
                (rs, rowNum) -> idsFilms.get(rs.getInt("FILM_ID"))
                                        .getGenres()
                                        .add(makeGenre(rs, rowNum)));
        return new ArrayList<>(idsFilms.values());
    }

    @Override
    public void saveGenres(Film film) {
        String sqlGenres = "";
        List<Integer> args = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            sqlGenres += "INSERT INTO GENRES(FILM_ID, GENRE_ID) VALUES (?, ?);\n";
            args.add(film.getId());
            args.add(genre.getId());
        }
        if (!args.isEmpty()) {
            jdbcTemplate.update(sqlGenres, args.toArray());
        }
    }

    @Override
    public void deleteGenresForIdFilm(int id) {
        String sql = "DELETE FROM GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("GENRE_ID");
        String name = rs.getString("GENRE_NAME");
        return new Genre(id, name);
    }
}
