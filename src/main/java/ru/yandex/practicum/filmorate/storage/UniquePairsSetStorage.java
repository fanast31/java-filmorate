package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface UniquePairsSetStorage {

    Set<Long> getAllKeys2(Long key1);

    Set<Long> getAllKey1(Long key2);

    void mergePair(Long key1, Long key2);

    void removePair(Long key1, Long key2);

    static Set<Long> executeRequest(JdbcTemplate jdbcTemplate, String sqlQuery, Long value) {
        List<Long> resultList =
                jdbcTemplate.query(sqlQuery, new Object[]{value}, (rs, rowNum) -> rs.getLong(1));
        return new HashSet<>(resultList);
    }
}
