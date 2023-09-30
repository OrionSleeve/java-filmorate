package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    public static final String SELECT_ID_SQL = "SELECT * FROM rating_mpa WHERE id = ?";
    public static final String SELECT_ALL_SQL = "SELECT * FROM rating_mpa";
    public static final String SELECT_NAME_SQL = "SELECT name FROM rating_mpa WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(int id) {
        this.isMpaExisted(id);
        return jdbcTemplate.queryForObject(SELECT_ID_SQL, this::createMpa, id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(SELECT_ALL_SQL, this::createMpa);
    }

    @Override
    public boolean isMpaExisted(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(SELECT_NAME_SQL, id);
        if (!sqlRowSet.next()) {
            throw new NotFoundException("Mpa id: " + id + " does not exist");
        }
        return sqlRowSet.next();
    }

    private Mpa createMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
