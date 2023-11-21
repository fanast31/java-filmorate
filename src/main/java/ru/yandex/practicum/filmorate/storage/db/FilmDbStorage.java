package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film findById(Long id) {
        return null;
    }

    @Override
    public Film create(Film data) {
        return null;
    }

    @Override
    public Film update(Film data) throws DataNotFoundException {
        return null;
    }

    @Override
    public List<Film> getAll() {
        return null;
    }

    @Override
    public void clearAll() {

    }
}
