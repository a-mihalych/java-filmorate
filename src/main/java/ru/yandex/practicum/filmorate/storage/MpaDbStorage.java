package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> getMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, this::makeMpa);
    }

    @Override
    public Mpa getMpaForId(int id) {
        String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";
        List<Mpa> mpas = jdbcTemplate.query(sql, this::makeMpa, id);
        if (mpas.size() != 1) {
            throw new NotFoundException("Не найден рейтинг MPA");
        }
        return mpas.get(0);
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("MPA_ID");
        String name = rs.getString("MPA_NAME");
        return new Mpa(id, name);
    }
}
