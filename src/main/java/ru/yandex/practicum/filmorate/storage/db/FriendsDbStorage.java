package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UniquePairsSetStorage;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class FriendsDbStorage implements UniquePairsSetStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Long> getAllKeys2(Long key1) {
        return null;
    }

    @Override
    public Set<Long> getAllKey1(Long key2) {
        return null;
    }

    @Override
    public void mergePair(Long key1, Long key2) {

    }

    @Override
    public void removePair(Long key1, Long key2) {

    }
}
