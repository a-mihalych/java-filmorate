package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getFilms() {
        String sql = "SELECT * FROM FILMS AS F " +
                     "INNER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID";
        return jdbcTemplate.query(sql, this::makeFilm);
    }

    @Override
    public Film getFilm(int id) {
        String sql = "SELECT * FROM FILMS AS F " +
                     "INNER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                     "WHERE F.FILM_ID = ?";
        List<Film> films = jdbcTemplate.query(sql, this::makeFilm, id);
        if (films.size() != 1) {
            throw new NotFoundException("Не найден фильм");
        }
        return films.get(0);
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO FILMS(FILM_NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_ID) " +
                     "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate != null) {
                stmt.setDate(3, Date.valueOf(releaseDate));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }

    @Override
    public Film saveFilm(Film film) {
        String sqlFilm = "UPDATE FILMS SET " +
                         "FILM_NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, MPA_ID = ? " +
                         "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Integer deleteFilm(int id) {
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public Collection<Film> getFilmsLikes(int count) {
        String sql = "SELECT F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.RELEASEDATE, F.DURATION, " +
                            "F.MPA_ID, M.MPA_NAME, COUNT(L.FILM_ID) AS COUNT_LIKES " +
                     "FROM FILMS AS F " +
                     "INNER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                     "LEFT JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID " +
                     "GROUP BY F.FILM_ID " +
                     "ORDER BY COUNT_LIKES DESC " +
                     "LIMIT ?";
        return jdbcTemplate.query(sql, this::makeFilm, count);
    }

    private Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int mpaId = resultSet.getInt("MPA_ID");
        String mpaName = resultSet.getString("MPA_NAME");
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASEDATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(new Mpa(mpaId, mpaName))
                .genres(new LinkedHashSet())
                .build();
    }
}
