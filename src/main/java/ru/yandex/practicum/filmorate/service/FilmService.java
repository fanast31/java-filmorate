package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage.FilmsGenresDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage.LikesDbStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractService<Film> {

    private final UserService userService;
    private final LikesDbStorage likesDbStorage;
    private final FilmsGenresDbStorage filmsGenresDbStorage;

    public FilmService(AbstractStorage<Film> storage, UserService userService,
                       LikesDbStorage likesDbStorage, FilmsGenresDbStorage filmsGenresDbStorage) {
        super(storage);
        this.userService = userService;
        this.likesDbStorage = likesDbStorage;
        this.filmsGenresDbStorage = filmsGenresDbStorage;
    }

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
        return getAll().stream()
                .sorted(Comparator.comparing(film -> ((Film) film).getLikesFromUsers().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

}
