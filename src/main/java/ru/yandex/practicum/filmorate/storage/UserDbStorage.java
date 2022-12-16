package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
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

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("USER_NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }

    @Override
    public User getUser(int id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.next()) {
            return User.builder()
                    .id(sqlRowSet.getInt("USER_ID"))
                    .email(sqlRowSet.getString("EMAIL"))
                    .login(sqlRowSet.getString("LOGIN"))
                    .name(sqlRowSet.getString("USER_NAME"))
                    .birthday(sqlRowSet.getDate("BIRTHDAY").toLocalDate())
                    .build();
        }
        return null;
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

    @Override
    public Collection<Integer> getIdFriendsForIdUser(int id) {
        String sql = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeIdFriend(rs), id);
    }

    private Integer makeIdFriend(ResultSet rs) throws SQLException {
        return rs.getInt("FRIEND_ID");
    }
}
