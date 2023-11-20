
package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

@Service
public class GenreService extends AbstractService<Genre> {

    public GenreService(AbstractStorage<Genre> storage) {
        super(storage);
    }

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
}
