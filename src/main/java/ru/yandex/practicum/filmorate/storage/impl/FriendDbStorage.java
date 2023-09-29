package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addAsFriend(int id, int idFriend) {
        String sqlQuery = "INSERT INTO friendship (user_id, friend_user_id) VALUES (?,?)";
        log.info("Add friend with id = {} to user with id = {}", idFriend, id);
        jdbcTemplate.update(sqlQuery, id, idFriend);
    }

    @Override
    public void deleteFriend(int id, int idFriend) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_user_id = ?";
        log.info("Delete friend with id = {} to user with id = {}", idFriend, id);
        jdbcTemplate.update(sqlQuery, id, idFriend);
    }

    @Override
    public List<User> getCommonFriends(int id, int idFriend) {
        String sqlQuery = "SELECT * FROM users " +
                "WHERE id IN (SELECT friend_user_id FROM friendship WHERE user_id = ?)" +
                "AND id IN (SELECT friend_user_id FROM friendship WHERE user_id = ?)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id, idFriend);
        List<User> commonFriends = new ArrayList<>();
        while (rowSet.next()) {
            commonFriends.add(new User(rowSet.getInt("id"),
                    rowSet.getString("email"),
                    rowSet.getString("login"),
                    rowSet.getString("name"),
                    Objects.requireNonNull(rowSet.getDate("birthday")).toLocalDate()));
        }
        log.info("Get common friends");
        return commonFriends.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<User> getAllFriends(int id) {
        String sqlQuery = "SELECT * FROM users WHERE id IN " +
                "(SELECT friend_user_id AS id FROM friendship WHERE user_id = ?)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        List<User> friends = new ArrayList<>();
        while (rowSet.next()) {
            friends.add(new User(rowSet.getInt("id"),
                    rowSet.getString("email"),
                    rowSet.getString("login"),
                    rowSet.getString("name"),
                    Objects.requireNonNull(rowSet.getDate("birthday")).toLocalDate()));
        }
        log.info("Get all friends of user with id = {}", id);
        return friends;
    }
}
