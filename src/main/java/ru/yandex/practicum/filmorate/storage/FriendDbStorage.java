package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;

@Repository
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int user, int friend) {
        String sql = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID) " +
                     "VALUES (?, ?)";
        jdbcTemplate.update(sql, user, friend);
    }

    @Override
    public void deleteFriend(int user, int friend) {
        String sql = "DELETE FROM FRIENDS WHERE (USER_ID = ? AND FRIEND_ID = ?)";
        jdbcTemplate.update(sql, user, friend);
    }
}
