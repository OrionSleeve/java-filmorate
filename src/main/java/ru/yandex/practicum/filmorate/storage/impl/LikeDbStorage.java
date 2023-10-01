package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    public static final String INSERT_SQL = "INSERT INTO likes (film_id, user_id) VALUES (?,?)";
    public static final String DELETE_SQL = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private final JdbcTemplate jdbcTemplate;


    @Override
    public void addLike(int id, int userId) {
        jdbcTemplate.update(INSERT_SQL, id, userId);
        log.info("Added like for film with id {} by user id {}", id, userId);
    }

    @Override
    public void removeLike(int id, int userId) {
        jdbcTemplate.update(DELETE_SQL, id, userId);
        log.info("Removed like for film with ID {} by user ID {}", id, userId);
    }
}
