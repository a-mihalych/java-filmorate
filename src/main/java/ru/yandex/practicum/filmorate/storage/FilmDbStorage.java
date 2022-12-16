package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
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
        String sqlFilms = "SELECT * FROM FILMS AS F " +
                          "INNER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID";
        return jdbcTemplate.query(sqlFilms, (rs, rowNum) -> makeFilm(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("GENRE_ID");
        String name = rs.getString("GENRE_NAME");
        return new Genre(id, name);
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        int idFilm = resultSet.getInt("FILM_ID");
        String sqlGenre = "SELECT G.GENRE_ID, G.GENRE_NAME FROM GENRES AS GS " +
                          "INNER JOIN GENRE AS G ON GS.GENRE_ID = G.GENRE_ID " +
                          "WHERE GS.FILM_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlGenre, (rs, rowNum) -> makeGenre(rs), idFilm);
        int mpaId = resultSet.getInt("MPA_ID");
        String mpaName = resultSet.getString("MPA_NAME");
        return Film.builder()
                .id(idFilm)
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASEDATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(new Mpa(mpaId, mpaName))
                .genres(new LinkedHashSet(genres))
                .build();
    }

    @Override
    public Film getFilm(int id) {
        String sql = "SELECT * FROM FILMS AS F " +
                     "INNER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                     "WHERE F.FILM_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        String sqlGenre = "SELECT G.GENRE_ID, G.GENRE_NAME " +
                          "FROM GENRES AS GS " +
                          "INNER JOIN GENRE AS G ON GS.GENRE_ID = G.GENRE_ID " +
                          "WHERE GS.FILM_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlGenre, (rs, rowNum) -> makeGenre(rs), id);
        if (sqlRowSet.next()) {
            int mpaId = sqlRowSet.getInt("MPA_ID");
            String mpaName = sqlRowSet.getString("MPA_NAME");
            return Film.builder()
                    .id(sqlRowSet.getInt("FILM_ID"))
                    .name(sqlRowSet.getString("FILM_NAME"))
                    .description(sqlRowSet.getString("DESCRIPTION"))
                    .releaseDate(sqlRowSet.getDate("RELEASEDATE").toLocalDate())
                    .duration(sqlRowSet.getInt("DURATION"))
                    .mpa(new Mpa(mpaId, mpaName))
                    .genres(new LinkedHashSet(genres))
                    .build();
        }
        return null;
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
        saveGenres(film);
        return film;
    }

    @Override
    public Film saveFilm(Film film) {
        String sqlGenreDelete = "DELETE FROM GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlGenreDelete, film.getId());
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
        saveGenres(film);
        return film;
    }

    @Override
    public Integer deleteFilm(int id) {
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public void addLike(int id, int userId) {
        String sql = "INSERT INTO LIKES(FILM_ID, USER_ID) " +
                     "VALUES (?, ?)";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        String sql = "DELETE FROM LIKES WHERE (FILM_ID = ? AND USER_ID = ?)";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public Collection<Film> getFilmsLikes(int count) {
        String sqlLikes = "SELECT F.FILM_ID, COUNT(L.USER_ID) AS COUNT_LIKES " +
                "FROM FILMS AS F " +
                "LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT_LIKES DESC " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(sqlLikes, (rs, rowNum) -> makeFilmLikes(rs), count);
        return films;
    }

    private Film makeFilmLikes(ResultSet rs) throws SQLException {
        int idFilm = rs.getInt("FILM_ID");
        return getFilm(idFilm);
    }

    private void saveGenres(Film film) {
        if ((!(film.getGenres() == null)) && (!(film.getGenres().isEmpty()))) {
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
    }
}
