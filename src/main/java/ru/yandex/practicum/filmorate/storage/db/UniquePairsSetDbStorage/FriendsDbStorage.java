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
public class FriendsDbStorage implements UniquePairsSetStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Long> getAllKeys2(Long key1) {
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ?";
        return UniquePairsSetStorage.executeRequest(jdbcTemplate, sqlQuery, key1);
    }

    @Override
    public Set<Long> getAllKey1(Long key2) {
        String sqlQuery = "SELECT user_id FROM friends WHERE friend_id = ?";
        return UniquePairsSetStorage.executeRequest(jdbcTemplate, sqlQuery, key2);
    }

    @Override
    public void mergePair(Long key1, Long key2) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, key1, key2);
    }

    @Override
    public void mergePair(Long key1, Set<Long> key2Set) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
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
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, key1, key2);
    }

    @Override
    public void removePairs(Long key1) {
        String sql = "DELETE FROM friends WHERE user_id = ?";
        jdbcTemplate.update(sql, key1);
    }
}
