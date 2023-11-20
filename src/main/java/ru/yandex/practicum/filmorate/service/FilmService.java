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

    private final UserService userService;

    public void addLike(Long filmId, Long userId) throws DataNotFoundException {
        Film film = findById(filmId);
        userService.findById(userId);
        film.getLikesFromUsers().add(userId);
    }

    public void addDislike(Long filmId, Long userId) throws DataNotFoundException {
        Film film = findById(filmId);
        userService.findById(userId);
        film.getLikesFromUsers().remove(userId);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparing(film -> ((Film) film).getLikesFromUsers().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

}
