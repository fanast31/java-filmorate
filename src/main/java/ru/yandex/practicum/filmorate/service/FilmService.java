package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage.FilmsGenresDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UniquePairsSetDbStorage.LikesDbStorage;

import java.util.*;
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
    public void updateDependentDataInObject(Film data) {

        Long mpaId = data.getMpaId();
        MPA mpa = data.getMpa();
        if (mpa != null) {
            mpaId = mpa.getId();
        }
        if (mpaId != null) {
            mpa = mpaService.findById(mpaId);
            data.setMpa(mpa);
        }

        Set<Genre> dataGenres = data.getGenres();
        Set<Genre> newDataGenres = new HashSet<>();
        if (dataGenres.size() > 0) {
            HashMap<Long, Genre> allAvailableGenres = new HashMap<>();
            for (int i = 0; i < genreService.getAll().size(); i++) {
                allAvailableGenres.put((long) i, genreService.getAll().get(i));
            }
            for (Genre genre : dataGenres) {
                Genre newGenre = allAvailableGenres.get(genre.getId());
                if (newGenre != null) {
                    newDataGenres.add(newGenre);
                } else {
                    throw new DataNotFoundException("Genre with id = " + genre.getId() + " not found");
                }
            }
        }
        data.setGenres(newDataGenres);
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

    @Override
    public List<Film> getAll() {
        List<Film> list = super.getAll();
        list.forEach(this::updateDependentDataInObject);
        return list;
    }

    @Override
    public Film findById(Long id) throws DataNotFoundException {
        Film unit = super.findById(id);
        updateDependentDataInObject(unit);
        return unit;
    }
}
