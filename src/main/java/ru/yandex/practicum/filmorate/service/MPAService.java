package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

@Service
public class MPAService extends AbstractService<MPA> {

    public MPAService(AbstractStorage<MPA> storage) {
        super(storage);
    }

    @Override
    public MPA create(MPA data) {
        throw new UnsupportedOperationException("the command is not supported");
    }

    @Override
    public MPA update(MPA data) throws DataNotFoundException {
        throw new UnsupportedOperationException("the command is not supported");
    }

    @Override
    public void clearAll() {
        throw new UnsupportedOperationException("the command is not supported");
    }

}
