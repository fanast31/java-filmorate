
package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryGenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService extends AbstractService<Genre> {

    private final InMemoryGenreStorage genreStorage;

    private final GenreService genreService;

    @Override
    public Genre create(Genre data) {
        throw new UnsupportedOperationException("the command is not supported");
    }

    @Override
    public Genre update(Genre data) throws DataNotFoundException {
        throw new UnsupportedOperationException("the command is not supported");
    }

    @Override
    public void clearAll() {
        throw new UnsupportedOperationException("the command is not supported");
    }

    @Override
    public List<Genre> getAll() {
        return null;
    }

    @Override
    public Genre findById(Long id) throws DataNotFoundException {
        return null;
    }
}
