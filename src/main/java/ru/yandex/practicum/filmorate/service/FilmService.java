package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage.FilmsGenresDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage.LikesDbStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractService<Film> {

    private final UserService userService;
    private final LikesDbStorage likesDbStorage;
    private final FilmsGenresDbStorage filmsGenresDbStorage;
    private final GenreService genreService;
    private final MPAService mpaService;

    public FilmService(AbstractStorage<Film> storage, UserService userService,
                       LikesDbStorage likesDbStorage, FilmsGenresDbStorage filmsGenresDbStorage,
                       GenreService genreService, MPAService mpaService) {
        super(storage);
        this.userService = userService;
        this.likesDbStorage = likesDbStorage;
        this.filmsGenresDbStorage = filmsGenresDbStorage;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

    @Override
    public void updateDependentDataInDB(Film data) {
        filmsGenresDbStorage.removePairs(data.getId());
        Set<Genre> dataGenres = data.getGenres();
        if (dataGenres.size() > 0) {
            filmsGenresDbStorage.mergePair(data.getId(),
                    dataGenres.stream().map(Genre::getId).collect(Collectors.toSet()));
        }
    }

    @Override
    public void updateDependentDataInObject(Film data) {

        MPA mpa = data.getMpa();
        if (mpa != null) {
            data.setMpa(mpaService.findById(mpa.getId()));
        }

        Set<Genre> dataGenres = data.getGenres();
        Set<Genre> newDataGenres = new HashSet<>();
        if (dataGenres.size() > 0) {
            List<Genre> allAvailableGenres = genreService.getAll();
            for (Genre genre : dataGenres) {
                if (allAvailableGenres.contains(genre)) {
                    newDataGenres.add(genre);
                } else {
                    throw new DataNotFoundException("Genre with id = " + genre.getId() + " not found");
                }
            }
        }
        data.setGenres(newDataGenres);

    }

    @Override
    public Film create(Film data) {
        updateDependentDataInObject(data);
        Film newData = super.create(data);
        updateDependentDataInDB(newData);
        return newData;
    }

    @Override
    public Film update(Film data) throws DataNotFoundException {
        updateDependentDataInObject(data);
        Film newData = super.update(data);
        updateDependentDataInDB(newData);
        return newData;
    }

    public void addLike(Long filmId, Long userId) throws DataNotFoundException {
        findById(filmId);
        userService.findById(userId);
        likesDbStorage.mergePair(filmId, userId);
    }

    public void addDislike(Long filmId, Long userId) throws DataNotFoundException {
        findById(filmId);
        userService.findById(userId);
        likesDbStorage.removePair(filmId, userId);

    }

    public List<Film> getTopFilms(int count) {
        return getAll().stream()
                .sorted(Comparator.comparing(film -> likesDbStorage.getAllKeys2(((Film) film).getId()).size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
