package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryMPAStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MPAService extends AbstractService<MPA> {

    private final InMemoryMPAStorage mpaStorage;

    private final MPAService mpaService;

    @Override
    MPA create(MPA data) {
        return null;
    }

    @Override
    MPA update(MPA data) throws DataNotFoundException {
        return null;
    }

    @Override
    void clearAll() {

    }

    @Override
    List<MPA> getAll() {
        return null;
    }

    @Override
    MPA findById(Long id) throws DataNotFoundException {
        return null;
    }
}
