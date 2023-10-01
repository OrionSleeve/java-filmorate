package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage implements GenreStorage {
    public static final String INSERT_SQL = "INSERT INTO film_genre (film_id, genre_id) " + "VALUES(?,?)";
    public static final String SELECT_ID_SQL = "SELECT * FROM genres WHERE id = ?";
    public static final String SELECT_GENRE_SQL = "SELECT * FROM genres";
    public static final String DELETE_GENRE_SQL = "DELETE FROM film_genre WHERE film_id = ?";
    public static final String SELECT_NAME_SQL = "SELECT name FROM genres WHERE id = ?";
    public static final String SELECT_LOAD_SQL = "SELECT g.ID AS GENRE_ID," +
            " g.NAME AS GENRE_NAME," +
            " fg.FILM_ID FROM genres g " +
            "INNER JOIN film_genre fg ON g.ID = fg.GENRE_ID WHERE fg.FILM_ID IN (";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createFilmGenre(Film film) {
        if (film == null || film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        try {
            jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Iterator<Genre> iterator = film.getGenres().iterator();
                    for (int j = 0; j < i; j++) {
                        iterator.next();
                    }
                    Genre genre = iterator.next();
                    ps.setLong(1, film.getId());
                    ps.setLong(2, genre.getId());
                }

                @Override
                public int getBatchSize() {
                    return film.getGenres().size();
                }
            });
            log.info("Created {} film genre", film.getGenres().size());
        } catch (DataAccessException ex) {
            log.error("Error creating film genre: {} " + ex.getMessage(), ex.getMessage(), ex);
        }
    }

    @Override
    public Genre getGenreById(int id) {
        this.isGenreExisted(id);
        log.info("Retrieved genre by id: {}", id);
        return jdbcTemplate.queryForObject(SELECT_ID_SQL, this::createGenre, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = jdbcTemplate.query(SELECT_GENRE_SQL, this::createGenre);
        log.info("Get {} genres from DB", genres.size());
        return genres;
    }

    @Override
    public void updateFilmByGenre(Film film) {
        jdbcTemplate.update(DELETE_GENRE_SQL, film.getId());
        this.createFilmGenre(film);
        log.info("Film {} updated by genre", film.getId());
    }


    @Override
    public boolean isGenreExisted(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(SELECT_NAME_SQL, id);
        if (!sqlRowSet.next()) {
            throw new NotFoundException("Genre id: " + id + " does not exist");
        }
        return sqlRowSet.next();
    }


    @Override
    public void loadGenres(List<Film> films) {
        final Map<Integer, Film> filmMap = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        try {
            String sql = SELECT_LOAD_SQL + String.join(",", Collections.nCopies(films.size(), "?")) + ")";
            jdbcTemplate.query(sql, (rs) -> {
                int filmId = rs.getInt("FILM_ID");
                Film film = filmMap.get(filmId);
                if (film != null) {
                    film.addGenre(new Genre(rs.getInt("GENRE_ID"),
                            rs.getString("GENRE_NAME")));
                }
            }, films.stream().map(Film::getId).toArray());
        } catch (DataAccessException ex) {
            log.info("Load genre is Fail: {}", ex.getMessage());
            throw new DataException("Error from load genre", ex);
        }
    }

    private Genre createGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
