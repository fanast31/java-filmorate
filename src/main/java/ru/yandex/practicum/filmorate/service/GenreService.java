
package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

@Service
public class GenreService extends AbstractService<Genre> {

    public GenreService(@Qualifier("genreDbStorage") AbstractStorage<Genre> storage) {
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

    @Override
    void updateDependentDataInObject(Genre data) {
        throw new UnsupportedOperationException("the command is not supported");
    }

    @Override
    void updateDependentDataInDB(Genre data) {
        throw new UnsupportedOperationException("the command is not supported");
    }
}
