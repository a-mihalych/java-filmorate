package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

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
}
