package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getUsers() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, this::makeUser);
    }

    @Override
    public User getUser(int id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        List<User> users = jdbcTemplate.query(sql, this::makeUser, id);
        if (users.size() != 1) {
            throw new NotFoundException("Не найден пользователь");
        }
        return users.get(0);
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO USERS(EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                     "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            LocalDate birthday = user.getBirthday();
            if (birthday != null) {
                stmt.setDate(4, Date.valueOf(birthday));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User saveUser(User user) {
        String sql = "UPDATE USERS SET " +
                     "EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ?" +
                     "WHERE USER_ID = ?";
        jdbcTemplate.update(sql,
                            user.getEmail(),
                            user.getLogin(),
                            user.getName(),
                            user.getBirthday(),
                            user.getId());
        return user;
    }

    @Override
    public Integer deleteUser(Integer id) {
        String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public Collection<User> getFriendsForIdUser(int id) {
        String sql = "SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.USER_NAME, U.BIRTHDAY " +
                     "FROM FRIENDS AS F " +
                     "INNER JOIN USERS AS U ON F.FRIEND_ID = U.USER_ID " +
                     "WHERE F.USER_ID = ?";
        return jdbcTemplate.query(sql, this::makeUser, id);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("USER_NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
