package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryFilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService extends AbstractService<Film> {

    private final InMemoryFilmStorage filmStorage;


    @Override
    public Film findById(Long id) throws DataNotFoundException {
        Film film = filmStorage.findById(id);
        if (film == null) {
            throw new DataNotFoundException("Film с id = " + id + " не найден");
        }
        return film;
    }

    @Override
    public Film create(Film data) {
        return filmStorage.create(data);
    }

    @Override
    public Film update(Film data) throws DataNotFoundException {
        return filmStorage.update(data);
    }

    @Override
    public void clearAll() {
        filmStorage.clearAll();
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

}
