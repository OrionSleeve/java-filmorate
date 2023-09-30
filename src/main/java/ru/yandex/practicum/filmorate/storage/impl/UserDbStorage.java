package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private static final String INSERT_SQL = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM users";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_ID_SQL = "SELECT id FROM users WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User newUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, (java.sql.Date.valueOf(user.getBirthday())));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Added user with id = {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(UPDATE_SQL,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("Update user with id = {}", user.getId());
        return user;
    }

    @Override
    public List<User> getUsers() {
        log.info("Get all users");
        return jdbcTemplate.query(SELECT_ALL_SQL, this::makeUser);
    }

    @Override
    public User getUserById(int id) {
        log.info("Get user with id = {}", id);
        return jdbcTemplate.queryForObject(SELECT_BY_ID_SQL, this::makeUser, id);
    }

    @Override
    public void isUserExisted(int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(SELECT_ID_SQL, id);
        if (!rowSet.next()) {
            throw new NotFoundException("User with id = " + id + " doesn't exist");
        }
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        log.info("Make users");
        return new User(rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}
