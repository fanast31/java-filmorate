package ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UniquePairsSetStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FilmsGenresDbStorage implements UniquePairsSetStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Long> getAllKeys2(Long key1) {
        String sqlQuery = "SELECT genre_id FROM filmsGenres WHERE film_id = ?";
        return UniquePairsSetStorage.executeRequest(jdbcTemplate, sqlQuery, key1);
    }

    @Override
    public Set<Long> getAllKey1(Long key2) {
        String sqlQuery = "SELECT film_id FROM filmsGenres WHERE genre_id = ?";
        return UniquePairsSetStorage.executeRequest(jdbcTemplate, sqlQuery, key2);
    }

    @Override
    public void mergePair(Long key1, Long key2) {
        String sql = "INSERT INTO filmsGenres (film_id, genre_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, key1, key2);
    }

    @Override
    public void removePair(Long key1, Long key2) {
        String sql = "DELETE FROM filmsGenres WHERE film_id = ? AND genre_id = ?";
        jdbcTemplate.update(sql, key1, key2);
    }
}
