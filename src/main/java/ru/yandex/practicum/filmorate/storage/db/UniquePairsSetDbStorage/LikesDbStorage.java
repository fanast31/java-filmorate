package ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.UniquePairsSetStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
}
