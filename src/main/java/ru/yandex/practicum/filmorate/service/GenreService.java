
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
    Genre create(Genre data) {
        return null;
    }

    @Override
    Genre update(Genre data) throws DataNotFoundException {
        return null;
    }

    @Override
    void clearAll() {

    }

    @Override
    List<Genre> getAll() {
        return null;
    }

    @Override
    Genre findById(Long id) throws DataNotFoundException {
        return null;
    }
}
