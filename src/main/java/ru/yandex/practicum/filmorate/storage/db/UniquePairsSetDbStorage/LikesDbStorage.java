package ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.UniquePairsSetStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikesDbStorage implements UniquePairsSetStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Long> getAllKeys2(Long key1) {
        String sqlQuery = "SELECT user_id FROM likesFromUsers WHERE film_id = ?";
        return UniquePairsSetStorage.executeRequest(jdbcTemplate, sqlQuery, key1);
    }

    @Override
    public void mergePair(Long key1, Long key2) {
        String sql = "MERGE INTO likesFromUsers (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, key1, key2);
    }

    @Override
    public void mergePair(Long key1, Set<Long> key2Set) {
        String sql = "MERGE INTO likesFromUsers (film_id, user_id) VALUES (?, ?)";
        Long[] key2Array = key2Set.toArray(new Long[0]);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, key1);
                ps.setLong(2, key2Array[i]);
            }

            @Override
            public int getBatchSize() {
                return key2Array.length;
            }
        });
    }

    @Override
    public void removePair(Long key1, Long key2) {
        String sql = "DELETE FROM likesFromUsers WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, key1, key2);
    }

    @Override
    public void removePairs(Long key1) {
        String sql = "DELETE FROM likesFromUsers WHERE film_id = ?";
        jdbcTemplate.update(sql, key1);
    }

    public List<Film> getTopFilms(int count) {
        String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, COUNT(l.user_id) as likes_count " +
                "FROM films f " +
                "LEFT JOIN likesFromUsers l ON f.id = l.film_id " +
                "GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id " +
                "ORDER BY likes_count DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, new Object[]{count}, new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                return Film.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("releaseDate").toLocalDate())
                        .duration(rs.getInt("duration"))
                        .mpaId(rs.getLong("mpa_id"))
                        .build();
            }
        });
    }
}
