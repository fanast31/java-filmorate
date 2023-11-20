package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Component
public class MPADbStorage implements MPAStorage {

    @Override
    public MPA findById(Long id) {
        return null;
    }

    @Override
    public List<MPA> getAll() {
        return null;
    }

    @Override
    public MPA create(MPA data) {
        throw new UnsupportedOperationException("the command is not supported");
    }

    @Override
    public MPA update(MPA data) {
        throw new UnsupportedOperationException("the command is not supported");
   }

    @Override
    public void clearAll() {
        throw new UnsupportedOperationException("the command is not supported");
    }
}
