package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final String SQL_GET_FILMS = "SELECT f.*,  rm.name AS mpa_name FROM films AS f " +
            "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id";
    private static final String UPDATE_SQL = "UPDATE films SET " + "name = ?," + "description = ?,"
            + "release_date = ?," + "duration = ?," + "rating_id = ?" + "WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO films (name,description,release_date,duration,rating_id)" +
            " VALUES (?,?,?,?,?)";
    private static final String SELECT_ID_SQL = "SELECT f.*,  rm.name AS mpa_name FROM films AS f " +
            "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id WHERE f.id = ?";
    private static final String SELECT_FAVORITE_SQL = "SELECT f.*," +
            " rm.name AS mpa_name FROM films AS f LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id LEFT JOIN likes AS" +
            " l ON f.id = l.film_id GROUP BY f.id ORDER BY COUNT(l.user_id) DESC LIMIT ?";
    private static final String SELECT_EX_ID_SQL = "SELECT id FROM films WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addedFilm(Film film) {
        KeyHolder id = new GeneratedKeyHolder();
        int sqlInsert = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, id);
        if (sqlInsert != 1) {
            throw new DataAccessException("Fail insert film to DB") {
            };
        }
        film.setId(Objects.requireNonNull(id.getKey()).intValue());
        log.info("Added film with id: {}", film.getId());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        log.info("List of {} films from DB",
                jdbcTemplate.queryForObject("SELECT COUNT (*) FROM FILMS", Integer.class));
        return jdbcTemplate.query(SQL_GET_FILMS, this::makeFilm);
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(UPDATE_SQL, film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        log.info("Updated film with ID: {}", film.getId());
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        return jdbcTemplate.queryForObject(SELECT_ID_SQL, this::makeFilm, id);
    }

    @Override
    public List<Film> getFavoriteFilms(int id) {
        log.info("Film with ID: {} from DB", id);
        return jdbcTemplate.query(SELECT_FAVORITE_SQL, this::makeFilm, id);
    }

    @Override
    public void isFilmExisted(int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(SELECT_EX_ID_SQL, id);
        if (!rowSet.next()) {
            throw new NotFoundException("Film with id: " + id + " does not exists");
        }
        log.info("Film with id: {} exists in DB", id);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa(rs.getInt("rating_id"), rs.getString("mpa_name"));
        return new Film(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"), mpa, new LinkedHashSet<>());
    }
}
