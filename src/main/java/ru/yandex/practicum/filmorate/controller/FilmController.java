package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> likeFilm(@PathVariable Long filmId, @PathVariable Long userId) throws DataNotFoundException {
        // Логика лайка фильма
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> unlikeFilm(@PathVariable Long id, @PathVariable Long userId) throws DataNotFoundException {
        // Логика удаления лайка
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        // Логика получения списка популярных фильмов
        ArrayList<Film> filmsList = null;
        return ResponseEntity.status(HttpStatus.OK).body(filmsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable Long id) throws DataNotFoundException {
        Film film = null;
        return ResponseEntity.status(HttpStatus.OK).body(film);
    }



    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Film film) {
        Film newFilm = filmService.create(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFilm);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody Film film) throws DataNotFoundException {
        Film newFilm = filmService.update(film);
        return ResponseEntity.status(HttpStatus.OK).body(newFilm);
    }

    @DeleteMapping()
    public ResponseEntity<?> clearAll() {
        filmService.clearAll();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getAll());
    }

}
