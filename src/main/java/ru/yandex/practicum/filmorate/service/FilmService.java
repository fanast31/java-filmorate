package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService extends AbstractService<Film> {

    private final InMemoryFilmStorage filmStorage;

    public void addLike(Long filmId, Long userId) throws DataNotFoundException {
        Film film = findById(userId);
        film = findById(filmId);
        film.getLikesFromUsers().add(userId);
    }

    public void addDislike(Long filmId, Long userId) throws DataNotFoundException {
        Film film = findById(userId);
        film = findById(filmId);
        film.getLikesFromUsers().remove(userId);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparing(film -> ((Film)film).getLikesFromUsers().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

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
