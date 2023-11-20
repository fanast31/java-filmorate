package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {

    @Override
    public Genre findById(Long id) {
        return null;
    }

    @Override
    public List<Genre> getAll() {
        return null;
    }

    @Override
    public Genre create(Genre data) {
        throw new UnsupportedOperationException("the command is not supported");
    }

    @Override
    public Genre update(Genre data) {
        throw new UnsupportedOperationException("the command is not supported");
    }

    @Override
    public void clearAll() {
        throw new UnsupportedOperationException("the command is not supported");
    }
}
