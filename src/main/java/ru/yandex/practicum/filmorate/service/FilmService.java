package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
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

    public FilmService(@Qualifier("filmDbStorage") AbstractStorage<Film> storage, UserService userService,
                       LikesDbStorage likesDbStorage, FilmsGenresDbStorage filmsGenresDbStorage,
                       GenreService genreService, MPAService mpaService) {
        super(storage);
        this.userService = userService;
        this.likesDbStorage = likesDbStorage;
        this.filmsGenresDbStorage = filmsGenresDbStorage;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

    private void checkDependentData(Film data) {

        MPA mpa = data.getMpa();
        if (mpa != null) {
            mpaService.findById(mpa.getId());
        }

        Set<Genre> dataGenres = data.getGenres();
        if (dataGenres != null && dataGenres.size() > 0) {
            for (Genre genre : dataGenres) {
                genreService.findById(genre.getId());
            }
        }
    }

    private void updateDependentDataInFilm(Film data) {

        Long mpaId = data.getMpaId();
        if (data.getMpa() != null) {
            mpaId = data.getMpa().getId();
        }
        if (mpaId != null) {
            data.setMpa(mpaService.findById(mpaId));
        }

        Set<Genre> newDataGenres = filmsGenresDbStorage.getAllKeys2(data.getId()).stream()
                .map(genreService::findById)
                .collect(Collectors.toSet());
        data.setGenres(newDataGenres);

    }

    private void createFilmsGenres(Film data) {
        Set<Genre> dataGenres = data.getGenres();
        filmsGenresDbStorage.removePairs(data.getId());
        if (dataGenres.size() > 0) {
            filmsGenresDbStorage.mergePair(data.getId(),
                    dataGenres.stream().map(Genre::getId).collect(Collectors.toSet()));
        }
    }

    @Override
    public Film create(Film data) {
        checkDependentData(data);
        super.create(data);
        createFilmsGenres(data);
        updateDependentDataInFilm(data);
        return data;
    }

    @Override
    public Film update(Film data) {
        checkDependentData(data);
        super.update(data);
        createFilmsGenres(data);
        updateDependentDataInFilm(data);
        return data;
    }

    public void addLike(Long filmId, Long userId) {
        findById(filmId);
        userService.findById(userId);
        likesDbStorage.mergePair(filmId, userId);
    }

    public void addDislike(Long filmId, Long userId) {
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
        list.forEach(this::updateDependentDataInFilm);
        return list;
    }

    @Override
    public Film findById(Long id) throws DataNotFoundException {
        Film unit = super.findById(id);
        updateDependentDataInFilm(unit);
        return unit;
    }
}
